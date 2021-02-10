package byx.container.exception;

/**
 * 找不到组件
 */
public class ComponentNotFoundException extends ByxContainerException
{
    public ComponentNotFoundException(String id)
    {
        super(String.format("There is no component in container with id \"%s\".", id));
    }
}
