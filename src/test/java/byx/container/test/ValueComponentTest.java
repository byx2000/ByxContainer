package byx.container.test;

import byx.container.core.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static byx.container.core.Component.*;

public class ValueComponentTest {
    @Test
    public void test() {
        Component c1 = value(123);
        assertEquals(123, c1.create());
        assertEquals(Integer.class, c1.getType());
        Component c2 = value(3.14);
        assertEquals(3.14, c2.create());
        assertEquals(Double.class, c2.getType());
        Component c3 = value("hello");
        assertEquals("hello", c3.create());
        assertEquals(String.class, c3.getType());
        Component c4 = value(true);
        assertEquals(true, c4.create());
        assertEquals(Boolean.class, c4.getType());
        Component c5 = value(false);
        assertEquals(false, c5.create());
        assertEquals(Boolean.class, c5.getType());
        Component c6 = value(null);
        assertNull(c6.create());
        assertNull(c6.getType());
    }
}
