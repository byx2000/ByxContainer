package byx.container.factory.json.parser;

import byx.container.Container;
import byx.container.component.Component;
import byx.container.exception.ByxContainerException;
import byx.container.exception.Message;

import java.util.List;
import java.util.Map;

public class ParserContext
{
    private final Container container;
    private final List<Map<String, Component>> scopes;
    private final Map<String, String> typeAlias;

    public ParserContext(Container container, List<Map<String, Component>> scopes, Map<String, String> typeAlias)
    {
        this.container = container;
        this.scopes = scopes;
        this.typeAlias = typeAlias;
    }

    public Container getContainer()
    {
        return container;
    }

    public Component resolveComponentRef(String id)
    {
        for (int i = scopes.size() - 1; i >= 0; --i)
        {
            if (scopes.get(i).containsKey(id))
            {
                return scopes.get(i).get(id);
            }
        }
        return null;
    }

    public void pushScope(Map<String, Component> scope)
    {
        scopes.add(scope);
    }

    public void popScope()
    {
        scopes.remove(scopes.size() - 1);
    }

    public Class<?> getClass(String className)
    {
        try
        {
            if (typeAlias.containsKey(className))
                className = typeAlias.get(className);
            return Class.forName(className);
        }
        catch (Exception e)
        {
            throw new ByxContainerException(Message.invalidClassName(className), e);
        }
    }

    public Class<?> getComponent(String componentClassName)
    {
        Class<?> type = getClass(componentClassName);
        if (!Component.class.isAssignableFrom(type))
            throw new ByxContainerException(Message.invalidComponentClassName(componentClassName));
        return type;
    }
}
