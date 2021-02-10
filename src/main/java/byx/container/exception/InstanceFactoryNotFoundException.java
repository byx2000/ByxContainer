package byx.container.exception;

import java.util.Arrays;

/**
 * 找不到实例工厂
 */
public class InstanceFactoryNotFoundException extends ByxContainerException
{
    public InstanceFactoryNotFoundException(Class<?> type, String methodName, Object[] params)
    {
        super(String.format("Cannot find instance factory \"%s\" of type \"%s\" with parameters %s.",
                methodName, type.getCanonicalName(), Arrays.toString(params)));
    }
}
