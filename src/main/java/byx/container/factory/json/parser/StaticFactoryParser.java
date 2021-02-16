package byx.container.factory.json.parser;

import byx.container.component.Component;
import byx.container.factory.json.JsonElement;

import static byx.container.factory.json.parser.Parser.*;
import static byx.container.factory.json.ReservedKey.*;
import static byx.container.component.Component.*;

public class StaticFactoryParser implements Parser
{
    @Override
    public Component parse(JsonElement element, ParserContext context)
    {
        String factory = element.getElement(RESERVED_FACTORY).getString();
        String method = element.getElement(RESERVED_METHOD).getString();
        Component[] params = new Component[0];
        if (element.containsKey(RESERVED_PARAMETERS))
        {
            params = parseComponentList(element.getElement(RESERVED_PARAMETERS), context);
        }
        return staticFactory(context.getClass(factory), method, params);
    }
}
