package byx.container.factory.json;

import byx.container.core.CachedContainer;
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
 *
 * @author byx
 */
interface ComponentParser {
    /**
     * 解析组件
     *
     * @param element 当前Json元素
     * @param context 上下文
     * @return 组件
     */
    Component parse(JsonElement element, ParserContext context);

    /**
     * 解析组件列表
     *
     * @param element 当前Json元素
     * @param context 解析器上下文
     * @return 组件列表
     */
    static Component[] parseComponentList(JsonElement element, ParserContext context) {
        List<Component> components = new ArrayList<>();
        for (int i = 0; i < element.getLength(); ++i) {
            components.add(COMPONENT_PARSER.parse(element.getElement(i), context));
        }
        return components.toArray(new Component[0]);
    }

    /**
     * 处理局部组件
     *
     * @param element 当前Json元素
     * @param context 解析器上下文
     */
    static void processLocals(JsonElement element, ParserContext context) {
        if (element.containsKey(RESERVED_LOCALS)) {
            Map<String, Component> scope = new HashMap<>(7);
            JsonElement locals = element.getElement(RESERVED_LOCALS);
            for (String key : locals.keySet()) {
                scope.put(key, new DelegateComponent());
            }
            context.pushScope(scope);
            for (String key : locals.keySet()) {
                DelegateComponent c = (DelegateComponent) scope.get(key);
                c.setComponent(COMPONENT_PARSER.parse(locals.getElement(key), context));
            }
        } else {
            context.pushScope(new HashMap<>(0));
        }
    }

