package byx.container;

import byx.container.component.Component;
import byx.container.exception.ByxContainerException;
import byx.container.exception.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IOC容器实现类
 */
public class ByxContainer implements Container
{
    private final Map<String, Component> components = new ConcurrentHashMap<>();

    @Override
    public void addComponent(String id, Component component)
    {
        if (component == null)
            throw new ByxContainerException(Message.parameterNotNull("component"));
        components.put(id, component);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getObject(String id)
    {
        checkComponentExist(id);
        return (T) components.get(id).create();
    }

    @Override
    public <T> T getObject(Class<T> type)
    {
        List<Component> res = new ArrayList<>();
        components.forEach((id, c) ->
        {
            if (c.getType() != null && type.isAssignableFrom(c.getType()))
            {
                res.add(c);
            }
        });

        if (res.size() == 0)
            throw new ByxContainerException(Message.componentNotFoundWithType(type));
        else if (res.size() > 1)
            throw new ByxContainerException(Message.multiComponentsWithType(type));

        return type.cast(res.get(0).create());
    }

    @Override
    public Class<?> getType(String id)
    {
        checkComponentExist(id);
        return components.get(id).getType();
    }

    private void checkComponentExist(String id)
    {
        if (!components.containsKey(id))
            throw new ByxContainerException(Message.componentNotFoundWithId(id));
    }
}
