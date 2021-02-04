package byx.container.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 将多个组件的创建结果封装成列表
 */
public class ListComponent implements Component
{
    private final Component[] components;

    public ListComponent(Component... components)
    {
        this.components = components;
    }

    @Override
    public Object create()
    {
        List<Object> list = new ArrayList<>();
        for (Component c : components)
        {
            list.add(c.create());
        }
        return list;
    }
}
