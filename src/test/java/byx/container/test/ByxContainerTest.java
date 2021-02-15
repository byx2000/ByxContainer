package byx.container.test;

import byx.container.ByxContainer;
import byx.container.Container;
import byx.container.exception.ByxContainerException;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static byx.container.component.Component.*;

public class ByxContainerTest
{
    @Test
    public void test()
    {
        Container container = new ByxContainer();
        container.addComponent("c1", constructor(String.class, value("hello")));
        container.addComponent("c2", staticFactory(List.class, "of", value(1), value(2), value(3)));
        assertEquals(String.class, container.getType("c1"));
        assertEquals(List.class, container.getType("c2"));
        assertEquals("hello", container.getObject("c1"));
        assertEquals(List.of(1, 2, 3), container.getObject("c2"));

        assertThrows(ByxContainerException.class,
                () -> container.addComponent("c3", null));
        assertThrows(ByxContainerException.class,
                () -> container.getObject("c3"));
        assertThrows(ByxContainerException.class,
                () -> container.getType("c3"));
    }
}
