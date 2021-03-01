package byx.container.test;

import byx.container.core.ByxContainer;
import byx.container.core.Component;
import byx.container.core.Container;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static byx.container.core.Component.*;

public class ReferenceComponentTest {
    @Test
    public void test() {
        Container container = new ByxContainer();
        container.addComponent("c1", constructor(String.class, value("hello")));
        container.addComponent("c2", value(1234));
        Component c1 = reference(container, "c1");
        assertEquals("hello", c1.create());
        assertEquals(String.class, c1.getType());
        Component c2 = reference(container, "c2");
        assertEquals(1234, c2.create());
        assertEquals(Integer.class, c2.getType());
    }
}
