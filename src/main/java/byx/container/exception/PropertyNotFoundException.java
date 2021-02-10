package byx.container.exception;

/**
 * 找不到属性
 */
public class PropertyNotFoundException extends ByxContainerException
{
    public PropertyNotFoundException(Class<?> type, String property, Class<?> valueType)
    {
        super(String.format("Cannot find property \"%s\" in \"%s\" of type \"%s\".",
                property, type.getCanonicalName(), valueType.getCanonicalName()));
    }
}
