package byx.container.component;

/**
 * 对一个Component创建的结果进行转换
 */
public class MapperComponent implements Component
{
    private final Component component;
    private final Mapper mapper;

    public MapperComponent(Component component, Mapper mapper)
    {
        this.component = component;
        this.mapper = mapper;
    }

    @Override
    public Object create()
    {
        return mapper.map(component.create());
    }

    @Override
    public Class<?> getType()
    {
        return null;
    }
}
