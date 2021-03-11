package byx.container.core;

/**
 * 单例组件
 *
 * @author byx
 */
public class SingletonComponent implements Component {
    private final Component component;
    private Object obj;

    public SingletonComponent(Component component) {
        this.component = component;
    }

    @Override
    public Object create() {
        if (obj == null) {
            obj = component.create();
        }
        return obj;
    }

    @Override
    public Class<?> getType() {
        return component.getType();
    }
}
