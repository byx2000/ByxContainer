package byx.container.test;

import byx.container.core.Component;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.Set;
import static byx.container.core.Component.set;
import static byx.container.core.Component.value;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SetComponentTest
{
    @Test
    @SuppressWarnings("unchecked")
    public void test()
    {
        Component c1 = set();
        Set<?> s1 = (Set<?>) c1.create();
        assertEquals(Collections.EMPTY_SET, s1);
        Component c2 = set(value(123));
        Set<Integer> s2 = (Set<Integer>) c2.create();
        assertEquals(Set.of(123), s2);
        Component c3 = set(value(123), value(456), value(789));
        Set<Integer> s3 = (Set<Integer>) c3.create();
        assertEquals(Set.of(123, 456, 789), s3);
        Component c4 = set(value(123), value("hello"), value(456));
        Set<Object> s4 = (Set<Object>) c4.create();
        assertEquals(Set.of(123, "hello", 456), s4);
    }
}
