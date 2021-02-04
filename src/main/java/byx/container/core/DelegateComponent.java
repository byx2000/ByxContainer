package byx.container.core;

/**
 * 代理组件：将组件的create方法转发到另一个组件的create方法。
 * 该组件用于解析配置文件时对局部组件进行延迟设置。
 */
public class DelegateComponent implements Component
{
    private Component component = Component.value(null);

    public void setComponent(Component component)
    {
        this.component = component;
    }

    @Override
    public Object create()
    {
        return component.create();
    }
}
