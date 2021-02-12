package byx.container.test;

import byx.container.component.Component;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static byx.container.component.Component.*;

public class InstanceFactoryComponentTest
{
    @Test
    public void test()
    {
        Component c1 = instanceFactory(value("static"), "length");
        assertEquals(6, c1.create());
        Component c2 = instanceFactory(value("apple"), "substring", value(1), value(4));
        assertEquals("apple".substring(1, 4), c2.create());
    }
}
