package byx.container.component;

import byx.container.Container;

/**
 * 对容器中组件的引用
 */
public class ReferenceComponent implements Component
{
    private final Container container;
    private final String id;

    public ReferenceComponent(Container container, String id)
    {
        this.container = container;
        this.id = id;
    }

    @Override
    public Object create()
    {
        return container.getObject(id);
    }

    @Override
    public Class<?> getType()
    {
        return container.getType(id);
    }
}
