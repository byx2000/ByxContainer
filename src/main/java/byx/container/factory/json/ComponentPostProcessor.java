package byx.container.factory.json;

import byx.container.component.Component;

import java.util.HashMap;
import java.util.Map;

import static byx.container.component.Component.instanceFactory;
import static byx.container.component.Component.value;
import static byx.container.factory.json.ComponentParser.componentParser;
import static byx.container.factory.json.ComponentParser.parseComponentList;
import static byx.container.factory.json.ReservedKey.*;

/**
 * 组件后置处理器
 */
public interface ComponentPostProcessor
{
    /**
     * 处理组件
     * @param element 当前Json元素
     * @param context 解析器上下文
     * @param component 已解析的组件
     * @return 处理后的组件
     */
    Component process(JsonElement element, ParserContext context, Component component);

    /**
     * 处理属性
     */
    ComponentPostProcessor processProperties = (element, context, component) ->
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
    };

    /**
     * 处理setter方法
     */
    ComponentPostProcessor processSetters = (element, context, component) ->
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
    };

    /**
     * 处理单例
     */
    ComponentPostProcessor processSingleton = (element, context, component) ->
    {
        boolean singleton = true;
        if (element.containsKey(RESERVED_SINGLETON))
        {
            singleton = element.getElement(RESERVED_SINGLETON).getBoolean();
        }
        return singleton ? component.singleton() : component;
    };

    /**
     * 处理后置处理器
     */
    ComponentPostProcessor processPostProcessor = (element, context, component) ->
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
    };

    /**
     * 后置处理器表
     * 根据关键键值获取对应的后置处理器
     */
    Map<String, ComponentPostProcessor> postProcessors = new HashMap<>()
    {{
        put(RESERVED_PROPERTIES, processProperties);
        put(RESERVED_SETTERS, processSetters);
        put(RESERVED_SINGLETON, processSingleton);
        put(RESERVED_POST_PROCESSOR, processPostProcessor);
    }};
}
