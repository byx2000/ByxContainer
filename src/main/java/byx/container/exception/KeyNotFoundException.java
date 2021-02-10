package byx.container.exception;

/**
 * 缺少key
 */
public class KeyNotFoundException extends ByxContainerException
{
    public KeyNotFoundException(String json, String key)
    {
        super(String.format("Cannot find key \"%s\" in this element:\n%s",
                key, json));
    }
}
