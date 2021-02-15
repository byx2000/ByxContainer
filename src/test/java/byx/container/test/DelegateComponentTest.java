package byx.container.test;

import byx.container.component.DelegateComponent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static byx.container.component.Component.*;

public class DelegateComponentTest
{
    @Test
    public void test()
    {
        DelegateComponent c = new DelegateComponent();
        assertNull(c.create());
        c.setComponent(constructor(String.class, value("hello")));
        assertEquals("hello", c.create());
        assertEquals(String.class, c.getType());
    }
}
