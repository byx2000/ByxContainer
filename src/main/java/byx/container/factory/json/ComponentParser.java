package byx.container.factory.json;

import byx.container.core.Component;
import byx.container.core.DelegateComponent;
import byx.container.exception.ByxContainerException;
import byx.container.exception.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static byx.container.core.Component.*;
import static byx.container.factory.json.ReservedKey.*;
import static byx.container.factory.json.ComponentPostProcessor.*;

/**
 * 组件解析器
 * 用于将Json中的组件定义解析成一个Component
 */
public interface ComponentParser
{
    /**
     * 解析组件
     * @param element 当前Json元素
     * @param context 上下文
     * @return 组件
     */
    Component parse(JsonElement element, ParserContext context);

    /**
     * 解析组件列表
     */
    static Component[] parseComponentList(JsonElement element, ParserContext context)
    {
        List<Component> components = new ArrayList<>();
        for (int i = 0; i < element.getLength(); ++i)
        {
            components.add(componentParser.parse(element.getElement(i), context));
        }
        return components.toArray(new Component[0]);
    }

    /**
     * 处理局部组件
     */
    static void processLocals(JsonElement element, ParserContext context)
    {
        if (element.containsKey(RESERVED_LOCALS))
        {
            Map<String, Component> scope = new HashMap<>();
            JsonElement locals = element.getElement(RESERVED_LOCALS);
            for (String key : locals.keySet())
            {
                scope.put(key, new DelegateComponent());
            }
            context.pushScope(scope);
            for (String key : locals.keySet())
            {
                DelegateComponent c = (DelegateComponent) scope.get(key);
                c.setComponent(componentParser.parse(locals.getElement(key), context));
            }
        }
        else
        {
            context.pushScope(new HashMap<>());
        }
    }

    /**
     * 解析组件
     */
    ComponentParser componentParser = new ComponentParser()
    {
        @Override
        public Component parse(JsonElement element, ParserContext context)
        {
            if (element.isPrimitive()) return primitiveParser.parse(element, context);

            for (String key : parsers.keySet())
            {
                if (element.containsKey(key))
                {
                    // 处理局部组件
                    processLocals(element, context);
                    // 解析组件
                    Component c = parsers.get(key).parse(element, context);
                    // 后置处理
                    for (ComponentPostProcessor postProcessor : postProcessors)
                    {
                        c = postProcessor.process(element, context, c);
                    }
                    context.popScope();
                    return c;
                }
            }

            throw new ByxContainerException(Message.unknownComponentType(element.getJsonString()));
        }
    };

    /**
     * 解析基本类型常数
     */
    ComponentParser primitiveParser = (element, context) ->
    {
        if (element.isInteger()) return value(element.getInteger());
        else if (element.isDouble()) return value(element.getDouble());
        else if (element.isString()) return value(element.getString());
        else if (element.isBoolean()) return value(element.getBoolean());
        else return value(null);
    };

    /**
     * 解析List
     */
    ComponentParser listParser = (element, context) ->
    {
        Component[] components = parseComponentList(element.getElement(RESERVED_LIST), context);
        return list(components);
    };

    /**
     * 解析Set
     */
    ComponentParser setParser = (element, context) ->
    {
        Component[] components = parseComponentList(element.getElement(RESERVED_SET), context);
        return set(components);
    };

    /**
     * 解析Map
     */
    ComponentParser mapParser = (element, context) ->
    {
        JsonElement mapElem = element.getElement(RESERVED_MAP);
        Map<Component, Component> componentMap = new HashMap<>();
        if (mapElem.isObject())
        {
            for (String key : mapElem.keySet())
            {
                componentMap.put(
                        value(key),
                        componentParser.parse(mapElem.getElement(key), context));
            }
        }
        else
        {
            for (int i = 0; i < mapElem.getLength(); ++i)
            {
                JsonElement item = mapElem.getElement(i);
                componentMap.put(
                        componentParser.parse(item.getElement(RESERVED_KEY), context),
                        componentParser.parse(item.getElement(RESERVED_VALUE), context));
            }
        }
        return map(componentMap);
    };

    /**
     * 解析引用组件
     */
    ComponentParser referenceParser = (element, context) ->
    {
        String id = element.getElement(RESERVED_REF).getString();
        Component c = context.resolveComponentRef(id);
        if (c != null) return c;
        return reference(context.getContainer(), id);
    };

    /**
     * 解析构造函数
     */
    ComponentParser constructorParser = (element, context) ->
    {
        String className = element.getElement(RESERVED_CLASS).getString();
        Component[] params = new Component[0];
        if (element.containsKey(RESERVED_PARAMETERS))
        {
            params = parseComponentList(element.getElement(RESERVED_PARAMETERS), context);
        }
        return constructor(context.getClass(className), params);
    };

    /**
     * 解析静态工厂
     */
    ComponentParser staticFactoryParser = (element, context) ->
    {
        String factory = element.getElement(RESERVED_FACTORY).getString();
        String method = element.getElement(RESERVED_METHOD).getString();
        Component[] params = new Component[0];
        if (element.containsKey(RESERVED_PARAMETERS))
        {
            params = parseComponentList(element.getElement(RESERVED_PARAMETERS), context);
        }
        return call(context.getClass(factory), method, params);
    };

    /**
     * 解析实例工厂
     */
    ComponentParser instanceFactoryParser = (element, context) ->
    {
        Component instance = componentParser.parse(element.getElement(RESERVED_INSTANCE), context);
        String method = element.getElement(RESERVED_METHOD).getString();
        Component[] params = new Component[0];
        if (element.containsKey(RESERVED_PARAMETERS))
        {
            params = parseComponentList(element.getElement(RESERVED_PARAMETERS), context);
        }
        return instance.call(method, params);
    };

    /**
     * 解析条件组件
     */
    ComponentParser conditionParser = (element, context) ->
    {
        Component predicate = componentParser.parse(element.getElement(RESERVED_IF), context);
        Component c1 = componentParser.parse(element.getElement(RESERVED_THEN), context);
        Component c2 = componentParser.parse(element.getElement(RESERVED_ELSE), context);
        return condition(predicate, c1, c2);
    };

    /**
     * 解析自定义组件
     */
    ComponentParser customParser = (element, context) ->
    {
        Component customComponent = componentParser.parse(element.getElement(RESERVED_CUSTOM), context);
        return customComponent.call("create");
    };

    /**
     * 解析类型匹配组件
     */
    ComponentParser typeMatchParser = (element, context) ->
    {
        String typeName = element.getElement(RESERVED_TYPE).getString();
        return type(context.getContainer(), context.getClass(typeName));
    };

    /**
     * 解析器表
     * 根据关键键值获取对应的解析器
     */
    Map<String, ComponentParser> parsers = new HashMap<>()
    {{
        put(RESERVED_LIST, listParser);
        put(RESERVED_SET, setParser);
        put(RESERVED_MAP, mapParser);
        put(RESERVED_REF, referenceParser);
        put(RESERVED_CLASS, constructorParser);
        put(RESERVED_FACTORY, staticFactoryParser);
        put(RESERVED_INSTANCE, instanceFactoryParser);
        put(RESERVED_IF, conditionParser);
        put(RESERVED_CUSTOM, customParser);
        put(RESERVED_TYPE, typeMatchParser);
    }};
}
