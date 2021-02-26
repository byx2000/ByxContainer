package byx.container.core;

/**
 * 后置处理器：对组件创建出来的对象进行进一步处理
 * 配合Component中的postProcessor一起使用
 */
public interface PostProcessor
{
    void process(Object obj);
}