    /**
     * 解析组件
     */
    ComponentParser COMPONENT_PARSER = new ComponentParser() {
        @Override
        public Component parse(JsonElement element, ParserContext context) {
            if (element.isPrimitive()) {
                return PRIMITIVE_PARSER.parse(element, context);
            }

            for (String key : PARSERS.keySet()) {
                if (element.containsKey(key)) {
                    // 处理局部组件
                    boolean inGlobalComponent = context.isInGlobalComponent();
                    if (inGlobalComponent) {
                        context.setInGlobalComponent(false);
                    }
                    processLocals(element, context);
                    if (inGlobalComponent) {
                        context.setInGlobalComponent(true);
                    }
                    // 解析组件
                    Component c = PARSERS.get(key).parse(element, context);
                    if (inGlobalComponent) {
                        c = c.cache(context.getGlobalComponentId(), (CachedContainer) context.getContainer());
                    }

                    // 后置处理
                    for (ComponentPostProcessor postProcessor : POST_PROCESSORS) {
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
    ComponentParser PRIMITIVE_PARSER = (element, context) -> {
        if (element.isInteger()) {
            return value(element.getInteger());
        } else if (element.isDouble()) {
            return value(element.getDouble());
        } else if (element.isString()) {
            return value(element.getString());
        } else if (element.isBoolean()) {
            return value(element.getBoolean());
        } else {
            return value(null);
        }
    };

    /**
     * 解析List
     */
    ComponentParser LIST_PARSER = (element, context) -> {
        Component[] components = parseComponentList(element.getElement(RESERVED_LIST), context);
        return list(components);
    };

    /**
     * 解析Set
     */
    ComponentParser SET_PARSER = (element, context) -> {
        Component[] components = parseComponentList(element.getElement(RESERVED_SET), context);
        return set(components);
    };

    /**
     * 解析Map
     */
    ComponentParser MAP_PARSER = (element, context) -> {
        JsonElement mapElem = element.getElement(RESERVED_MAP);
        Map<Component, Component> componentMap = new HashMap<>();
        if (mapElem.isObject()) {
            for (String key : mapElem.keySet()) {
                componentMap.put(value(key), COMPONENT_PARSER.parse(mapElem.getElement(key), context));
            }
        } else {
            for (int i = 0; i < mapElem.getLength(); ++i) {
                JsonElement item = mapElem.getElement(i);
                componentMap.put(COMPONENT_PARSER.parse(item.getElement(RESERVED_KEY), context), COMPONENT_PARSER.parse(item.getElement(RESERVED_VALUE), context));
            }
        }
        return map(componentMap);
    };

    /**
     * 解析引用组件
     */
    ComponentParser REFERENCE_PARSER = (element, context) -> {
        String id = element.getElement(RESERVED_REF).getString();
        Component c = context.resolveComponentRef(id);
        if (c != null) {
            return c;
        }
        return reference(context.getContainer(), id);
    };

    /**
     * 解析构造函数
     */
    ComponentParser CONSTRUCTOR_PARSER = (element, context) -> {
        String className = element.getElement(RESERVED_CLASS).getString();
        Component[] params = new Component[0];
        if (element.containsKey(RESERVED_PARAMETERS)) {
            params = parseComponentList(element.getElement(RESERVED_PARAMETERS), context);
        }
        return constructor(context.getClass(className), params);
    };

    /**
     * 解析静态工厂
     */
    ComponentParser STATIC_FACTORY_PARSER = (element, context) -> {
        String factory = element.getElement(RESERVED_FACTORY).getString();
        String method = element.getElement(RESERVED_METHOD).getString();
        Component[] params = new Component[0];
        if (element.containsKey(RESERVED_PARAMETERS)) {
            params = parseComponentList(element.getElement(RESERVED_PARAMETERS), context);
        }
        return call(context.getClass(factory), method, params);
    };

    /**
     * 解析实例工厂
     */
    ComponentParser INSTANCE_FACTORY_PARSER = (element, context) -> {
        Component instance = COMPONENT_PARSER.parse(element.getElement(RESERVED_INSTANCE), context);
        String method = element.getElement(RESERVED_METHOD).getString();
        Component[] params = new Component[0];
        if (element.containsKey(RESERVED_PARAMETERS)) {
            params = parseComponentList(element.getElement(RESERVED_PARAMETERS), context);
        }
        return instance.call(method, params);
    };

    /**
     * 解析条件组件
     */
    ComponentParser CONDITION_PARSER = (element, context) -> {
        Component predicate = COMPONENT_PARSER.parse(element.getElement(RESERVED_IF), context);
        Component c1 = COMPONENT_PARSER.parse(element.getElement(RESERVED_THEN), context);
        Component c2 = COMPONENT_PARSER.parse(element.getElement(RESERVED_ELSE), context);
        return condition(predicate, c1, c2);
    };

    /**
     * 解析自定义组件
     */
    ComponentParser CUSTOM_PARSER = (element, context) -> {
        Component customComponent = COMPONENT_PARSER.parse(element.getElement(RESERVED_CUSTOM), context);
        return customComponent.call("create");
    };

    /**
     * 解析类型匹配组件
     */
    ComponentParser TYPE_MATCH_PARSER = (element, context) -> {
        String typeName = element.getElement(RESERVED_TYPE).getString();
        return type(context.getContainer(), context.getClass(typeName));
    };

    /**
     * 解析器表
     * 根据关键键值获取对应的解析器
     */
    Map<String, ComponentParser> PARSERS = new HashMap<>() {{
        put(RESERVED_LIST, LIST_PARSER);
        put(RESERVED_SET, SET_PARSER);
        put(RESERVED_MAP, MAP_PARSER);
        put(RESERVED_REF, REFERENCE_PARSER);
        put(RESERVED_CLASS, CONSTRUCTOR_PARSER);
        put(RESERVED_FACTORY, STATIC_FACTORY_PARSER);
        put(RESERVED_INSTANCE, INSTANCE_FACTORY_PARSER);
        put(RESERVED_IF, CONDITION_PARSER);
        put(RESERVED_CUSTOM, CUSTOM_PARSER);
        put(RESERVED_TYPE, TYPE_MATCH_PARSER);
    }};
}
