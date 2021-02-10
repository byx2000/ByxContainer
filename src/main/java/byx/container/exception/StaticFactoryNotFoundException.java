package byx.container.exception;

import java.util.Arrays;

/**
 * 找不到静态工厂函数
 */
public class StaticFactoryNotFoundException extends ByxContainerException
{
    public StaticFactoryNotFoundException(Class<?> factory, String methodName, Object[] params)
    {
        super(String.format("Cannot find static factory \"%s\" of type \"%s\" with parameters %s.",
                methodName, factory.getCanonicalName(), Arrays.toString(params)));
    }
}
