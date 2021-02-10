package byx.container.exception;

import java.util.Arrays;

/**
 * 找不到setter方法
 */
public class SetterNotFoundException extends ByxContainerException
{
    public SetterNotFoundException(Class<?> type, String setterName, Object[] params)
    {
        super(String.format("Cannot find setter \"%s\" of type \"%s\" with parameters %s.",
                setterName, type.getCanonicalName(), Arrays.toString(params)));
    }
}
