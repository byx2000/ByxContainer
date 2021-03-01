package byx.container.core;

import byx.container.exception.ByxContainerException;
import byx.container.exception.Message;
import byx.container.util.ReflectUtils;

import java.util.*;

/**
 * 组件：对一个对象的创建过程的封装
 * 调用Component的create方法即可创建对应的对象
 * 调用Component的getType方法可以获取组件创建的对象的类型
 * 这里包含了很多预定义的组件，它们都通过匿名内部类的方式来定义
 * 这里还包含了很多默认方法，用来方便地创建各种不同类型的组件
 *
 * @author byx
 */
public interface Component {
    /**
     * 创建对象
     *
     * @return 组件创建的对象
     */
    Object create();

    /**
     * 获取类型
     *
     * @return 组件创建的对象类型
     */
    Class<?> getType();

    /**
     * 值组件：封装了已创建的对象
     *
     * @param value 值
     * @return 值组件
     */
    static Component value(Object value) {
        return new Component() {
            @Override
            public Object create() {
                return value;
            }

            @Override
            public Class<?> getType() {
                return value == null ? null : value.getClass();
            }
        };
    }

    /**
     * 构造函数组件：封装了使用构造函数创建的对象
     *
     * @param type   对象类型
     * @param params 产生构造函数参数的组件数组
     * @return 构造函数组件
     */
    static Component constructor(Class<?> type, Component... params) {
        return new Component() {
            @Override
            public Object create() {
                Object[] p = Arrays.stream(params).map(Component::create).toArray();
                try {
                    return ReflectUtils.create(type, p);
                } catch (Exception e) {
                    throw new ByxContainerException(Message.constructorNotFound(type, p), e);
                }
            }

            @Override
            public Class<?> getType() {
                return type;
            }
        };
    }

    /**
     * 静态工厂组件：封装了使用静态工厂创建的对象
     *
     * @param type   工厂类
     * @param method 工厂方法名
     * @param params 产生方法参数的组件数组
     * @return 静态工厂组件
     */
    static Component call(Class<?> type, String method, Component... params) {
        return new Component() {
            @Override
            public Object create() {
                Object[] p = Arrays.stream(params).map(Component::create).toArray();
                try {
                    return ReflectUtils.call(type, method, p);
                } catch (Exception e) {
                    throw new ByxContainerException(Message.staticFactoryNotFound(type, method, p), e);
                }
            }

            @Override
            public Class<?> getType() {
                Class<?>[] parameterTypes = Arrays.stream(params).map(Component::getType).toArray(Class[]::new);
                return ReflectUtils.getReturnType(type, method, parameterTypes);
            }
        };
    }

    /**
     * 实例工厂组件：封装了使用实例工厂创建的组件
     *
     * @param method 实例方法名
     * @param params 产生方法参数的组件数组
     * @return 实例工厂组件
     */
    default Component call(String method, Component... params) {
        return new Component() {
            @Override
            public Object create() {
                Object i = Component.this.create();
                if (i == null) {
                    throw new ByxContainerException("Instance is null.");
                }
                Object[] p = Arrays.stream(params).map(Component::create).toArray();
                try {
                    return ReflectUtils.call(i, method, p);
                } catch (Exception e) {
                    throw new ByxContainerException(Message.instanceFactoryNotFound(i.getClass(), method, p), e);
                }
            }

            @Override
            public Class<?> getType() {
                Class<?> type = Component.this.getType();
                if (type == null) {
                    return null;
                }
                Class<?>[] parameterTypes = Arrays.stream(params).map(Component::getType).toArray(Class[]::new);
                return ReflectUtils.getReturnType(type, method, parameterTypes);
            }
        };
    }

    /**
     * 对当前组件创建的对象进行后置处理
     *
     * @param processor 后置处理器
     * @return 包含后置处理的组件
     */
    default Component postProcess(PostProcessor processor) {
        return new Component() {
            @Override
            public Object create() {
                Object obj = Component.this.create();
                processor.process(obj);
                return obj;
            }

            @Override
            public Class<?> getType() {
                return Component.this.getType();
            }
        };
    }

