package byx.container.factory;

import byx.container.ByxContainer;
import byx.container.Container;
import byx.container.component.Component;
import byx.container.component.DelegateComponent;
import byx.container.component.Mapper;
import byx.container.component.MapperComponent;
import byx.container.exception.ByxContainerException;
import byx.container.exception.Message;
import com.alibaba.fastjson.JSON;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static byx.container.component.Component.*;

/**
 * 从Json格式的配置文件创建容器
 */
public class JsonContainerFactory implements ContainerFactory
{
    private final String json;
    private Container container;
    private List<Map<String, Component>> scopes;
    private Map<String, String> typeAlias;

    // 保留键值
    private static final String RESERVED_COMPONENTS = "components";
    private static final String RESERVED_LIST = "list";
    private static final String RESERVED_SET = "set";
    private static final String RESERVED_MAP = "map";
    private static final String RESERVED_KEY = "key";
    private static final String RESERVED_VALUE = "value";
    private static final String RESERVED_REF = "ref";
    private static final String RESERVED_LOCALS = "locals";
    private static final String RESERVED_CLASS = "class";
    private static final String RESERVED_PARAMETERS = "parameters";
    private static final String RESERVED_FACTORY = "factory";
    private static final String RESERVED_METHOD = "method";
    private static final String RESERVED_INSTANCE = "instance";
    private static final String RESERVED_PROPERTIES = "properties";
    private static final String RESERVED_SETTERS = "setters";
    private static final String RESERVED_IF = "if";
    private static final String RESERVED_THEN = "then";
    private static final String RESERVED_ELSE = "else";
    private static final String RESERVED_SINGLETON = "singleton";
    private static final String RESERVED_MAPPER = "mapper";
    private static final String RESERVED_CUSTOM = "custom";
    private static final String RESERVED_TYPE_ALIAS = "typeAlias";

    /**
     * 从文件流创建JsonContainerFactory
     * @param inputStream 文件流
     */
    public JsonContainerFactory(InputStream inputStream)
    {
        if (inputStream == null)
            throw new ByxContainerException(Message.parameterNotNull("inputStream"));
        this.json = readJsonFile(inputStream);
    }

