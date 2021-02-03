package byx.container.core;

/**
 * 组件：能够从IOC容器中获取的一个对象。
 * 所有的组件都被IOC容器管理，每个组件都封装了自己与其他组件的依赖关系。
 * 换句话说，每个组件都知道如何创建自己，这些信息封装在create方法中。
 * 每次从IOC容器中获取某个组件时，该组件的create方法将被调用。
 * Component中还封装了一些常用的静态方法和默认方法
 */
public interface Component
{
    /**
     * 创建组件
     * @return 组件对象
     */
    Object create();

    /**
     * 创建ValueComponent
     * @param value 值
     * @return 用value构造的ValueComponent
     */
    static Component value(Object value)
    {
        return new ValueComponent(value);
    }

    /**
     * 创建构造函数Component
     * @param type 类型
     * @param params 参数组件
     * @return 构造函数Component
     */
    static Component constructor(Class<?> type, Component... params)
    {
        return new FunctionComponent(Function.constructor(type), params);
    }

    /**
     * 创建静态工厂Component
     * @param type 类型
     * @param name 方法名
     * @param params 参数组件
     * @return 静态工厂Component
     */
    static Component staticFactory(Class<?> type, String name, Component... params)
    {
        return new FunctionComponent(Function.staticFactory(type, name), params);
    }

    /**
     * 创建实例工厂Component
     * @param instance 实例组件
     * @param name 方法名
     * @param params 参数组件
     * @return 实例工厂Component
     */
    static Component instanceFactory(Component instance, String name, Component... params)
    {
        return () ->
        {
            Object obj = instance.create();
            return new FunctionComponent(Function.instanceFactory(obj, name), params).create();
        };
    }
}
