package byx.container.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 反射工具类
 */
public class ReflectUtils
{
    /**
     * 创建对象
     * @param type 类型
     * @param params 构造函数参数
     * @param <T> 类型
     * @return 通过调用特定构造函数创建的对象
     */
    public static <T> T create(Class<T> type, Object... params)
    {
        try
        {
            Constructor<?> constructor = type.getConstructor(getTypes(params));
            return type.cast(constructor.newInstance(params));
        }
        catch (Exception e)
        {
            for (Constructor<?> constructor : type.getConstructors())
            {
                if (constructor.getParameterCount() == params.length)
                {
                    try
                    {
                        return type.cast(constructor.newInstance(params));
                    }
                    catch (Exception ignored) {}
                }
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 调用静态方法
     * @param type 类型
     * @param methodName 方法名
     * @param params 参数
     * @return 静态方法的返回值
     */
    public static Object call(Class<?> type, String methodName, Object... params)
    {
        try
        {
            Method method = type.getMethod(methodName, getTypes(params));
            return method.invoke(null, params);
        }
        catch (Exception e)
        {
            for (Method method : type.getMethods())
            {
                if (method.getName().equals(methodName) && method.getParameterCount() == params.length)
                {
                    try
                    {
                        return method.invoke(null, params);
                    }
                    catch (Exception ignored) {}
                }
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 调用对象方法
     * @param obj 对象
     * @param methodName 方法名
     * @param params 方法参数
     * @return 方法返回值
     */
    public static Object call(Object obj, String methodName, Object... params)
    {
        try
        {
            Method method = obj.getClass().getMethod(methodName, getTypes(params));
            return method.invoke(obj, params);
        }
        catch (Exception e)
        {
            for (Method method : obj.getClass().getMethods())
            {
                if (method.getName().equals(methodName) && method.getParameterCount() == params.length)
                {
                    try
                    {
                        return method.invoke(obj, params);
                    }
                    catch (Exception ignored) {}
                }
            }
            throw new RuntimeException(e);
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

    private static Class<?>[] getTypes(Object... params)
    {
        Class<?>[] types = new Class[params.length];
        for (int i = 0; i < params.length; ++i)
        {
            types[i] = params[i].getClass();
        }
        return types;
    }
}
