# ByxContainer——轻量级IOC/DI容器
ByxContainer是一个轻量级IOC/DI容器，具有以下特性：
* 使用JSON格式的配置文件
* 支持构造函数注入、静态工厂注入、实例工厂注入、属性注入、setter注入、条件注入
* 组件的延迟加载和单例组件
* 根据id注册、获取容器中的组件

## 配置文件
ByxContainer使用JSON作为配置文件的格式。你可以将配置文件命名为任何名字，放在任何你喜欢的路径下。

ByxContainer配置文件的基本框架如下：
```
{
    "typeAlias":
    {
        // 配置components中使用的类型别名
    },
    "components":
    {
        // 定义容器中的所有组件
    }
}
```

|键|类型|说明|是否必须|
|-|-|-|-|
|`typeAlias`|对象|`components`中使用的类型别名|否|
|`components`|对象|定义容器中的所有组件|是|

关于类型别名的使用，请参考[这里](#类型别名)。

## 组件
ByxContainer管理的基本单位是组件。组件代表了系统中的一个对象，并封装了该对象的创建过程。当我们向ByxContainer注册一个组件时，需要为组件指定一个唯一的字符串key，同时还要定义组件的创建过程和依赖关系。

ByxContainer的所有组件都定义在`components`中。组件以键值对的形式写在`components`对象中，键就是组件的id，值就是组件的创建过程和依赖关系的定义。

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
容器初始化完成后，需要使用注册组件时的id来获取某个特定组件：
```java
// 获取id为c1的组件
SomeType c1 = container.getComponent("c1");
```

## 组件的创建方式
在ByxContainer中，支持以下四种组件创建方式：
* 常数
* 构造函数(constructor)
* 静态工厂(static factory)
* 实例工厂(instance factor)
### 常数
ByxContainer支持JSON中的所有基本类型常数，包括整数、浮点数、字符串、布尔、null，它们在Java中对应的类型分别是：`int`、`double`、`String`、`boolean`、`null`。
```json
{
    "components":
    {
        "intValue": 123,
        "doubleValue": 3.14,
        "stringValue": "hello",
        "trueValue": true,
        "falseValue": false,
        "nullValue": null
    }
}
```
以上配置声明了几种不同类型的常数组件。
### 构造函数
配置构造函数创建方式时，需要指定全限定类名，如果要传递参数则指定参数数组。

|键|类型|说明|是否必须|
|-|-|-|-|
|`class`|字符串|全限定类名|是|
|`parameter`|数组|构造函数参数|否|

```json
{
    "components":
    {
        "a1":
        {
            "class": "byx.test.A"
        },
        "a2":
        {
            "class": "byx.test.A",
            "parameters": ["hello", 123]
        }
    }
}
```
以上配置等价于以下Java代码：
```java
A a1 = new A();
A a2 = new A("hello", 123);
```
注：`parameters`数组中的元素既可以是常数，也可以嵌套其它组件的定义：
```json
{
    "components":
    {
        "a3":
        {
            "class": "byx.test.A",
            "parameters": 
            [
                "hello", 
                123, 
                {"class": "byx.test.B"}
            ]
        }
    }
}
```
以上配置等价于以下Java代码：
```java
A a3 = new A("hello", 123, new B());
```
### 静态工厂
配置静态工厂创建方式时，需要指定工厂类的类名和工厂函数名，如果要传递参数则指定参数数组。

|键|类型|说明|是否必须|
|-|-|-|-|
|`factory`|字符串|工厂类全限定类名|是|
|`method`|字符串|工厂类静态方法名|是|
|`parameter`|数组|方法参数|否|

```json
{
    "components":
    {
        "a1":
        {
            "factory": "byx.test.Factory",
            "method": "createDefault"
        },
        "a2":
        {
            "factory": "byx.test.Factory",
            "method": "create",
            "parameters": ["hello", 123]
        }
    }
}
```
以上配置等价于以下Java代码：
```java
A a1 = Factory.createDefault();
A a2 = Factory.create("hello", 123);
```
### 实例工厂
配置实例工厂创建方式时，需要指定实例组件和工厂函数名，如果要传递参数则指定参数数组。

|键|类型|说明|是否必须|
|-|-|-|-|
|`instance`|对象或基本类型|实例组件定义|是|
|`method`|字符串|实例工厂方法名|是|
|`parameter`|数组|方法参数|否|

```json
{
    "components":
    {
        "a1":
        {
            "instance": {"class": "byx.test.B"},
            "method": "createDefault"
        },
        "a2":
        {
            "instance": {"class": "byx.test.B"},
            "method": "create",
            "parameters": ["hello", 123]
        }
    }
}
```
以上配置等价于以下Java代码：
```java
A a1 = new B().createDefault();
A a2 = new B().create("hello", 123);
```
## 组件的依赖声明
声明完组件的创建方式后，还可继续声明组件的依赖。ByxContainer支持以下两种依赖声明方式：
* 属性设置
* setter方法
### 属性设置
ByxContainer支持在组件创建完成之后对组件的属性进行设置。这里的属性等同于JavaBean的Property。

|键|类型|说明|是否必须|
|-|-|-|-|
|`properties`|对象|(属性名, 属性值)键值对|否|

```json
{
    "components":
    {
        "a": 
        {
            "class": "byx.test.A",
            "properties":
            {
                "id": 1001,
                "name": "byx",
                "score": 97.5
            }
        }
    }
}
```
以上配置等价于以下Java代码：
```java
A a = new A();
a.setId(1001);
a.setName("byx");
a.setScore(97.5);
```
注：`properties`中的属性值既可以是常数，也可以嵌套其它组件的定义：
```json
{
    "components":
    {
        "a": 
        {
            "class": "byx.test.A",
            "properties":
            {
                "id": 1001,
                "b": {"class": "byx.test.B"}
            }
        }
    }
}
```
以上配置等价于以下Java代码：
```java
A a = new A();
a.setId(1001);
a.setB(new B());
```
### setter方法
ByxContainer支持在组件创建完成之后调用组件的setter方法，并传递参数。

|键|类型|说明|是否必须|
|-|-|-|-|
|`setters`|对象|(setter方法名, 方法参数)键值对|否|

```json
{
    "components":
    {
        "a": 
        {
            "class": "byx.test.A",
            "setters":
            {
                "setId": [1001],
                "setNameAndScore": ["byx", 97.5]
            }
        }
    }
}
```
以上配置等价于以下Java代码：
```java
A a = new A();
a.setId(1001);
a.setNameAndScore("byx", 97.5);
```
## 其它组件定义
ByxContainer还支持以下特殊组件定义：
* 集合组件
* 引用组件
* 条件组件
### 集合组件
ByxContainer支持`List`、`Set`和`Map`这三种集合组件。

下面是这三种集合组件的定义：
```json
{
    "components":
    {
        "c1": {"list": [1, 2, 3]},
        "c2": {"set": [4, 5, 6]},
        "c3": 
        {
            "map":
            {
                "k1": 100,
                "k2": 200,
                "k3": 300
            }
        },
        "c4":
        {
            "map":
            [
                {"key": 400, "value": "v1"},
                {"key": 500, "value": "v2"},
                {"key": 600, "value": "v3"}
            ]
        }
    }
}
```
以上配置等价于以下Java代码：
```java
List<Object> c1 = List.of(1, 2, 3);
Set<Object> c2 = Set.of(4, 5, 6);
Map<Object> c3 = Map.of("k1", 100, "k2", 200, "k3", 300);
Map<Object> c4 = Map.of(400, "v1", 500, "v2", 600, "v3");
```
注：
* 集合组件的元素既可以是常数，也可以嵌套其它组件的定义
* `c3`和`c4`是`Map`组件的两种不同定义方式。第一种只支持`String`类型的key，第二种支持任意数据类型的key
### 引用组件
ByxContainer既支持对全局组件的引用，也支持对局部组件的引用。

|键|类型|说明|是否必须|
|-|-|-|-|
|`ref`|字符串|引用的组件key|是|

```json
{
    "components":
    {
        "a1": {"class": "byx.test.A"},
        "a2": {"ref": "a1"}
    }
}
```
以上配置等价于以下Java代码：
```java
A a1 = new A();
A a2 = a1;
```
注：关于局部组件，请参考[这里](#局部组件)。
### 条件组件
ByxContainer支持根据不同条件产生不同的组件定义。

|键|类型|说明|是否必须|
|-|-|-|-|
|`if`|对象或基本类型|作为条件的组件定义|是|
|`then`|对象或基本类型|条件为真时产生的组件|是|
|`else`|对象或基本类型|条件为假时产生的组件|是|

```json
{
    "components":
    {
        "flag": true,
        "c":
        {
            "if": {"ref": "flag"},
            "then": {"class": "com.byx.A"},
            "else": {"class": "com.byx.B"}
        }
    }
}
```
以上配置等价于以下Java代码：
```java
boolean flag = true;
Object c;
if (flag)
    c = new A();
else
    c = new B();
```
注：当`if`设置成一个组件定义时，容器将把该组件创建的对象解释成`boolean`值，如果该组件创建的对象不是`boolean`类型，则当成`false`处理。
## 高级特性
ByxContainer还支持以下高级特性：
* 局部组件
* 单例组件
* 自定义组件
* 自定义转换器
* 类型别名
### 局部组件
有时候，某些组件的创建过程中需要用到其它的组件，如下面这个例子：
```java
A a = new A("hello", "123", new B());
```
我们可以按照如下方式来配置：
```json
{
    "components":
    {
        "msg": "hello",
        "val": 123,
        "b": {"class": "byx.test.B"},
        "a":
        {
            "class": "byx.test.A",
            "parameters": [{"ref": "msg"}, {"ref": "val"}, {"ref": "b"}]
        }
    }
}
```
在`A`的构造函数中传递的三个参数分别属于三个不同的组件，但是这三个组件仅仅用于构建`A`对象，我们不希望这些临时组件占用容器中的一个key。当然，可以使用一种“就地初始化”的写法：
```json
{
    "components":
    {
        "a":
        {
            "class": "byx.test.A",
            "parameters": 
            [
                "hello", 
                123, 
                {"class": "byx.test.B"}
            ]
        }
    }
}
```
这种写法可以隐藏临时组件，但是当组件创建过程比较复杂的时候，这种写法会形成深层次的嵌套结构，导致可读性变差。而且，这种写法也不能很好地应对某个临时组件被多次使用的情况，例如：
```java
B b = new B();
A a = new A(b, b);
```
以上问题可以使用ByxContainer提供的局部组件来解决：
```json
{
    "components":
    {
        "a":
        {
            "class": "byx.test.A",
            "parameters": [{"ref": "msg"}, {"ref": "val"}, {"ref": "b"}],
            "locals":
            {
                "msg": "hello",
                "val": 123,
                "b": {"class": "byx.test.B"}
            }
        }
    }
}
```
在`a`组件定义的内部有一个`locals`块，里面包含了属于`a`的局部组件定义，这些局部组件不会占用容器的全局key，它只能在组件`a`的作用域内被引用。如果局部组件的key与全局组件的key相同，则局部组件将会覆盖全局组件。
### 单例组件
在默认情况下，ByxContainer中的所有组件都是单例的，这意味着组件最多只会被创建一次。当你多次获取同一个组件时，所得到的是同一个对象实例：
```java
Object c1 = container.getComponent("c1");
Object c2 = container.getComponent("c2");
// c1与c2指向同一个对象
```
如果想要改变这个默认行为，只需要在组件定义中加入如下配置：
```json
"singleton": false
```
### 自定义组件
ByxContainer支持用户定义自己的组件，并在配置文件中使用。

用户可以通过实现`byx.container.component.Component`接口来创建自己的组件：
```java
public interface Component
{
    Object create();
}
```
`Component`接口中的`create`方法封装了对象创建的细节：
```java
public class MyComponent implements Component
{
    private final String msg;
    private final int val;

    public MyComponent()
    {
        MyComponent("hello", 123);
    }

    public MyComponent(String msg, int val)
    {
        this.msg = msg;
        this.val = val;
    }

    @Override
    public Object create()
    {
        return new A(msg, val);
    }
}
```

在配置文件中使用自定义组件，需要指定自定义组件的全限定类名，如果想传递构造函数参数，则需指定参数列表。

|键|类型|说明|是否必须|
|-|-|-|-|
|`custom`|字符串|自定义组件的全限定类名|是|
|`parameters`|数组|传递给自定义组件的构造函数参数|否|

```json
{
    "components":
    {
        "a1":
        {
            "custom": "byx.test.MyComponent"
        },
        "a2":
        {
            "custom": "byx.test.MyComponent",
            "parameters": ["hi", 456]
        }
    }
}
```
以上配置等价于以下Java代码：
```java
A a1 = new MyComponent().create();
A a2 = new MyComponent("hi", 456).create();
```
### 自定义转换器
ByxContainer支持对组件创建出来的对象进行进一步转换。用户需要通过实现`byx.container.component.Mapper`接口来创建自定义转换器：
```java
public interface Mapper
{
    Object map(Object obj);
}
```
`Mapper`接口中的`map`方法封装了对组件创建结果的处理：
```java
public class MyMapper implements Mapper
{
    private final int val;

    public MyMapper()
    {
        MyMapper(1);
    }

    public MyMapper(int val)
    {
        this.val = val;
    }

    @Override
    public Object map(Object obj)
    {
        if (obj instanceof Integer)
            return ((int) obj) + val;
        return obj;
    }
}
```
在配置文件中使用自定义转换器，需要在组件定义中指定`mapper`属性：
* 当`mapper`属性为字符串时，该字符串表示自定义`mapper`的全限定类名，同时用默认构造函数来创建该`mapper`
* 当`mapper`属性为对象时，对象中必须包含`class`和`parameters`属性，其中`class`表示`mapper`的全限定类名，`parameters`表示创建`mapper`时向构造函数传递的参数
```json
{
    "components":
    {
        "c1":
        {
            "class": "java.lang.Integer",
            "parameters": [123],
            "mapper": "byx.test.MyMapper"
        },
        "c1":
        {
            "class": "java.lang.Integer",
            "parameters": [123],
            "mapper":
            {
                "class": "byx.test.MyMapper",
                "parameters": [100]
            }
        }
    }
}
```
以上配置等价于以下Java代码：
```java
int c1 = (int) new MyMapper().map(container.getComponent("c1"));
int c2 = (int) new MyMapper(100).map(container.getComponent("c2"));
```
### 类型别名
为了避免键入大量冗长的全限定类名，ByxContainer提供了类型别名功能，只需在容器定义最外层添加`typeAlias`配置：
```json
{
    "typeAlias":
    {
        "A": "byx.test.A",
        "B": "byx.test.B"
    },
    "components":
    {
        "a":
        {
            "class": "A",
            "parameters": ["hello", 123]
        },
        "b":
        {
            "class": "B"
        }
    }
}
```
以上配置等价于以下Java代码：
```java
A a = new A("hello", 123);
B b = new B();
```
注：`components`中所有出现类名的地方都可以使用`typeAlias`中定义的别名，包括`class`、`factory`、`custom`等等。