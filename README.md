# ByxContainer——轻量级IOC容器

ByxContainer是一个轻量级IOC容器，具有以下特性：

* 使用Json格式的配置文件，抛弃复杂冗长的xml语法
* 支持构造函数注入、静态工厂注入、实例工厂注入、JavaBean属性注入、setter注入、条件注入
* 支持灵活的对象创建方式的配置
* 支持循环依赖
* 支持对象的延迟加载和单例


## 在项目中引入ByxContainer

1. 添加maven仓库地址

    ```xml
    <repositories>
        <repository>
            <id>byx-maven-repo</id>
            <name>byx-maven-repo</name>
            <url>https://gitee.com/byx2000/maven-repo/raw/master/</url>
        </repository>
    </repositories>
    ```

2. 添加maven依赖

    ```xml
    <dependencies>
        <dependency>
            <groupId>byx.container</groupId>
            <artifactId>ByxContainer</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>
    ```

## API文档

[API文档](http://byx2000.gitee.io/javadoc/ByxContainer-1.0.0-javadoc/)

## 使用示例

下面通过一个简单的使用示例来快速了解ByxContainer的使用。

1. 在`example`包下新建一个`Student`类：

    ```java
    package example;

    public class Student
    {
        private int id;
        private String name;
        private int age;
        private double score;

        public Student() {}

        public Student(int id, String name)
        {
            this.id = id;
            this.name = name;
        }

        public void setId(int id) { this.id = id; }
        public int getId() { return id; }
        public void setName(String name) { this.name = name; }
        public String getName() { return name; }
        public void setAge(int age) { this.age = age; }
        public int getAge() { return age; }
        public void setScore(double score) { this.score = score; }
        public double getScore() { return score; }

        @Override
        public String toString()
        {
            return "Student{" + "id=" + id + ", name='" + name + '\'' + ", age=" + age + ", score=" + score + '}';
        }
    }
    ```

2. 在`resources`目录下新建一个`container.json`，并写入如下内容：

    ```json
    {
        "$schema": "http://byx2000.gitee.io/byxcontainer/schema/schema.json",
        "components": {
            "message": "hello world!",
            "student": {
                "class": "example.Student",
                "parameters": [1001, "XiaoMing"],
                "properties": {
                    "age": 17,
                    "score": 97.5
                }
            }
        }
    }
    ```

    在`container.json`中，我们向容器注册了一个`String`和一个`Student`，id分别为`message`和`student`。

3. 新建一个测试类`Test`：

    ```java
    public class Test
    {
        public static void main(String[] args)
        {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("container.json");
            ContainerFactory factory = new JsonContainerFactory(inputStream);
            Container container = factory.create();

            String message = container.getObject("message");
            System.out.println(message);
            Student student = container.getObject(Student.class);
            System.out.println(student);
        }
    }
    ```

4. 运行测试类，控制台输出如下：

    ```
    hello world!
    Student{id=1001, name='XiaoMing', age=17, score=97.5}
    ```

## 使用说明

ByxContainer使用**组件**管理IOC容器中的对象，一个组件封装了一个对象的创建方式。通过对组件的配置，我们就可以对对象的创建方式进行配置，从而创建各种复杂对象。下面列出了ByxContainer中所有组件配置的使用方法。

### 对象创建

* [立即数](./doc/立即数.md)
* [构造函数](./doc/构造函数.md)
* [静态工厂](./doc/静态工厂.md)
* [实例工厂](./doc/实例工厂.md)

### 对象初始化

* [属性设置](./doc/属性设置.md)
* [调用setter方法](./doc/调用setter方法.md)

### 特殊组件声明

* [引用组件](./doc/引用组件.md)
* [局部组件](./doc/局部组件.md)
* [条件组件](./doc/条件组件.md)
* [自定义组件](./doc/自定义组件.md)

### 其它配置

* [类型别名](./doc/类型别名.md)
* [单例](./doc/单例.md)