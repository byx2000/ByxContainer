package byx.container.core;

/**
 * IOC容器：对象的管理者
 * 组件是IOC容器管理的基本单位
 * IOC容器在需要时会通过调用组件的create方法来创建对象
 * 当应用程序需要某个对象时，可以直接向容器获取
 * 可以通过id获取，也可通过类型获取
 * 在使用IOC容器前，需要先注册组件
 * @see Component
 */
public interface Container
{
    /**
     * 将组件注册到容器
     * @param id 唯一标识这个组件的key
     * @param component 组件
     */
    void addComponent(String id, Component component);

    /**
     * 根据id获取对象
     * @param id 组件id
     * @param <T> 返回值类型
     * @return 指定id的组件创建的对象
     */
    <T> T getObject(String id);

    /**
     * 根据类型获取对象
     * @param type 对象类型
     * @param <T> 对象类型
     * @return 指定类型的对象
     */
    <T> T getObject(Class<T> type);

    /**
     * 获取组件类型
     * @param id 组件id
     * @return 指定id的组件的类型
     */
    Class<?> getType(String id);
}
