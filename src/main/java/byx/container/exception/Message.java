package byx.container.exception;

import java.util.Arrays;

/**
 * 错误消息生成
 */
public interface Message
{
    /**
     * 找不到构造函数
     */
    static String constructorNotFound(Class<?> type, Object[] params)
    {
        return String.format("Cannot find constructor of type \"%s\" with parameters %s.",
                type.getCanonicalName(), Arrays.toString(params));
    }

    /**
     * 找不到静态工厂
     */
    static String staticFactoryNotFound(Class<?> factory, String methodName, Object[] params)
    {
        return String.format("Cannot find static factory \"%s\" of type \"%s\" with parameters %s.",
                methodName, factory.getCanonicalName(), Arrays.toString(params));
    }

    /**
     * 找不到实例工厂
     */
    static String instanceFactoryNotFound(Class<?> type, String methodName, Object[] params)
    {
        return String.format("Cannot find instance factory \"%s\" of type \"%s\" with parameters %s.",
                methodName, type.getCanonicalName(), Arrays.toString(params));
    }

    /**
     * 找不到属性
     */
    static String propertyNotFount(Class<?> type, String property, Class<?> valueType)
    {
        return String.format("Cannot find property \"%s\" in \"%s\" of type \"%s\".",
                property, type.getCanonicalName(), valueType.getCanonicalName());
    }

    /**
     * 找不到setter方法
     */
    static String setterNotFound(Class<?> type, String setterName, Object[] params)
    {
        return String.format("Cannot find setter \"%s\" of type \"%s\" with parameters %s.",
                setterName, type.getCanonicalName(), Arrays.toString(params));
    }

    /**
     * 参数不能为null
     */
    static String parameterNotNull(String name)
    {
        return String.format("Parameter \"%s\" cannot be null.",
                name);
    }

    /**
     * 找不到组件
     */
    static String componentNotFound(String id)
    {
        return String.format("There is no component in container with id \"%s\".",
                id);
    }

    /**
     * 无效的类名
     */
    static String invalidClassName(String className)
    {
        return String.format("Incorrect class name: %s.",
                className);
    }

    /**
     * 无效的Mapper类名
     */
    static String invalidMapperClassName(String className)
    {
        return String.format("\"%s\" is not a \"byx.container.component.Mapper\".",
                className);
    }

    /**
     * 无效的Component类名
     */
    static String invalidComponentClassName(String className)
    {
        return String.format("\"%s\" is not a \"byx.container.component.Component\".",
                className);
    }

    /**
     * 不正确的Json元素类型
     */
    static String incorrectJsonElementType(String json, String type)
    {
        return String.format("This element is not %s: \n%s",
                type, json);
    }

    /**
     * 找不到Json元素的指定key
     */
    static String jsonKeyNotFound(String json, String key)
    {
        return String.format("Cannot find key \"%s\" in this element:\n%s",
                key, json);
    }

    /**
     * 未知的组件类型
     */
    static String unknownComponentType(String json)
    {
        return String.format("Unknown component type:\n%s",
                json);
    }
}
