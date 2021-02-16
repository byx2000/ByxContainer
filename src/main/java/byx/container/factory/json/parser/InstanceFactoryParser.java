package byx.container.factory.json.parser;

import byx.container.component.Component;
import byx.container.factory.json.JsonElement;

import static byx.container.factory.json.parser.Parser.*;
import static byx.container.factory.json.ReservedKey.*;
import static byx.container.component.Component.*;

public class InstanceFactoryParser implements Parser
{
    @Override
    public Component parse(JsonElement element, ParserContext context)
    {
        Component instance = componentParser.parse(element.getElement(RESERVED_INSTANCE), context);
        String method = element.getElement(RESERVED_METHOD).getString();
        Component[] params = new Component[0];
        if (element.containsKey(RESERVED_PARAMETERS))
        {
            params = parseComponentList(element.getElement(RESERVED_PARAMETERS), context);
        }
       return instanceFactory(instance, method, params);
    }
}
