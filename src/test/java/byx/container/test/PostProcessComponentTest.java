package byx.container.test;

import byx.container.component.Component;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static byx.container.component.Component.*;

public class PostProcessComponentTest
{
    @Test
    public void test()
    {
        Component c = constructor(StringBuilder.class).postProcess(obj ->
        {
            StringBuilder sb = (StringBuilder) obj;
            sb.append("hello");
            sb.append(" world");
        });

        StringBuilder sb = (StringBuilder) c.create();
        assertEquals("hello world", sb.toString());
        assertEquals(StringBuilder.class, c.getType());
    }
}
