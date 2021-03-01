package byx.container.core;

/**
 * 容器工厂接口：用于创建并初始化容器
 *
 * @author byx
 */
public interface ContainerFactory {
    /**
     * 创建容器
     *
     * @return 容器实例
     */
    Container create();
}
