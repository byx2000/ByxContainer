package byx.container.component;

/**
 * 包装一个常数的组件
 */
public class ValueComponent implements Component
{
    private final Object value;

    public ValueComponent(Object value)
    {
        this.value = value;
    }

    @Override
    public Object create()
    {
        return value;
    }
}
