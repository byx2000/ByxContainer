package byx.container.core;

import java.util.HashSet;
import java.util.Set;

/**
 * 将多个组件的创建结果封装成集合
 */
public class SetComponent implements Component
{
    private final Component[] components;

    public SetComponent(Component[] components)
    {
        this.components = components;
    }

    @Override
    public Object create()
    {
        Set<Object> set = new HashSet<>();
        for (Component c : components)
        {
            set.add(c.create());
        }
        return set;
    }
}
