package byx.container.factory.json.parser;

import byx.container.component.Component;
import byx.container.factory.json.JsonElement;

import static byx.container.component.Component.*;
import static byx.container.factory.json.ReservedKey.*;
import static byx.container.factory.json.parser.Parser.*;

public class ConstructorParser implements Parser
{
    @Override
    public Component parse(JsonElement element, ParserContext context)
    {
        String className = element.getElement(RESERVED_CLASS).getString();
        Component[] params = new Component[0];
        if (element.containsKey(RESERVED_PARAMETERS))
        {
            params = parseComponentList(element.getElement(RESERVED_PARAMETERS), context);
        }
        return constructor(context.getClass(className), params);
    }
}
