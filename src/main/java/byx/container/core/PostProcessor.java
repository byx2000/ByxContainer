package byx.container.core;

/**
 * 后置处理器：对组件创建出来的对象进行进一步处理
 * 配合Component中的postProcessor一起使用
 *
 * @author byx
 */
public interface PostProcessor {
    /**
     * 对容器创建的对象进行处理
     *
     * @param obj 容器创建出的对象
     */
    void process(Object obj);
}
