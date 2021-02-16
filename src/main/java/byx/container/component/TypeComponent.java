package byx.container.component;

import byx.container.Container;

/**
 * 获取容器中指定类型的组件
 */
public class TypeComponent implements Component
{
    private final Container container;
    private final Class<?> type;

    public TypeComponent(Container container, Class<?> type)
    {
        this.container = container;
        this.type = type;
    }

    @Override
    public Object create()
    {
        return container.getObject(type);
    }

    @Override
    public Class<?> getType()
    {
        return type;
    }
}
