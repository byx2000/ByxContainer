package byx.container.test;

import byx.container.core.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static byx.container.core.Component.*;

public class InvokeSetterTest {
    public static class User {
        private Integer id;
        private String username;
        private String password;

        public void setId(Integer id) {
            this.id = id;
        }

        public void setUsernameAndPassword(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public Integer getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    @Test
    public void test() {
        Component c = constructor(User.class).invokeSetter("setId", value(1001)).invokeSetter("setUsernameAndPassword", value("byx"), value("123456"));
        User u = (User) c.create();
        assertEquals(1001, u.getId());
        assertEquals("byx", u.getUsername());
        assertEquals("123456", u.getPassword());
        assertEquals(User.class, c.getType());

        Component builder = constructor(StringBuilder.class).invokeSetter("append", value("hello")).invokeSetter("append", value(" world"));
        Component str = builder.call("toString");
        assertEquals("hello world", str.create());
        assertEquals(StringBuilder.class, builder.getType());
        assertEquals(String.class, str.getType());
    }
}
