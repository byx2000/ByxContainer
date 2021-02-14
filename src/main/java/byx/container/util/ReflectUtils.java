package byx.container.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtils
{
    private static final Map<Class<?>, Class<?>> primitiveAndWrap = new HashMap<>();

    static
    {
        primitiveAndWrap.put(byte.class, Byte.class);
        primitiveAndWrap.put(short.class, Short.class);
        primitiveAndWrap.put(int.class, Integer.class);
        primitiveAndWrap.put(long.class, Long.class);
        primitiveAndWrap.put(float.class, Float.class);
        primitiveAndWrap.put(double.class, Double.class);
        primitiveAndWrap.put(char.class, Character.class);
        primitiveAndWrap.put(boolean.class, Boolean.class);
    }

    /**
     * 判断是不是基本类型
     * @param type 类型
     * @return 如果type是基本类型，则返回true，否则返回false
     */
    public static boolean isPrimitive(Class<?> type)
    {
        return primitiveAndWrap.containsKey(type);
    }

    /**
     * 获取包装类型
     * @param type 类型
     * @return 如果type是基本类型，则返回对应的包装类型，否则返回type
     */
    public static Class<?> getWrap(Class<?> type)
    {
        if (!isPrimitive(type)) return type;
        return primitiveAndWrap.get(type);
    }

    /**
     * 获取基本类型
     * @param type 类型
     * @return 如果type是包装类型，则返回对应的基本类型，否则返回type
     */
    public static Class<?> getPrimitive(Class<?> type)
    {
        for (Class<?> key : primitiveAndWrap.keySet())
        {
            if (primitiveAndWrap.get(key) == type) return key;
        }
        return type;
    }

    /**
     * 调用构造函数创建对象
     * @param type 要创建对象的类型
     * @param params 参数
     * @param <T> 返回类型
     * @return 调用构造函数创建的对象
     */
    public static <T> T create(Class<T> type, Object... params)
    {
        try
        {
            return type.cast(getConstructor(type, getTypes(params)).newInstance(params));
        }
        catch (Exception e)
        {
            throw new RuntimeException(String.format("Cannot invoke constructor of \"%s\" with parameters %s.",
                    type.getCanonicalName(), Arrays.toString(params)), e);
        }
    }

    /**
     * 调用静态方法
     * @param type 类型
     * @param methodName 方法名
     * @param params 参数
     * @param <T> 返回类型
     * @return 静态方法的返回值
     */
    @SuppressWarnings("unchecked")
    public static <T> T call(Class<?> type, String methodName, Object... params)
    {
        try
        {
            return (T) getMethod(type, methodName, getTypes(params)).invoke(null, params);
        }
        catch (Exception e)
        {
            throw new RuntimeException(String.format("Cannot invoke static method \"%s\" of \"%s\" with parameters %s.",
                    methodName, type.getCanonicalName(), Arrays.toString(params)), e);
        }
    }

    /**
     * 调用实例方法
     * @param obj 实例对象
     * @param methodName 方法名
     * @param params 参数
     * @param <T> 返回类型
     * @return 实例方法的返回值
     */
    @SuppressWarnings("unchecked")
    public static <T> T call(Object obj, String methodName, Object... params)
    {
        try
        {
            return (T) getMethod(obj.getClass(), methodName, getTypes(params)).invoke(obj, params);
        }
        catch (Exception e)
        {
            throw new RuntimeException(String.format("Cannot invoke method \"%s\" of \"%s\" with parameters %s.",
                    methodName, obj.getClass().getCanonicalName(), Arrays.toString(params)), e);
        }
    }

    /**
     * 设置JavaBean的属性
     * @param bean JavaBean实例
     * @param propertyName 属性名
     * @param value 值
     */
    public static void setProperty(Object bean, String propertyName, Object value)
    {
        try
        {
            PropertyDescriptor pd = new PropertyDescriptor(propertyName, bean.getClass());
            Method setter = pd.getWriteMethod();
            setter.invoke(bean, value);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取方法返回值类型
     * @param type 方法所属的类
     * @param methodName 方法名
     * @param parameterTypes 参数类型数组
     * @return 方法返回值类型
     */
    public static Class<?> getReturnType(Class<?> type, String methodName, Class<?>... parameterTypes)
    {
        return getMethod(type, methodName, parameterTypes).getReturnType();
    }

    /**
     * 根据参数数组获取类型数组
     */
    private static Class<?>[] getTypes(Object... params)
    {
        Class<?>[] types = new Class[params.length];
        for (int i = 0; i < params.length; ++i)
        {
            types[i] = params[i].getClass();
        }
        return types;
    }

    /**
     * 判断两个类型是否匹配（相同或者为包装类型关系）
     */
    private static boolean match(Class<?> declaredType, Class<?> actualType)
    {
        return getWrap(declaredType).isAssignableFrom(getWrap(actualType));
    }

    /**
     * 判断两个类型列表是否匹配（列表长度相同且对应位置的类型相匹配）
     */
    private static boolean match(Class<?>[]c1, Class<?>[] c2)
    {
        if (c1.length == c2.length)
        {
            for (int i = 0; i < c1.length; ++i)
            {
                if (!match(c1[i], c2[i])) return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 根据参数类型获取构造函数
     */
    private static Constructor<?> getConstructor(Class<?> type, Class<?>[] parameterTypes)
    {
        try
        {
            return type.getConstructor(parameterTypes);
        }
        catch (Exception e)
        {
            for (Constructor<?> constructor : type.getConstructors())
            {
                if (constructor.getParameterCount() == parameterTypes.length)
                {
                    if (match(constructor.getParameterTypes(), parameterTypes))
                        return constructor;
                }
            }
            throw new RuntimeException(String.format("Cannot find constructor of \"%s\" with parameter types %s.",
                    type.getCanonicalName(), Arrays.toString(parameterTypes)), e);
        }
    }

    /**
     * 根据参数类型和方法名获取方法
     */
    private static Method getMethod(Class<?> type, String name, Class<?>[] parameterTypes)
    {
        try
        {
            return type.getMethod(name, parameterTypes);
        }
        catch (Exception e)
        {
            for (Method method : type.getMethods())
            {
                if (method.getName().equals(name) && method.getParameterCount() == parameterTypes.length)
                {
                    if (match(method.getParameterTypes(), parameterTypes))
                        return method;
                }
            }
            throw new RuntimeException(String.format("Cannot find method \"%s\" of \"%s\" with parameter types %s.",
                    name, type.getCanonicalName(), Arrays.toString(parameterTypes)), e);
        }
    }
}
