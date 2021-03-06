package byx.container.test;

import byx.container.core.ByxContainer;
import byx.container.core.Container;
import byx.container.core.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static byx.container.core.Component.*;

public class TypeComponentTest {
    public static class A {
    }

    public static class B extends A {
    }

    @Test
    public void test() {
        Container container = new ByxContainer();
        container.addComponent("c1", value(123));
        container.addComponent("c2", value("hello"));
        container.addComponent("c3", constructor(B.class));

        Component c1 = type(container, Integer.class);
        int r1 = (int) c1.create();
        assertEquals(123, r1);
        assertNull(c1.getType());
        Component c2 = type(container, String.class);
        String r2 = (String) c2.create();
        assertEquals("hello", r2);
        assertNull(c2.getType());
        Component c3 = type(container, A.class);
        A r3 = (A) c3.create();
        assertTrue(r3 instanceof B);
        assertNull(c3.getType());
    }
}
