# setter方法调用

ByxContainer支持在对象创建出来之后调用对象的setter方法。假设有`Student`类定义如下：

```java
public class Student
{
    private int id;
    private String name;
    private double score;

    public setId(int id)
    {
        this.id = id;
    }

    public setNameAndScore(String name, double score)
    {
        this.name = name;
        this.score = score;
    }

    // 其它方法...
}
```

可以使用下面的配置来声明对`Student`的setter方法的调用：

```json
{
    "components":
    {
        "s": 
        {
            "class": "byx.test.Student",
            "setters":
            {
                "setId": [1001],
                "setNameAndScore": ["byx", 97.5]
            }
        }
    }
}
```

与之等价的Java代码定义方式：

```java
Component s = constructor(Student.name)
        .invokeSetter("setId", value(1001))
        .invokeSetter("setNameAndScore", value("byx"), value(97.5));
```

与之等价的对象创建逻辑：

```java
Student s = new A();
s.setId(1001);
s.setNameAndScore("byx", 97.5);
```

## 注意事项

* setter调用必须结合对象创建声明（构造函数、静态工厂、实例工厂）一起使用
* `setter`包含所有需要调用的(setter方法名, 方法参数)键值对
* `setter`中的方法参数既可以是立即数，也可以嵌套其它组件的定义
* JavaBean的属性设置可以通过调用setter方法来实现，但是某些setter方法可以有多个参数（如上面的`setNameAndScore`），而JavaBean的属性setter方法只允许一个参数