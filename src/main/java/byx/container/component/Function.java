package byx.container.component;

import byx.container.util.ReflectUtils;

/**
 * 函数：封装了可以延迟调用的一个函数。
 * Function可以表示构造函数、静态静态工厂和实例工厂。
 * Function中还包含了将上述三种情况适配成Function的静态方法。
 */
public interface Function
{
    /**
     * 调用
     * @param params 参数
     * @return 调用的返回值
     */
    Object call(Object... params);

    /**
     * 构造函数
     * @param type 类型
     * @return 构造函数的返回值
     */
    static Function constructor(Class<?> type)
    {
        return params -> ReflectUtils.create(type, params);
    }

    /**
     * 静态工厂
     * @param type 类型
     * @param name 方法名
     * @return 静态工厂的返回值
     */
    static Function staticFactory(Class<?> type, String name)
    {
        return params -> ReflectUtils.call(type, name, params);
    }

    /**
     * 实例工厂
     * @param instance 对象实例
     * @param name 方法名
     * @return 实例工厂的返回值
     */
    static Function instanceFactory(Object instance, String name)
    {
        return params -> ReflectUtils.call(instance, name, params);
    }
}
