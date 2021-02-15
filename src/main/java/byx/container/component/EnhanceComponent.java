package byx.container.component;

/**
 * 对组件创建的结果进行进一步增强
 */
public class EnhanceComponent implements Component
{
    private final Component component;
    private final Enhancer enhancer;

    public EnhanceComponent(Component component, Enhancer enhancer)
    {
        this.component = component;
        this.enhancer = enhancer;
    }

    @Override
    public Object create()
    {
        Object obj = component.create();
        enhancer.enhance(obj);
        return obj;
    }

    @Override
    public Class<?> getType()
    {
        return component.getType();
    }
}
