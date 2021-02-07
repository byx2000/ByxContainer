package byx.container.test;

import byx.container.ByxContainer;
import byx.container.component.Component;
import byx.container.Container;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static byx.container.component.Component.*;

public class ReferenceComponentTest
{
    @Test
    public void test()
    {
        Container container = new ByxContainer();
        container.addComponent("c1", constructor(String.class, value("hello")));
        container.addComponent("c2", value(1234));
        Component c1 = reference(container, "c1");
        assertEquals("hello", c1.create());
        Component c2 = reference(container, "c2");
        assertEquals(1234, c2.create());
    }
}
