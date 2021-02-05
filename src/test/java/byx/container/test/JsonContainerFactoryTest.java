package byx.container.test;

import byx.container.core.Container;
import byx.container.core.ContainerFactory;
import byx.container.factory.JsonContainerFactory;
import org.junit.jupiter.api.Test;
import java.io.InputStream;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
public class JsonContainerFactoryTest
{
    public static class Student
    {
        private int id;
        private String name;
        private double score;

        public Student()
        {
            id = -1;
            name = "unknown_name";
            score = 0.0;
        }

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String username)
        {
            this.name = username;
        }

        public double getScore()
        {
            return score;
        }

        public void setScore(double score)
        {
            this.score = score;
        }
    }

    /**
     * 常数
     */
    @Test
    public void test1()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test1.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        Container container = factory.create();

        int c1 = (int) container.getComponent("intValue");
        assertEquals(123, c1);
        double c2 = (double) container.getComponent("doubleValue");
        assertEquals(3.14, c2);
        String c3 = (String) container.getComponent("stringValue");
        assertEquals("hello", c3);
        boolean c4 = (boolean) container.getComponent("trueValue");
        assertTrue(c4);
        boolean c5 = (boolean) container.getComponent("falseValue");
        assertFalse(c5);
        Object c6 = container.getComponent("nullValue");
        assertNull(c6);
    }

    /**
     * 集合类型
     */
    @Test
    public void test2()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test2.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        Container container = factory.create();

        List<?> c1 = (List<?>) container.getComponent("c1");
        assertTrue(c1.isEmpty());
        List<Integer> c2 = (List<Integer>) container.getComponent("c2");
        assertEquals(List.of(100), c2);
        List<Integer> c3 = (List<Integer>) container.getComponent("c3");
        assertEquals(List.of(123, 456, 789), c3);
        List<Object> c4 = (List<Object>) container.getComponent("c4");
        assertEquals(List.of(123, "hello", 456), c4);
    }

    /**
     * 引用
     */
    @Test
    public void test3()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test3.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        Container container = factory.create();

        String c1 = (String) container.getComponent("c1");
        assertEquals("hello", c1);
        int c2 = (int) container.getComponent("c2");
        assertEquals(123, c2);
        String c3 = (String) container.getComponent("c3");
        assertEquals("hello", c3);
        int c4 = (int) container.getComponent("c4");
        assertEquals(123, c4);
        String c5 = (String) container.getComponent("c5");
        assertEquals("hello", c5);
        List<String> c6 = (List<String>) container.getComponent("c6");
        assertEquals(List.of("hello", "powerful", "byx", "container", "!"), c6);
        String c7 = (String) container.getComponent("c7");
        assertEquals("hi", c7);
        String c8 = (String) container.getComponent("c8");
        assertEquals("thank you", c8);
    }

    /**
     * 构造函数
     */
    @Test
    public void test4()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test4.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        Container container = factory.create();

        Student c1 = (Student) container.getComponent("c1");
        assertEquals(-1, c1.getId());
        assertEquals("unknown_name", c1.getName());
        assertEquals(0.0, c1.getScore());

        String c2 = (String) container.getComponent("c2");
        assertEquals("", c2);
    }
}
