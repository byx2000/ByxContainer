package byx.container.test;

import byx.container.core.ByxContainer;
import byx.container.core.Container;
import byx.container.exception.*;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static byx.container.core.Component.*;

public class ComponentExceptionTest
{
    public static class User
    {
        private String username;
        private String password;

        public String getUsername()
        {
            return username;
        }

        public void setUsername(String username)
        {
            this.username = username;
        }

        public String getPassword()
        {
            return password;
        }

        public void setPassword(String password)
        {
            this.password = password;
        }
    }

    @Test
    public void test()
    {
        // constructor
        assertThrows(ByxContainerException.class,
                () -> constructor(String.class, value(123), value("hello")).create());
        assertThrows(ByxContainerException.class,
                () -> constructor(String.class, staticFactory(String.class, "create", value(123))).create());

        // staticFactory
        assertThrows(ByxContainerException.class,
                () -> staticFactory(String.class, "valueOf", value(123), value("hello")).create());
        assertThrows(ByxContainerException.class,
                () -> staticFactory(String.class, "create", value(123)).create());
        assertThrows(ByxContainerException.class,
                () -> staticFactory(String.class, "valueOf", constructor(Integer.class, value(3.14))).create());

        // instanceFactory
        assertThrows(ByxContainerException.class,
                () -> instanceFactory(value("static"), "length", value(123)).create());
        assertThrows(ByxContainerException.class,
                () -> instanceFactory(value("static"), "length1").create());
        assertThrows(ByxContainerException.class,
                () -> instanceFactory(value("static"), "length", constructor(Integer.class, value(3.14))).create());

        // reference
        Container container = new ByxContainer();
        assertThrows(ByxContainerException.class,
                () -> reference(container, "hello").create());

        // setProperty
        assertThrows(ByxContainerException.class,
                () -> constructor(User.class).setProperty("id", value(1001)).create());
        assertThrows(ByxContainerException.class,
                () -> constructor(User.class).setProperty("username", value(123)).create());
        assertThrows(ByxContainerException.class,
                () -> constructor(User.class, value("hello")).setProperty("id", value(1001)).create());
        assertThrows(ByxContainerException.class,
                () -> constructor(User.class).setProperty("password", staticFactory(String.class, "create")).create());

        // invokeSetter
        assertThrows(ByxContainerException.class,
                () -> constructor(User.class).invokeSetter("setId", value(1001)).create());
        assertThrows(ByxContainerException.class,
                () -> constructor(User.class).invokeSetter("setUsername", staticFactory(String.class, "create")).create());
        assertThrows(ByxContainerException.class,
                () -> constructor(User.class, value(123)).invokeSetter("setPassword", value("123456")).create());

        // list
        assertThrows(ByxContainerException.class,
                () -> list(constructor(User.class, value(123))).create());

        // set
        assertThrows(ByxContainerException.class,
                () -> list(staticFactory(User.class, "create")).create());

        // map
        assertThrows(ByxContainerException.class,
                () -> map(Map.of(value(123), constructor(User.class, value("hello")))).create());
    }
}
