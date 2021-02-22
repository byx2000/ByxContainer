package byx.container.factory.json.parser;

import byx.container.component.Component;
import byx.container.component.DelegateComponent;
import byx.container.factory.json.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static byx.container.component.Component.instanceFactory;
import static byx.container.component.Component.value;
import static byx.container.factory.json.ReservedKey.*;

public interface Parser
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
     * 处理属性
     */
    static Component processProperties(JsonElement element, ParserContext context, Component component)
    {
        if (element.containsKey(RESERVED_PROPERTIES))
        {
            JsonElement props = element.getElement(RESERVED_PROPERTIES);
            for (String name : props.keySet())
            {
                Component value = componentParser.parse(props.getElement(name), context);
                component = component.setProperty(name, value);
            }
        }
        return component;
    }

    /**
     * 处理setter方法
     */
    static Component processSetters(JsonElement element, ParserContext context, Component component)
    {
        if (element.containsKey(RESERVED_SETTERS))
        {
            JsonElement setters = element.getElement(RESERVED_SETTERS);
            for (String setterName : setters.keySet())
            {
                Component[] params = parseComponentList(setters.getElement(setterName), context);
                component = component.invokeSetter(setterName, params);
            }
        }
        return component;
    }

    /**
     * 处理单例
     */
    static Component processSingleton(JsonElement element, Component component)
    {
        boolean singleton = true;
        if (element.containsKey(RESERVED_SINGLETON))
        {
            singleton = element.getElement(RESERVED_SINGLETON).getBoolean();
        }
        return singleton ? component.singleton() : component;
    }

    /**
     * 处理后置处理器
     */
    static Component processPostProcessor(JsonElement element, ParserContext context, Component component)
    {
        if (element.containsKey(RESERVED_POST_PROCESSOR))
        {
            Component postProcessorComponent = componentParser.parse(element.getElement(RESERVED_POST_PROCESSOR), context);
            component = instanceFactory(
                    instanceFactory(
                            value(component),
                            "postProcess",
                            postProcessorComponent),
                    "create");
        }
        return component;
    }

    Parser componentParser = new ComponentParser();
    Parser primitiveParser = new PrimitiveParser();
    Parser listParser = new ListParser();
    Parser setParser = new SetParser();
    Parser mapParser = new MapParser();
    Parser referenceParser = new ReferenceParser();
    Parser constructorParser = new ConstructorParser();
    Parser staticFactoryParser = new StaticFactoryParser();
    Parser instanceFactoryParser = new InstanceFactoryParser();
    Parser conditionParser = new ConditionParser();
    Parser customParser = new CustomParser();
    Parser typeMatchParser = new TypeMatchParser();

    Map<String, Parser> parsers = new HashMap<>()
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
