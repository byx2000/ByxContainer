package byx.container.test;

import byx.container.core.ByxContainer;
import byx.container.core.Container;
import byx.container.exception.ByxContainerException;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static byx.container.core.Component.*;

public class ByxContainerTest
{
    public static class A {}
    public static class B extends A {}
    public interface C {}
    public static class D implements C {}

    @Test
    public void test()
    {
        Container container = new ByxContainer();
        container.addComponent("c1", constructor(String.class, value("hello")));
        container.addComponent("c2", call(List.class, "of", value(1), value(2), value(3)));
        container.addComponent("c3", constructor(Integer.class, value(123)));
        container.addComponent("c4", constructor(Integer.class, value(456)));
        container.addComponent("c5", value(new B()));
        container.addComponent("c6", constructor(D.class));
        assertEquals(String.class, container.getType("c1"));
        assertEquals(List.class, container.getType("c2"));
        assertEquals("hello", container.getObject("c1"));
        assertEquals(List.of(1, 2, 3), container.getObject("c2"));
        assertEquals(123, (int) container.getObject("c3"));
        assertEquals(Integer.class, container.getType("c3"));
        assertEquals(456, (int) container.getObject("c4"));
        assertEquals(Integer.class, container.getType("c4"));
        assertEquals(B.class, container.getType("c5"));
        assertEquals(D.class, container.getType("c6"));

        String s = container.getObject(String.class);
        assertEquals("hello", s);
        List<?> list = container.getObject(List.class);
        assertEquals(List.of(1, 2, 3), list);
        A a = container.getObject(A.class);
        assertTrue(a instanceof B);
        B b = container.getObject(B.class);
        C c = container.getObject(C.class);
        assertTrue(c instanceof D);
        D d = container.getObject(D.class);

        assertThrows(ByxContainerException.class,
                () -> container.addComponent("c100", null));
        assertThrows(ByxContainerException.class,
                () -> container.getObject("c101"));
        assertThrows(ByxContainerException.class,
                () -> container.getType("c101"));

        assertThrows(ByxContainerException.class,
                () -> container.getObject(Integer.class));
        assertThrows(ByxContainerException.class,
                () -> container.getObject(Double.class));
    }
}
