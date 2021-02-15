package byx.container.test;

import byx.container.component.Component;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static byx.container.component.Component.*;

public class ListComponentTest
{
    @Test
    @SuppressWarnings("unchecked")
    public void test()
    {
        Component c1 = list();
        List<?> l1 = (List<?>) c1.create();
        assertEquals(Collections.EMPTY_LIST, l1);
        assertEquals(List.class, c1.getType());
        Component c2 = list(value(1));
        List<Integer> l2 = (List<Integer>) c2.create();
        assertEquals(List.of(1), l2);
        assertEquals(List.class, c2.getType());
        Component c3 = list(value("aaa"), value("bbb"), value("ccc"));
        List<String> l3 = (List<String>) c3.create();
        assertEquals(List.of("aaa", "bbb", "ccc"), l3);
        assertEquals(List.class, c3.getType());
        Component c4 = list(value(1), value(2), value("hello"));
        List<Object> l4 = (List<Object>) c4.create();
        assertEquals(List.of(1, 2, "hello"), l4);
        assertEquals(List.class, c4.getType());
    }
}
