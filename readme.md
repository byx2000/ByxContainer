# ByxContainer——轻量级IOC容器
ByxContainer是一个轻量级IOC容器，具有以下特性：
* 使用Json格式的配置文件
* 支持构造函数注入、静态工厂注入、实例工厂注入、属性注入、setter注入、条件注入
* 支持自定义对象创建方式
* 对象的延迟加载和单例组件
* 根据id或类型获取容器中的对象

## 组件
从本质上说，ByxContainer是一个对象工厂，负责创建和管理所有已在容器中注册的对象。当我们需要一个对象时，可以直接向容器索取我们需要的对象，而无需自己创建这个对象。

为了实现这个效果，我们需要提前”告诉“ByxContainer：哪些对象需要被管理，以及这些对象该如何创建。

ByxContainer使用**组件**来管理一个对象的创建。组件代表了系统中的一个对象，并封装了该对象的创建过程。当我们向ByxContainer注册一个对象时，需要：

* 创建组件，这个过程称为**组件的定义**
* 为组件指定一个唯一的key，并加入ByxContainer

以上两个步骤统称为**配置ByxContainer**。

有两种方式来配置ByxContainer：

* 使用配置文件
* 使用Java代码

## ByxContainer的配置文件

ByxContainer使用Json作为配置文件的格式。你可以将配置文件命名为任何名字，放在任何你喜欢的路径下。

ByxContainer配置文件的基本框架如下：

```
{
    "typeAlias":
    {
        
    },
    "components":
    {
        
    }
}
```


* `typeAlias`用于配置类型别名
* `components`用于声明容器中的所有组件

ByxContainer的所有组件都定义在`components`中。组件以键值对的形式写在`components`对象中，键就是组件的id，值就是组件的定义。

```
{
    "components":
    {
        "c1": ... // 组件c1的定义
        "c2": ... // 组件c2的定义
        "c3": ... // 组件c3的定义
        ...
    }
}
```
## 加载与使用ByxContainer

在应用程序的初始化代码中，按照以下方式加载配置文件，并初始化容器：

```java
InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("配置文件路径");
ContainerFactory factory = new JsonContainerFactory(inputStream);
Container container = factory.create();
```

容器初始化完成后，有两种方法来获取某个已注册的对象：

* 通过注册时的id获取
    ```java
    // 获取id为c的对象
    SomeType c = container.getObject("c");
    ```
* 通过类型获取
    ```java
    // 获取容器中类型为SomeType的对象
    SomeType c = container.getObject(SomeType.class);
    ```

在以下三种情况时，会抛出`ByxContainerExceprion`异常：

* 指定id的对象不存在
* 指定类型的对象不存在
* 有多个指定类型的对象

## 对象创建

* [立即数](./doc/立即数.md)
* [构造函数](./doc/构造函数.md)
* [静态工厂](./doc/静态工厂.md)
* [实例工厂](./doc/实例工厂.md)

## 对象初始化

* [属性设置](./doc/属性设置.md)
* [调用setter方法](./doc/调用setter方法.md)

## 特殊组件声明

* [引用组件](./doc/引用组件.md)
* [局部组件](./doc/局部组件.md)
* [条件组件](./doc/条件组件.md)
* [自定义组件](./doc/自定义组件.md)

## 其它配置

* [类型别名](./doc/类型别名.md)
* [单例](./doc/单例.md)