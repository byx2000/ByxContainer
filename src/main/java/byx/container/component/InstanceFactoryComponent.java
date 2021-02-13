package byx.container.component;

import byx.container.exception.ByxContainerException;
import byx.container.exception.Message;
import byx.container.util.ReflectUtils;

import java.util.Arrays;

/**
 * 使用实例工厂创建对象
 */
public class InstanceFactoryComponent implements Component
{
    private final Component instance;
    private final String method;
    private final Component[] params;

    public InstanceFactoryComponent(Component instance, String method, Component[] params)
    {
        this.instance = instance;
        this.method = method;
        this.params = params;
    }

    @Override
    public Object create()
    {
        Object i = instance.create();
        if (i == null) throw new ByxContainerException("Instance is null.");
        Object[] p = Arrays.stream(params).map(Component::create).toArray();

        try
        {
            return ReflectUtils.call(i, method, p);
        }
        catch (Exception e)
        {
            throw new ByxContainerException(Message.instanceFactoryNotFound(i.getClass(), method, p), e);
        }
    }
}