    /**
     * 读取json文件
     */
    private static String readJsonFile(InputStream inputStream)
    {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)))
        {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                json.append(line);
            }
            return json.toString();
        }
        catch (Exception e)
        {
            throw new ByxContainerException("Error occurs when reading json file.", e);
        }
    }

    /**
     * 解析容器
     */
    private Container parseContainer(JsonElement element)
    {
        this.container = new ByxContainer();

        // 解析typeAlias
        typeAlias = new HashMap<>();
        if (element.containsKey(RESERVED_TYPE_ALIAS))
        {
            JsonElement typeAliasElem = element.getElement(RESERVED_TYPE_ALIAS);
            for (String alias : typeAliasElem.keySet())
            {
                typeAlias.put(alias, typeAliasElem.getElement(alias).getString());
            }
        }

        // 解析components
        scopes = new ArrayList<>();
        JsonElement components = element.getElement(RESERVED_COMPONENTS);
        for (String key : components.keySet())
        {
            Component c = parseComponent(components.getElement(key));
            container.addComponent(key, c);
        }
        return container;
    }

    /**
     * 加载类
     */
    private Class<?> getClass(String className)
    {
        try
        {
            if (typeAlias.containsKey(className))
                className = typeAlias.get(className);
            return Class.forName(className);
        }
        catch (Exception e)
        {
            throw new ByxContainerException(Message.invalidClassName(className), e);
        }
    }

    /**
     * 加载mapper
     */
    private Class<?> getMapper(String mapperClassName)
    {
        Class<?> type = getClass(mapperClassName);
        if (!Mapper.class.isAssignableFrom(type))
            throw new ByxContainerException(Message.invalidMapperClassName(mapperClassName));
        return type;
    }

    /**
     * 加载组件
     */
    private Class<?> getComponent(String componentClassName)
    {
        Class<?> type = getClass(componentClassName);
        if (!Component.class.isAssignableFrom(type))
            throw new ByxContainerException(Message.invalidComponentClassName(componentClassName));
        return type;
    }

    /**
     * 解析组件
     */
    private Component parseComponent(JsonElement element)
    {
        // 常数
        if (element.isPrimitive()) return parseValue(element);

        // 解析局部组件，创建当前作用域
        if (element.containsKey(RESERVED_LOCALS)) parseLocals(element);
        else scopes.add(new HashMap<>());

        Component component;

        // 列表
        if (element.containsKey(RESERVED_LIST)) component = parseList(element);
        // 集合
        else if (element.containsKey(RESERVED_SET)) component = parseSet(element);
        // map
        else if (element.containsKey(RESERVED_MAP)) component = parseMap(element);
        // 引用
        else if (element.containsKey(RESERVED_REF)) component = parseRef(element);
        // 构造函数注入
        else if (element.containsKey(RESERVED_CLASS))
        {
            String className = element.getElement(RESERVED_CLASS).getString();
            Component[] params = new Component[0];
            if (element.containsKey(RESERVED_PARAMETERS))
            {
                params = parseComponentList(element.getElement(RESERVED_PARAMETERS));
            }
            component = constructor(getClass(className), params);
        }
        // 静态工厂
        else if (element.containsKey(RESERVED_FACTORY))
        {
            String factory = element.getElement(RESERVED_FACTORY).getString();
            String method = element.getElement(RESERVED_METHOD).getString();
            Component[] params = new Component[0];
            if (element.containsKey(RESERVED_PARAMETERS))
            {
                params = parseComponentList(element.getElement(RESERVED_PARAMETERS));
            }
            component = staticFactory(getClass(factory), method, params);
        }
        // 实例工厂
        else if (element.containsKey(RESERVED_INSTANCE))
        {
            Component instance = parseComponent(element.getElement(RESERVED_INSTANCE));
            String method = element.getElement(RESERVED_METHOD).getString();
            Component[] params = new Component[0];
            if (element.containsKey(RESERVED_PARAMETERS))
            {
                params = parseComponentList(element.getElement(RESERVED_PARAMETERS));
            }
            component = instanceFactory(instance, method, params);
        }
        // 条件注入
        else if (element.containsKey(RESERVED_IF))
        {
            Component predicate = parseComponent(element.getElement(RESERVED_IF));
            Component c1 = parseComponent(element.getElement(RESERVED_THEN));
            Component c2 = parseComponent(element.getElement(RESERVED_ELSE));
            component = condition(predicate, c1, c2);
        }
        // 自定义组件
        else if (element.containsKey(RESERVED_CUSTOM))
        {
            component = parseCustomComponent(element);
        }
        // 未知注入方式
        else
        {
            throw new ByxContainerException(Message.unknownComponentType(element.getJsonString()));
        }

        // 处理属性
        if (element.containsKey(RESERVED_PROPERTIES))
        {
            component = parseProperties(element.getElement(RESERVED_PROPERTIES), component);
        }

        // 处理setter方法
        if (element.containsKey(RESERVED_SETTERS))
        {
            component = parseSetters(element.getElement(RESERVED_SETTERS), component);
        }

        // 单例
        boolean singleton = true;
        if (element.containsKey(RESERVED_SINGLETON))
        {
            singleton = element.getElement(RESERVED_SINGLETON).getBoolean();
        }

        // mapper
        if (element.containsKey(RESERVED_MAPPER))
        {
            component = parseMapper(element.getElement(RESERVED_MAPPER), component);
        }

        // 弹出当前作用域
        scopes.remove(scopes.size() - 1);
        return singleton ? component.singleton() : component;
    }

    /**
     * 解析常数
     */
    private Component parseValue(JsonElement element)
    {
        if (element.isInteger()) return value(element.getInteger());
        else if (element.isDouble()) return value(element.getDouble());
        else if (element.isString()) return value(element.getString());
        else if (element.isBoolean()) return value(element.getBoolean());
        else return value(null);
    }

    /**
     * 解析列表
     */
    private Component parseList(JsonElement element)
    {
        Component[] components = parseComponentList(element.getElement(RESERVED_LIST));
        return list(components);
    }

    /**
     * 解析集合
     */
    private Component parseSet(JsonElement element)
    {
        Component[] components = parseComponentList(element.getElement(RESERVED_SET));
        return set(components);
    }

    /**
     * 解析map
     */
    private Component parseMap(JsonElement element)
    {
        JsonElement mapElem = element.getElement(RESERVED_MAP);
        Map<Component, Component> componentMap = new HashMap<>();
        if (mapElem.isObject())
        {
            for (String key : mapElem.keySet())
            {
                componentMap.put(value(key), parseComponent(mapElem.getElement(key)));
            }
        }
        else
        {
            for (int i = 0; i < mapElem.getLength(); ++i)
            {
                JsonElement item = mapElem.getElement(i);
                componentMap.put(parseComponent(item.getElement(RESERVED_KEY)), parseComponent(item.getElement(RESERVED_VALUE)));
            }
        }
        return map(componentMap);
    }

    /**
     * 解析引用
     */
    private Component parseRef(JsonElement element)
    {
        JsonElement refName = element.getElement(RESERVED_REF);
        String id = refName.getString();
        for (int i = scopes.size() - 1; i >= 0; --i)
        {
            if (scopes.get(i).containsKey(id))
            {
                return scopes.get(i).get(id);
            }
        }
        return reference(container, id);
    }

    /**
     * 解析局部组件
     */
    private void parseLocals(JsonElement element)
    {
        Map<String, Component> scope = new HashMap<>();
        JsonElement locals = element.getElement(RESERVED_LOCALS);
        for (String key : locals.keySet())
        {
            scope.put(key, new DelegateComponent());
        }
        scopes.add(scope);
        for (String key : locals.keySet())
        {
            DelegateComponent c = (DelegateComponent) scope.get(key);
            c.setComponent(parseComponent(locals.getElement(key)));
        }
    }

    /**
     * 解析组件列表
     */
    private Component[] parseComponentList(JsonElement element)
    {
        List<Component> components = new ArrayList<>();
        for (int i = 0; i < element.getLength(); ++i)
        {
            components.add(parseComponent(element.getElement(i)));
        }
        return components.toArray(new Component[0]);
    }

    /**
     * 解析属性
     */
    private Component parseProperties(JsonElement element, Component component)
    {
        for (String name : element.keySet())
        {
            Component value = parseComponent(element.getElement(name));
            component = component.setProperty(name, value);
        }
        return component;
    }

    /**
     * 解析setter
     */
    private Component parseSetters(JsonElement element, Component component)
    {
        for (String setterName : element.keySet())
        {
            Component[] params = parseComponentList(element.getElement(setterName));
            component = component.invokeSetter(setterName, params);
        }
        return component;
    }

    /**
     * 解析mapper
     */
    private Component parseMapper(JsonElement element, Component component)
    {
        String className;
        Component[] params;
        if (element.isString())
        {
            className = element.getString();
            params = new Component[0];
        }
        else
        {
            className = element.getElement(RESERVED_CLASS).getString();
            params = parseComponentList(element.getElement(RESERVED_PARAMETERS));
        }
        return instanceFactory(
                constructor(
                        MapperComponent.class,
                        value(component),
                        constructor(
                                getMapper(className),
                                params))
                        .singleton(),
                "create");
    }

    /**
     * 解析自定义组件
     */
    private Component parseCustomComponent(JsonElement element)
    {
        String className = element.getElement(RESERVED_CUSTOM).getString();
        Component[] params = new Component[0];
        if (element.containsKey(RESERVED_PARAMETERS))
        {
            params = parseComponentList(element.getElement(RESERVED_PARAMETERS));
        }
        return instanceFactory(
                constructor(
                        getComponent(className),
                        params)
                        .singleton(),
                "create");
    }

    @Override
    public Container create()
    {
        return parseContainer(new JsonElementAdapterForFastjson(JSON.parse(json)));
    }
}
