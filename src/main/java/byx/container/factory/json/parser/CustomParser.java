package byx.container.factory.json.parser;

import byx.container.component.Component;
import byx.container.factory.json.JsonElement;

import static byx.container.factory.json.parser.Parser.*;
import static byx.container.factory.json.ReservedKey.*;
import static byx.container.component.Component.*;

public class CustomParser implements Parser
{
    @Override
    public Component parse(JsonElement element, ParserContext context)
    {
        String className = element.getElement(RESERVED_CUSTOM).getString();
        Component[] params = new Component[0];
        if (element.containsKey(RESERVED_PARAMETERS))
        {
            params = parseComponentList(element.getElement(RESERVED_PARAMETERS), context);
        }
        return instanceFactory(
                constructor(
                        context.getComponent(className),
                        params)
                        .singleton(),
                "create");
    }
}
