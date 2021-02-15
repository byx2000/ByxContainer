package byx.container.test;

import byx.container.component.Component;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static byx.container.component.Component.*;

public class ConditionComponentTest
{
    @Test
    public void test()
    {
        Component p1 = value(true);
        Component p2=  value(false);

        Component c1 = condition(p1, value(123), value("hello"));
        assertEquals(123, c1.create());
        assertNull(c1.getType());
        Component c2 = condition(p2, value(123), value("hello"));
        assertEquals("hello", c2.create());
        assertNull(c2.getType());
    }
}
