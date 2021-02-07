package byx.container.test;

import byx.container.component.Component;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static byx.container.component.Component.*;

public class ValueComponentTest
{
    @Test
    public void test()
    {
        Component c1 = value(123);
        assertEquals(123, c1.create());
        Component c2 = value(3.14);
        assertEquals(3.14, c2.create());
        Component c3 = value("hello");
        assertEquals("hello", c3.create());
        Component c4 = value(true);
        assertEquals(true, c4.create());
        Component c5 = value(false);
        assertEquals(false, c5.create());
        Component c6 = value(null);
        assertNull(c6.create());
    }
}
