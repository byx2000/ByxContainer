package byx.container.test;

import byx.container.core.Component;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static byx.container.core.Component.*;

public class StaticFactoryComponentTest {
    @Test
    public void test() {
        Component c1 = call(Collections.class, "emptyList");
        assertEquals(Collections.emptyList(), c1.create());
        assertEquals(List.class, c1.getType());
        Component c2 = call(List.class, "of", value(1), value(2), value(3));
        assertEquals(List.of(1, 2, 3), c2.create());
        assertEquals(List.class, c2.getType());
        Component c3 = call(String.class, "valueOf", value(888));
        assertEquals("888", c3.create());
        assertEquals(String.class, c3.getType());
    }
}
