package byx.container.exception;

/**
 * 元素类型错误
 */
public class ElementTypeIncorrectException extends ByxContainerException
{
    public ElementTypeIncorrectException(String json, String type)
    {
        super(String.format("This element is not %s: \n%s",
                type, json));
    }
}
