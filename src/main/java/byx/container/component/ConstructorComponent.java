package byx.container.component;

import byx.container.exception.ByxContainerException;
import byx.container.exception.Message;
import byx.container.util.ReflectUtils;
import java.util.Arrays;

/**
 * 使用构造函数创建对象
 */
public class ConstructorComponent implements Component
{
    private final Class<?> type;
    private final Component[] params;

    public ConstructorComponent(Class<?> type, Component... params)
    {
        this.type = type;
        this.params = params;
    }

    @Override
    public Object create()
    {
        Object[] p = Arrays.stream(params).map(Component::create).toArray();

        try
        {
            return ReflectUtils.create(type, p);
        }
        catch (Exception e)
        {
            throw new ByxContainerException(Message.constructorNotFound(type, p), e);
        }
    }

    @Override
    public Class<?> getType()
    {
        return type;
    }
}
