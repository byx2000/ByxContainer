package byx.container.test;

import byx.container.component.Component;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static byx.container.component.Component.*;

public class MapperComponentTest
{
    @Test
    public void test()
    {
        Component c = constructor(String.class, value("123")).map(obj -> Integer.parseInt((String) obj));
        int i = (int) c.create();
        assertEquals(123, i);
        assertNull(c.getType());
    }
}
