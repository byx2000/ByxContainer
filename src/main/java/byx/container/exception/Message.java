package byx.container.exception;

import java.util.Arrays;

/**
 * 错误消息生成
 */
public abstract class Message
{
    /**
     * 找不到构造函数
     */
    public static String constructorNotFound(Class<?> type, Object[] params)
    {
        return String.format("Cannot find constructor of type \"%s\" with parameters %s.",
                type.getCanonicalName(), Arrays.toString(params));
    }

    /**
     * 找不到静态工厂
     */
    public static String staticFactoryNotFound(Class<?> factory, String methodName, Object[] params)
    {
        return String.format("Cannot find static factory \"%s\" of type \"%s\" with parameters %s.",
                methodName, factory.getCanonicalName(), Arrays.toString(params));
    }

    /**
     * 找不到实例工厂
     */
    public static String instanceFactoryNotFound(Class<?> type, String methodName, Object[] params)
    {
        return String.format("Cannot find instance factory \"%s\" of type \"%s\" with parameters %s.",
                methodName, type.getCanonicalName(), Arrays.toString(params));
    }

    /**
     * 找不到属性
     */
    public static String propertyNotFount(Class<?> type, String property, Class<?> valueType)
    {
        return String.format("Cannot find property \"%s\" in \"%s\" of type \"%s\".",
                property, type.getCanonicalName(), valueType.getCanonicalName());
    }

    /**
     * 找不到setter方法
     */
    public static String setterNotFound(Class<?> type, String setterName, Object[] params)
    {
        return String.format("Cannot find setter \"%s\" of type \"%s\" with parameters %s.",
                setterName, type.getCanonicalName(), Arrays.toString(params));
    }

    /**
     * 参数不能为null
     */
    public static String parameterNotNull(String name)
    {
        return String.format("Parameter \"%s\" cannot be null.",
                name);
    }

    /**
     * 找不到指定id的组件组件
     */
    public static String componentNotFoundWithId(String id)
    {
        return String.format("Cannot find component with id \"%s\".",
                id);
    }

    /**
     * 无效的类名
     */
    public static String invalidClassName(String className)
    {
        return String.format("Incorrect class name: %s.",
                className);
    }

    /**
     * 无效的Component类名
     */
    public static String invalidComponentClassName(String className)
    {
        return String.format("\"%s\" is not a \"byx.container.component.Component\".",
                className);
    }

    /**
     * 不正确的Json元素类型
     */
    public static String incorrectJsonElementType(String json, String type)
    {
        return String.format("This element is not %s: \n%s",
                type, json);
    }

    /**
     * 找不到Json元素的指定key
     */
    public static String jsonKeyNotFound(String json, String key)
    {
        return String.format("Cannot find key \"%s\" in this element:\n%s",
                key, json);
    }

    /**
     * 未知的组件类型
     */
    public static String unknownComponentType(String json)
    {
        return String.format("Unknown component type:\n%s",
                json);
    }

    /**
     * 找不到指定类型的组件
     */
    public static String componentNotFoundWithType(Class<?> type)
    {
        return String.format("Cannot find component with type \"%s\".",
                type.getCanonicalName());
    }

    /**
     * 存在多个指定类型的组件
     */
    public static String multiComponentsWithType(Class<?> type)
    {
        return String.format("There is more than one component with type \"%s\".",
                type.getCanonicalName());
    }
}
