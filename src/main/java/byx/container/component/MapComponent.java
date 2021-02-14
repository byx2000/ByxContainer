package byx.container.component;

import java.util.HashMap;
import java.util.Map;

/**
 * 将多个组件的创建结果封装成map
 */
public class MapComponent implements Component
{
    private final Map<Component, Component> componentMap;

    public MapComponent(Map<Component, Component> componentMap)
    {
        this.componentMap = componentMap;
    }

    @Override
    public Object create()
    {
        Map<Object, Object> map = new HashMap<>();
        for (Component k : componentMap.keySet())
        {
            map.put(k.create(), componentMap.get(k).create());
        }
        return map;
    }

    @Override
    public Class<?> getType()
    {
        return Map.class;
    }
}
