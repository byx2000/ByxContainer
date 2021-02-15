package byx.container.test;

import byx.container.Container;
import byx.container.exception.ByxContainerException;
import byx.container.factory.ContainerFactory;
import byx.container.factory.JsonContainerFactory;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class JsonContainerFactoryExceptionTest
{
    /**
     * 根元素缺少components键
     */
    @Test
    public void test1()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test1.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ByxContainerException.class, factory::create);
    }

    /**
     * 静态工厂定义缺少method键
     */
    @Test
    public void test2()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test2.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ByxContainerException.class, factory::create);
    }

    /**
     * 实例工厂定义缺少method键
     */
    @Test
    public void test3()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test3.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ByxContainerException.class, factory::create);
    }

    /**
     * 条件注入定义缺少then键
     */
    @Test
    public void test4()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test4.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ByxContainerException.class, factory::create);
    }

    /**
     * typeAlias不是对象
     */
    @Test
    public void test5()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test5.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ByxContainerException.class, factory::create);
    }

    /**
     * components不是对象
     */
    @Test
    public void test6()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test6.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ByxContainerException.class, factory::create);
    }

    /**
     * 不正确的类名
     */
    @Test
    public void test7()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test7.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ByxContainerException.class, factory::create);
    }

    /**
     * 不正确的Mapper类型
     */
    @Test
    public void test8()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test8.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ByxContainerException.class, factory::create);
    }

    /**
     * 不正确的Component类型
     */
    @Test
    public void test9()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test9.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ByxContainerException.class, factory::create);
    }

    /**
     * 无法识别的组件类型
     */
    @Test
    public void test10()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test10.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ByxContainerException.class, factory::create);
    }

    /**
     * 不正确的list定义
     */
    @Test
    public void test11()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test11.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ByxContainerException.class, factory::create);
    }

    /**
     * 不正确的set定义
     */
    @Test
    public void test12()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test12.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ByxContainerException.class, factory::create);
    }

    /**
     * 不正确的map定义
     */
    @Test
    public void test13()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test13.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        assertThrows(ByxContainerException.class, factory::create);
    }

    /**
     * 找不到构造函数
     */
    @Test
    public void test14()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test14.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        Container container = factory.create();
        assertThrows(ByxContainerException.class,
                () -> container.getObject("c1"));
    }

    /**
     * 找不到静态工厂
     */
    @Test
    public void test15()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test15.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        Container container = factory.create();
        assertThrows(ByxContainerException.class,
                () -> container.getObject("c1"));
    }

    /**
     * 实例工厂的实例为null
     */
    @Test
    public void test16()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test16.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        Container container = factory.create();
        assertThrows(ByxContainerException.class,
                () -> container.getObject("c1"));
    }

    /**
     * 找不到实例工厂方法
     */
    @Test
    public void test17()
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("exception/test17.json");
        ContainerFactory factory = new JsonContainerFactory(inputStream);
        Container container = factory.create();
        assertThrows(ByxContainerException.class,
                () -> container.getObject("c1"));
    }
}
