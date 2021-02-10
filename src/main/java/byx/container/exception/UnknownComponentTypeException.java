package byx.container.exception;

/**
 * 未知组件类型
 */
public class UnknownComponentTypeException extends ByxContainerException
{
    public UnknownComponentTypeException(String json)
    {
        super(String.format("Unknown component type:\n%s",
                json));
    }
}
