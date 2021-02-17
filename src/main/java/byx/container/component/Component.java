package byx.container.component;

import byx.container.Container;
import byx.container.exception.ByxContainerException;
import byx.container.exception.Message;
import byx.container.util.ReflectUtils;
import java.util.Arrays;
import java.util.Map;

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
     * 获取类型
     * @return 组件生成的类型
     */
    Class<?> getType();

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
        return new ConstructorComponent(type, params);
    }

    /**
     * 创建静态工厂Component
     * @param type 类型
     * @param method 方法名
     * @param params 参数组件
     * @return 静态工厂Component
     */
    static Component staticFactory(Class<?> type, String method, Component... params)
    {
        return new StaticFactoryComponent(type, method, params);
    }

    /**
     * 创建实例工厂Component
     * @param instance 实例组件
     * @param method 方法名
     * @param params 参数组件
     * @return 实例工厂Component
     */
    static Component instanceFactory(Component instance, String method, Component... params)
    {
        return new InstanceFactoryComponent(instance, method, params);
    }

    /**
     * 对组件创建的对象进行后置处理
     * @param processor 后置处理器
     * @return PostProcessComponent
     */
    default Component postProcess(PostProcessor processor)
    {
        return new PostProcessComponent(this, processor);
    }

    /**
     * 设置当前Component创建的对象的属性
     * @param property 属性名
     * @param value 属性值
     * @return MapperComponent
     */
    default Component setProperty(String property, Component value)
    {
        return this.postProcess(obj ->
        {
            Object v = value.create();
            try
            {
                ReflectUtils.setProperty(obj, property, v);
            }
            catch (Exception e)
            {
                throw new ByxContainerException(Message.propertyNotFount(obj.getClass(), property, v.getClass()), e);
            }
        });
    }

    /**
     * 在当前Component创建后的对象上调用setter方法
     * @param setter setter方法名称
     * @param params setter方法参数
     * @return MapperComponent
     */
    default Component invokeSetter(String setter, Component... params)
    {
        return this.postProcess(obj ->
        {
            Object[] p = Arrays.stream(params).map(Component::create).toArray();
            try
            {
                ReflectUtils.call(obj, setter, p);
            }
            catch (Exception e)
            {
                throw new ByxContainerException(Message.setterNotFound(obj.getClass(), setter, p), e);
            }
        });
    }

    /**
     * 将当前组件变为单例
     * @return SingletonComponent
     */
    default Component singleton()
    {
        return new SingletonComponent(this);
    }

    /**
     * 创建引用组件
     * @param container 容器
     * @param id 组件的唯一标识
     * @return ReferenceComponent
     */
    static Component reference(Container container, String id)
    {
        return new ReferenceComponent(container, id);
    }

    /**
     * 创建list组件
     * @param components 多个组件
     * @return ListComponent
     */
    static Component list(Component... components)
    {
        return new ListComponent(components);
    }

    /**
     * 创建set组件
     * @param components 组件列表
     * @return SetComponent
     */
    static Component set(Component... components)
    {
        return new SetComponent(components);
    }

    /**
     * 创建map组件
     * @param componentMap 组件map
     * @return MapComponent
     */
    static Component map(Map<Component, Component> componentMap)
    {
        return new MapComponent(componentMap);
    }

    /**
     * 创建条件组件
     * @param predicate 谓词
     * @param c1 predicate为true时返回的组件
     * @param c2 predicate为false时返回的组件
     * @return ConditionComponent
     */
    static Component condition(Component predicate, Component c1, Component c2)
    {
        return new ConditionComponent(predicate, c1, c2);
    }

    /**
     * 获取容器中指定类型的组件
     * @param container 容器
     * @param type 类型
     * @return TypeComponent
     */
    static Component type(Container container, Class<?> type)
    {
        return new TypeComponent(container, type);
    }
}
