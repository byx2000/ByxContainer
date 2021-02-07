package byx.container.component;

import java.util.Arrays;

/**
 * 由函数产生的组件
 */
public class FunctionComponent implements Component
{
    private final Function function;
    private final Component[] params;

    public FunctionComponent(Function function, Component... params)
    {
        this.function = function;
        this.params = params;
    }

    @Override
    public Object create()
    {
        return function.call(Arrays.stream(params).map(Component::create).toArray());
    }
}
