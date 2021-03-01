package byx.container.factory.json;

import byx.container.core.Component;
import byx.container.exception.ByxContainerException;
import byx.container.exception.Message;

import java.util.ArrayList;
import java.util.List;

import static byx.container.core.Component.call;
import static byx.container.core.Component.value;
import static byx.container.factory.json.ComponentParser.COMPONENT_PARSER;
import static byx.container.factory.json.ComponentParser.parseComponentList;
import static byx.container.factory.json.ReservedKey.*;

/**
 * 组件后置处理器
 */
interface ComponentPostProcessor {
    /**
     * 处理组件
     *
     * @param element   当前Json元素
     * @param context   解析器上下文
     * @param component 已解析的组件
     * @return 处理后的组件
     */
    Component process(JsonElement element, ParserContext context, Component component);

    /**
     * 处理属性
     */
    ComponentPostProcessor PROCESS_PROPERTIES = (element, context, component) -> {
        if (element.containsKey(RESERVED_PROPERTIES)) {
            JsonElement props = element.getElement(RESERVED_PROPERTIES);
            for (String name : props.keySet()) {
                Component value = COMPONENT_PARSER.parse(props.getElement(name), context);
                component = component.setProperty(name, value);
            }
        }
        return component;
    };

    /**
     * 处理setter方法
     */
    ComponentPostProcessor PROCESS_SETTERS = (element, context, component) -> {
        if (element.containsKey(RESERVED_SETTERS)) {
            JsonElement setters = element.getElement(RESERVED_SETTERS);
            for (String setterName : setters.keySet()) {
                Component[] params = parseComponentList(setters.getElement(setterName), context);
                component = component.invokeSetter(setterName, params);
            }
        }
        return component;
    };

    /**
     * 处理单例
     */
    ComponentPostProcessor PROCESS_SINGLETON = (element, context, component) -> {
        boolean singleton = true;
        if (element.containsKey(RESERVED_SINGLETON)) {
            singleton = element.getElement(RESERVED_SINGLETON).getBoolean();
        }
        return singleton ? component.singleton() : component;
    };

    /**
     * 处理后置处理器
     */
    ComponentPostProcessor PROCESS_POST_PROCESSOR = (element, context, component) -> {
        if (element.containsKey(RESERVED_POST_PROCESSOR)) {
            Component postProcessorComponent = COMPONENT_PARSER.parse(element.getElement(RESERVED_POST_PROCESSOR), context);
            component = value(component).call("postProcess", postProcessorComponent).call("create");
        }
        return component;
    };

    /**
     * AOP处理器
     */
    ComponentPostProcessor PROCESS_AOP = (element, context, component) -> {
        if (element.containsKey(RESERVED_INTERCEPTOR)) {
            try {
                Component interceptorComponent = COMPONENT_PARSER.parse(element.getElement(RESERVED_INTERCEPTOR), context);
                Class<?> aopClass = Class.forName("byx.aop.AOP");
                component = call(aopClass, "proxy", component, interceptorComponent).castTo(component.getType());
            } catch (ClassNotFoundException e) {
                throw new ByxContainerException(Message.byxAopNotFound(), e);
            }
        }
        return component;
    };

    /**
     * 后置处理器列表
     * 组件创建后依次执行的后置处理器
     */
    List<ComponentPostProcessor> POST_PROCESSORS = new ArrayList<>() {{
        add(PROCESS_PROPERTIES);
        add(PROCESS_SETTERS);
        add(PROCESS_SINGLETON);
        add(PROCESS_POST_PROCESSOR);
        add(PROCESS_AOP);
    }};
}
