package byx.container.core;

/**
 * IOC容器：管理着系统中所有的组件以及它们之间的依赖关系。
 * 应用程序初始化时，应该先定义好组件之间的依赖关系，然后将所有组件添加到IOC容器。
 * 当应用程序需要某个组件时，可以直接向容器获取。
 */
public interface Container
{
    /**
     * 将组件添加到容器
     * @param key 唯一标识这个组件的key
     * @param component 组件
     */
    void addComponent(String key, Component component);

    /**
     * 获取组件
     * @param key 该组件的key
     * @return 组件
     */
    Object getComponent(String key);
}
