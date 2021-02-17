package byx.container.component;

/**
 * 对组件创建的结果进行进一步处理
 */
public class PostProcessComponent implements Component
{
    private final Component component;
    private final PostProcessor processor;

    public PostProcessComponent(Component component, PostProcessor processor)
    {
        this.component = component;
        this.processor = processor;
    }

    @Override
    public Object create()
    {
        Object obj = component.create();
        processor.process(obj);
        return obj;
    }

    @Override
    public Class<?> getType()
    {
        return component.getType();
    }
}
