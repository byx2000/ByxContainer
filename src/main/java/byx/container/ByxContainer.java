package byx.container;

import byx.container.component.Component;
import byx.container.exception.ByxContainerException;
import byx.container.exception.Message;
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
    public Class<?> getType(String id)
    {
        checkComponentExist(id);
        return components.get(id).getType();
    }

    private void checkComponentExist(String id)
    {
        if (!components.containsKey(id))
            throw new ByxContainerException(Message.componentNotFound(id));
    }
}
