ByxContainer支持对容器中的对象进行AOP代理，需要引入ByxAOP依赖：

```xml
<repositories>
    <repository>
        <id>byx-maven-repo</id>
        <name>byx-maven-repo</name>
        <url>https://gitee.com/byx2000/maven-repo/raw/master/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>byx.aop</groupId>
        <artifactId>ByxAOP</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

关于ByxAOP的详细信息，请看[这里](https://github.com/byx2000/ByxAOP)。

假设有如下`User`类：

```java
public class User {
    private String  username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
```

我们为`User`类写一个增强类，这个类里面定义了对`User`中方法的增强方法：

```java
public class UserAdvice {
    @Before
    @WithName("setUsername")
    public String[] beforeSetUsername(String username) {
        return new String[]{username + " x"};
    }

    @After
    @WithName("getUsername")
    public String afterGetUsername(String retVal) {
        return retVal + " y";
    }
}
```

在`UserAdvice`中，我们为`setUsername`方法添加了前置增强，为`getUsername`方法添加了后置增强。

接着，我们在`container.json`中编写如下配置（假设`User`和`UserAdvice`都在`byx.test`包中）：

```json
{
    "components": {
        "user": {
            "class": "bux.test.User",
            "advice": {
                "class": "byx.test.UserAdvice"
            }
        }
    }
}
```

在Java代码中获取`User`类的对象：

```java
User user = container.getObject("user");
```

或：

```java
User user = container.getObject(User.class);
```

运行下面的代码：

```java
user.setUsername("byx");
System.out.println(user.getUsername());
```

控制台输出如下：

```
byx x y
```

正如我们所预料的，`user`对象被增强了。