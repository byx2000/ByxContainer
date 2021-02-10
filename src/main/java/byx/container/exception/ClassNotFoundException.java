package byx.container.exception;

/**
 * 不正确的类型
 */
public class ClassNotFoundException extends ByxContainerException
{
    public ClassNotFoundException(String className)
    {
        super(String.format("Incorrect class name: %s.",
                className));
    }
}