    /**
     * 对当前组件创建的对象进行属性设置
     *
     * @param property 属性名
     * @param value    属性值
     * @return 包含属性设置的组件
     */
    default Component setProperty(String property, Component value) {
        return this.postProcess(obj -> {
            Object v = value.create();
            try {
                ReflectUtils.setProperty(obj, property, v);
            } catch (Exception e) {
                throw new ByxContainerException(Message.propertyNotFount(obj.getClass(), property, v.getClass()), e);
            }
        });
    }

    /**
     * 在当前组件创建的对象上调用setter方法
     *
     * @param setter setter方法名称
     * @param params setter方法参数
     * @return 包含setter设置的组件
     */
    default Component invokeSetter(String setter, Component... params) {
        return this.postProcess(obj -> {
            Object[] p = Arrays.stream(params).map(Component::create).toArray();
            try {
                ReflectUtils.call(obj, setter, p);
            } catch (Exception e) {
                throw new ByxContainerException(Message.setterNotFound(obj.getClass(), setter, p), e);
            }
        });
    }

    /**
     * 将当前组件变为单例
     *
     * @return 单例组件
     */
    default Component singleton() {
        final Object[] obj = {null};
        return new Component() {
            @Override
            public Object create() {
                if (obj[0] == null) {
                    obj[0] = Component.this.create();
                }
                return obj[0];
            }

            @Override
            public Class<?> getType() {
                return Component.this.getType();
            }
        };
    }

    /**
     * 引用组件：对容器中指定id的对象的引用
     *
     * @param container 容器
     * @param id        组件id
     * @return 引用组件
     */
    static Component reference(Container container, String id) {
        return new Component() {
            @Override
            public Object create() {
                return container.getObject(id);
            }

            @Override
            public Class<?> getType() {
                return container.getType(id);
            }
        };
    }

    /**
     * 列表组件：封装List对象
     *
     * @param components 产生List元素的组件列表
     * @return 列表组件
     */
    static Component list(Component... components) {
        return new Component() {
            @Override
            public Object create() {
                List<Object> list = new ArrayList<>();
                for (Component c : components) {
                    list.add(c.create());
                }
                return list;
            }

            @Override
            public Class<?> getType() {
                return List.class;
            }
        };
    }

    /**
     * 集合组件：封装Set对象
     *
     * @param components 产生Set元素的组件列表
     * @return 集合组件
     */
    static Component set(Component... components) {
        return new Component() {
            @Override
            public Object create() {
                Set<Object> set = new HashSet<>();
                for (Component c : components) {
                    set.add(c.create());
                }
                return set;
            }

            @Override
            public Class<?> getType() {
                return Set.class;
            }
        };
    }

    /**
     * Map组件：封装Map对象
     *
     * @param componentMap 产生Map键值对的组件Map
     * @return Map组件
     */
    static Component map(Map<Component, Component> componentMap) {
        return new Component() {
            @Override
            public Object create() {
                Map<Object, Object> map = new HashMap<>(componentMap.size());
                for (Component k : componentMap.keySet()) {
                    map.put(k.create(), componentMap.get(k).create());
                }
                return map;
            }

            @Override
            public Class<?> getType() {
                return Map.class;
            }
        };
    }

    /**
     * 条件组件：根据不同条件创建不同的对象
     *
     * @param predicate 谓词
     * @param c1        predicate为true时的组件
     * @param c2        predicate为false时的组件
     * @return 条件组件
     */
    static Component condition(Component predicate, Component c1, Component c2) {
        return new Component() {
            @Override
            public Object create() {
                Object p = predicate.create();
                if (p instanceof Boolean && (boolean) p) {
                    return c1.create();
                }
                return c2.create();
            }

            @Override
            public Class<?> getType() {
                return null;
            }
        };
    }

    /**
     * 类型组件：获取容器中指定类型的对象
     *
     * @param container 容器
     * @param type      类型
     * @return 类型组件
     */
    static Component type(Container container, Class<?> type) {
        return new Component() {
            @Override
            public Object create() {
                return container.getObject(type);
            }

            @Override
            public Class<?> getType() {
                return null;
            }
        };
    }

    /**
     * 修改当前组件的类型
     *
     * @param type 类型
     * @return 修改类型后的组件
     */
    default Component castTo(Class<?> type) {
        return new Component() {
            @Override
            public Object create() {
                return Component.this.create();
            }

            @Override
            public Class<?> getType() {
                return type;
            }
        };
    }
}
