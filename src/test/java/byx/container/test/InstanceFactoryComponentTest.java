package byx.container.test;

import byx.container.core.Component;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static byx.container.core.Component.*;

public class InstanceFactoryComponentTest
{
    @Test
    public void test()
    {
        Component c1 = value("static").call("length");
        assertEquals(6, c1.create());
        assertEquals(int.class, c1.getType());
        Component c2 = value("apple").call("substring", value(1), value(4));
        assertEquals("apple".substring(1, 4), c2.create());
        assertEquals(String.class, c2.getType());
    }
}
