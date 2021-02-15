package byx.container.test;

import byx.container.component.Component;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static byx.container.component.Component.*;

public class MapComponentTest
{
    @Test
    @SuppressWarnings("unchecked")
    public void test()
    {
        Map<Component, Component> componentMap = new HashMap<>();
        componentMap.put(value("k1"), value(123));
        componentMap.put(value("k2"), value(456));
        componentMap.put(value("k3"), value(789));
        Component c = map(componentMap);
        Map<String, Integer> map = (Map<String, Integer>) c.create();
        assertEquals(123, map.get("k1"));
        assertEquals(456, map.get("k2"));
        assertEquals(789, map.get("k3"));
        assertEquals(Map.class, c.getType());
    }
}
