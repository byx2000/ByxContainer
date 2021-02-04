package byx.container.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IOC容器实现类
 */
public class ByxContainer implements Container
{
    private final Map<String, Component> components = new ConcurrentHashMap<>();

    @Override
    public void addComponent(String key, Component component)
    {
        if (component == null)
            throw new RuntimeException("Parameter \"component\" cannot be null.");
        components.put(key, component);
    }

    @Override
    public Object getComponent(String key)
    {
        if (!components.containsKey(key))
            throw new RuntimeException(String.format("There is no component with key \"%s\".", key));
        return components.get(key).create();
    }
}
