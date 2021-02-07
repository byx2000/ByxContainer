package byx.container;

import byx.container.component.Component;

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
            throw new RuntimeException("Parameter \"component\" cannot be null.");
        components.put(id, component);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getComponent(String id)
    {
        if (!components.containsKey(id))
            throw new RuntimeException(String.format("There is no component with key \"%s\".", id));
        return (T) components.get(id).create();
    }
}
