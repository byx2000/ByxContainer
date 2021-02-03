package byx.container.test;

import byx.container.core.Function;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Collections;

public class FunctionTest
{
    @Test
    public void test()
    {
        Function f1 = Function.constructor(String.class);
        assertEquals("", f1.call());
        Function f2 = Function.constructor(String.class);
        assertEquals("hello", f2.call("hello"));
        Function f3 = Function.staticFactory(Collections.class, "emptyList");
        assertEquals(Collections.emptyList(), f3.call());
        Function f4 = Function.staticFactory(String.class, "valueOf");
        assertEquals(String.valueOf(123), f4.call(123));
        Function f5 = Function.instanceFactory("static", "length");
        assertEquals("static".length(), f5.call());
        Function f6 = Function.instanceFactory("apple", "substring");
        assertEquals("apple".substring(1, 4), f6.call(1, 4));
    }
}
