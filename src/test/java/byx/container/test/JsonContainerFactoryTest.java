package byx.container.test;

import byx.container.core.Container;
import byx.container.core.ContainerFactory;
import byx.container.factory.JsonContainerFactory;
import org.junit.jupiter.api.Test;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
public class JsonContainerFactoryTest
{
    public static class Student
    {
        private int id;
        private String name;
        private List<Integer> scores;

        public Student()
        {
            id = -1;
            name = "unknown_name";
            scores = new ArrayList<>();
        }

        public Student(int id, String name, List<Integer> scores)
        {
            this.id = id;
            this.name = name;
            this.scores = scores;
        }

        public static Student createDefault()
        {
            return new Student();
        }

        public static Student create(int id, String name, List<Integer> scores)
        {
            return new Student(id, name, scores);
        }

        public void setIdAndName(int id, String name)
        {
            this.id = id;
            this.name = name;
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

        public List<Integer> getScores()
        {
            return scores;
        }

        public void setScores(List<Integer> scores)
        {
            this.scores = scores;
        }
    }

    public static class Student2 extends Student
    {
        private int age;
        boolean male;

        public int getAge()
        {
            return age;
        }

        public void setAge(int age)
        {
            this.age = age;
        }

        public boolean isMale()
        {
            return male;
        }

        public void setMale(boolean male)
        {
            this.male = male;
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

        String c1 = (String) container.getComponent("c1");
        assertEquals("", c1);
        Student c2 = (Student) container.getComponent("c2");
        assertEquals(-1, c2.getId());
        assertEquals("unknown_name", c2.getName());
        assertEquals(Collections.EMPTY_LIST, c2.getScores());
        String c3 = (String) container.getComponent("c3");
        assertEquals("hello", c3);
        Student c4 = (Student) container.getComponent("c4");
        assertEquals(1001, c4.getId());
        assertEquals("byx", c4.getName());
        assertEquals(List.of(88.5, 97.5, 90), c4.getScores());
        Student c5 = (Student) container.getComponent("c5");
        assertEquals(1002, c5.getId());
        assertEquals("XiaoMing", c5.getName());
        assertEquals(List.of(69.5, 87, 77), c5.getScores());
    }

    /**
     * 静态工厂
     */
    @Test
    public void test5()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test5.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        Container container = factory.create();

        List<?> c1 = (List<?>) container.getComponent("c1");
        assertEquals(Collections.emptyList(), c1);
        String c2 = (String) container.getComponent("c2");
        assertEquals("123", c2);
        Student c3 = (Student) container.getComponent("c3");
        assertEquals(-1, c3.getId());
        assertEquals("unknown_name", c3.getName());
        assertEquals(Collections.EMPTY_LIST, c3.getScores());
        Student c4 = (Student) container.getComponent("c4");
        assertEquals(1001, c4.getId());
        assertEquals("byx", c4.getName());
        assertEquals(List.of(88.5, 97.5, 90), c4.getScores());
        Student c5 = (Student) container.getComponent("c5");
        assertEquals(1002, c5.getId());
        assertEquals("XiaoMing", c5.getName());
        assertEquals(List.of(69.5, 87, 77), c5.getScores());
    }

    /**
     * 实例工厂
     */
    @Test
    public void test6()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test6.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        Container container = factory.create();

        int c1 = (int) container.getComponent("c1");
        assertEquals(5, c1);
        String c2 = (String) container.getComponent("c2");
        assertEquals("ppl", c2);
        String c3 = (String) container.getComponent("c3");
        assertEquals("unknown_name", c3);
        String c4 = (String) container.getComponent("c4");
        assertEquals("ell", c4);
        String c5 = (String) container.getComponent("c5");
        assertEquals("he", c5);
    }

    /**
     * 属性注入
     */
    @Test
    public void test7()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test7.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        Container container = factory.create();

        Student c1 = (Student) container.getComponent("c1");
        assertEquals(1001, c1.getId());
        assertEquals("byx", c1.getName());
        assertEquals(List.of(70, 80, 90), c1.getScores());
        Student2 c2 = (Student2) container.getComponent("c2");
        assertEquals(1002, c2.getId());
        assertEquals("XiaoMing", c2.getName());
        assertEquals(21, c2.getAge());
        assertTrue(c2.isMale());
        assertEquals(List.of(70, 60, 50), c2.getScores());
        Student c3 = (Student) container.getComponent("c3");
        assertEquals(1003, c3.getId());
        assertEquals("XiaoHong", c3.getName());
        assertEquals(List.of(10, 20, 30), c3.getScores());
        Student c4 = (Student) container.getComponent("c4");
        assertEquals(1004, c4.getId());
        assertEquals("XiaoHua", c4.getName());
        assertEquals(List.of(10, 20, 30), c4.getScores());
    }

    /**
     * setter注入
     */
    @Test
    public void test8()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test8.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        Container container = factory.create();

        Student c1 = (Student) container.getComponent("c1");
        assertEquals(1001, c1.getId());
        assertEquals("byx", c1.getName());
        assertEquals(List.of(90, 85, 80), c1.getScores());
        Student c2 = (Student) container.getComponent("c2");
        assertEquals(1002, c2.getId());
        assertEquals("XiaoMing", c2.getName());
        assertEquals(List.of(75, 80, 85), c2.getScores());
        Student c3 = (Student) container.getComponent("c3");
        assertEquals(1003, c3.getId());
        assertEquals("XiaoHua", c3.getName());
        assertEquals(List.of(50, 60, 70), c3.getScores());
        Student c4 = (Student) container.getComponent("c4");
        assertEquals(1004, c4.getId());
        assertEquals("XiaoJun", c4.getName());
        assertEquals(List.of(90, 95, 100), c4.getScores());
    }
}
