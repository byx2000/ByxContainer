package byx.container.exception;

/**
 * 不正确的Component类型
 */
public class NotComponentException extends ByxContainerException
{
    public NotComponentException(String className)
    {
        super(String.format("\"%s\" is not a \"byx.container.component.Component\".",
                className));
    }
}
