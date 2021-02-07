package byx.container.test;

import byx.container.component.Component;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static byx.container.component.Component.*;

public class FunctionComponentTest
{
    @Test
    public void test()
    {
        Component c1 = constructor(String.class);
        assertEquals("", c1.create());
        Component c2 = constructor(String.class, value("hello"));
        assertEquals("hello", c2.create());
        Component c3 = staticFactory(Collections.class, "emptyList");
        assertEquals(Collections.emptyList(), c3.create());
        Component c4 = staticFactory(String.class, "valueOf", value(123));
        assertEquals(String.valueOf(123), c4.create());
        Component c5 = instanceFactory(value("static"), "length");
        assertEquals(6, c5.create());
        Component c6 = instanceFactory(value("apple"), "substring", value(1), value(4));
        assertEquals("apple".substring(1, 4), c6.create());
    }
}
