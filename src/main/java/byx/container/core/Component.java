package byx.container.core;

import byx.container.exception.ByxContainerException;
import byx.container.exception.Message;
import byx.container.util.ReflectUtils;

import java.util.*;

/**
 * 组件：对一个对象的创建过程的封装
 * 调用组件的create方法即可创建对应的对象
 * 这里包含了很多预定义的组件，它们都通过匿名内部类的方式来定义
 * 这里还包含了很多默认方法，用来方便地创建各种不同类型的组件
 */
public interface Component
{
    /**
     * 创建对象
     * @return 组件创建的对象
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
     */
    static Component value(Object value)
    {
        return new Component()
        {
            @Override
            public Object create()
            {
                return value;
            }

            @Override
            public Class<?> getType()
            {
                return value == null ? null : value.getClass();
            }
        };
    }

    /**
     * 创建构造函数Component
     * @param type 类型
     * @param params 参数组件
     */
    static Component constructor(Class<?> type, Component... params)
    {
        return new Component()
        {
            @Override
            public Object create()
            {
                Object[] p = Arrays.stream(params).map(Component::create).toArray();
                try
                {
                    return ReflectUtils.create(type, p);
                }
                catch (Exception e)
                {
                    throw new ByxContainerException(Message.constructorNotFound(type, p), e);
                }
            }

            @Override
            public Class<?> getType()
            {
                return type;
            }
        };
    }

    /**
     * 创建静态工厂Component
     * @param type 类型
     * @param method 方法名
     * @param params 参数组件
     */
    static Component call(Class<?> type, String method, Component... params)
    {
        return new Component()
        {
            @Override
            public Object create()
            {
                Object[] p = Arrays.stream(params).map(Component::create).toArray();
                try
                {
                    return ReflectUtils.call(type, method, p);
                }
                catch (Exception e)
                {
                    throw new ByxContainerException(Message.staticFactoryNotFound(type, method, p), e);
                }
            }

            @Override
            public Class<?> getType()
            {
                Class<?>[] parameterTypes = Arrays.stream(params).map(Component::getType).toArray(Class[]::new);
                return ReflectUtils.getReturnType(type, method, parameterTypes);
            }
        };
    }

    /**
     * 创建实例工厂Component
     * @param method 方法名
     * @param params 参数组件
     */
    default Component call(String method, Component... params)
    {
        return new Component()
        {
            @Override
            public Object create()
            {
                Object i = Component.this.create();
                if (i == null) throw new ByxContainerException("Instance is null.");
                Object[] p = Arrays.stream(params).map(Component::create).toArray();
                try
                {
                    return ReflectUtils.call(i, method, p);
                }
                catch (Exception e)
                {
                    throw new ByxContainerException(Message.instanceFactoryNotFound(i.getClass(), method, p), e);
                }
            }

            @Override
            public Class<?> getType()
            {
                Class<?> type = Component.this.getType();
                if (type == null) return null;
                Class<?>[] parameterTypes = Arrays.stream(params).map(Component::getType).toArray(Class[]::new);
                return ReflectUtils.getReturnType(type, method, parameterTypes);
            }
        };
    }

    /**
     * 对组件创建的对象进行后置处理
     * @param processor 后置处理器
     */
    default Component postProcess(PostProcessor processor)
    {
        return new Component()
        {
            @Override
            public Object create()
            {
                Object obj = Component.this.create();
                processor.process(obj);
                return obj;
            }

            @Override
            public Class<?> getType()
            {
                return Component.this.getType();
            }
        };
    }

    /**
     * 设置当前Component创建的对象的属性
     * @param property 属性名
     * @param value 属性值
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
     */
    default Component singleton()
    {
        final Object[] obj = { null };
        return new Component()
        {
            @Override
            public Object create()
            {
                if (obj[0] == null) obj[0] = Component.this.create();
                return obj[0];
            }

            @Override
            public Class<?> getType()
            {
                return Component.this.getType();
            }
        };
    }

    /**
     * 创建引用组件
     * @param container 容器
     * @param id 组件的唯一标识
     */
    static Component reference(Container container, String id)
    {
        return new Component()
        {
            @Override
            public Object create()
            {
                return container.getObject(id);
            }

            @Override
            public Class<?> getType()
            {
                return container.getType(id);
            }
        };
    }

    /**
     * 创建list组件
     * @param components 多个组件
     */
    static Component list(Component... components)
    {
        return new Component()
        {
            @Override
            public Object create()
            {
                List<Object> list = new ArrayList<>();
                for (Component c : components)
                {
                    list.add(c.create());
                }
                return list;
            }

            @Override
            public Class<?> getType()
            {
                return List.class;
            }
        };
    }

    /**
     * 创建set组件
     * @param components 组件列表
     */
    static Component set(Component... components)
    {
        return new Component()
        {
            @Override
            public Object create()
            {
                Set<Object> set = new HashSet<>();
                for (Component c : components)
                {
                    set.add(c.create());
                }
                return set;
            }

            @Override
            public Class<?> getType()
            {
                return Set.class;
            }
        };
    }

    /**
     * 创建map组件
     * @param componentMap 组件map
     */
    static Component map(Map<Component, Component> componentMap)
    {
        return new Component()
        {
            @Override
            public Object create()
            {
                Map<Object, Object> map = new HashMap<>();
                for (Component k : componentMap.keySet())
                {
                    map.put(k.create(), componentMap.get(k).create());
                }
                return map;
            }

            @Override
            public Class<?> getType()
            {
                return Map.class;
            }
        };
    }

    /**
     * 创建条件组件
     * @param predicate 谓词
     * @param c1 predicate为true时返回的组件
     * @param c2 predicate为false时返回的组件
     */
    static Component condition(Component predicate, Component c1, Component c2)
    {
        return new Component()
        {
            @Override
            public Object create()
            {
                Object p = predicate.create();
                if (p instanceof Boolean && (boolean) p) return c1.create();
                return c2.create();
            }

            @Override
            public Class<?> getType()
            {
                return null;
            }
        };
    }

    /**
     * 获取容器中指定类型的组件
     * @param container 容器
     * @param type 类型
     */
    static Component type(Container container, Class<?> type)
    {
        return new Component()
        {
            @Override
            public Object create()
            {
                return container.getObject(type);
            }

            @Override
            public Class<?> getType()
            {
                return null;
            }
        };
    }

    /**
     * 修改当前组件的类型
     * @param type 类型
     */
    default Component castTo(Class<?> type)
    {
        return new Component()
        {
            @Override
            public Object create()
            {
                return Component.this.create();
            }

            @Override
            public Class<?> getType()
            {
                return type;
            }
        };
    }
}
