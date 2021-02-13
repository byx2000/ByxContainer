package byx.container.component;

import byx.container.exception.ByxContainerException;
import byx.container.exception.Message;
import byx.container.util.ReflectUtils;

import java.util.Arrays;

/**
 * 使用静态工厂创建对象
 */
public class StaticFactoryComponent implements Component
{
    private final Class<?> type;
    private final String method;
    private final Component[] params;

    public StaticFactoryComponent(Class<?> type, String method, Component[] params)
    {
        this.type = type;
        this.method = method;
        this.params = params;
    }

    @Override
    public Object create()
    {
        Object[] p = Arrays.stream(params).map(Component::create).toArray();

        try
        {
            return ReflectUtils.call(type, method, p);
        }
        catch (Exception e)
        {
            throw new ByxContainerException(Message.staticFactoryNotFound(type, method, p));
        }
    }
}
