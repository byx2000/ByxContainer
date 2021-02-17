package byx.container.factory.json.parser;

import byx.container.component.Component;
import byx.container.factory.json.JsonElement;
import static byx.container.component.Component.*;
import static byx.container.factory.json.ReservedKey.*;

public class TypeMatchParser implements Parser
{
    @Override
    public Component parse(JsonElement element, ParserContext context)
    {
        String typeName = element.getElement(RESERVED_TYPE).getString();
        return type(context.getContainer(), context.getClass(typeName));
    }
}
