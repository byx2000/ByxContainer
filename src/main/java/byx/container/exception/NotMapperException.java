package byx.container.exception;

/**
 * 不正确的Mapper类型
 */
public class NotMapperException extends ByxContainerException
{
    public NotMapperException(String className)
    {
        super(String.format("\"%s\" is not a \"byx.container.component.Mapper\".",
                className));
    }
}
