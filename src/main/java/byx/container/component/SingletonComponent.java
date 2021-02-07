package byx.container.component;

/**
 * 单例组件
 */
public class SingletonComponent implements Component
{
    private final Component component;
    private Object obj;

    public SingletonComponent(Component component)
    {
        this.component = component;
    }

    @Override
    public Object create()
    {
        if (obj == null) obj = component.create();
        return obj;
    }
}
