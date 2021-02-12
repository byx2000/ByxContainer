package byx.container.test;

import byx.container.component.Component;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static byx.container.component.Component.*;

public class ConstructorComponentTest
{
    @Test
    public void test()
    {
        Component c1 = constructor(String.class);
        assertEquals("", c1.create());
        Component c2 = constructor(Integer.class, value(123));
        assertEquals(123, c2.create());
    }
}
