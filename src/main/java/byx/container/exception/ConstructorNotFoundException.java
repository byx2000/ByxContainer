package byx.container.exception;

import java.util.Arrays;

/**
 * 找不到构造函数
 */
public class ConstructorNotFoundException extends ByxContainerException
{
    public ConstructorNotFoundException(Class<?> type, Object[] params)
    {
        super(String.format("Cannot find constructor of type \"%s\" with parameters %s.",
                type.getCanonicalName(), Arrays.toString(params)));
    }
}
