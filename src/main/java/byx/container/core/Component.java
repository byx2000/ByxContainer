package byx.container.core;

/**
 * 组件：能够从IOC容器中获取的一个对象。
 * 所有的组件都被IOC容器管理，每个组件都封装了自己与其他组件的依赖关系。
 * 换句话说，每个组件都知道如何创建自己，这些信息封装在create方法中。
 * 每次从IOC容器中获取某个组件时，该组件的create方法将被调用。
 */
public interface Component
{
    /**
     * 创建组件
     * @return 组件对象
     */
    Object create();
}
