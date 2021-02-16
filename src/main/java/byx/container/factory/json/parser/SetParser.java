package byx.container.factory.json.parser;

import byx.container.component.Component;
import byx.container.factory.json.JsonElement;

import static byx.container.factory.json.parser.Parser.*;
import static byx.container.factory.json.ReservedKey.*;
import static byx.container.component.Component.*;

public class SetParser implements Parser
{
    @Override
    public Component parse(JsonElement element, ParserContext context)
    {
        Component[] components = parseComponentList(element.getElement(RESERVED_SET), context);
        return set(components);
    }
}
