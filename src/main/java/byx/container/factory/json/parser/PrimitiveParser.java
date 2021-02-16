package byx.container.factory.json.parser;

import byx.container.component.Component;
import byx.container.factory.json.JsonElement;

import static byx.container.component.Component.value;

public class PrimitiveParser implements Parser
{
    @Override
    public Component parse(JsonElement element, ParserContext context)
    {
        if (element.isInteger()) return value(element.getInteger());
        else if (element.isDouble()) return value(element.getDouble());
        else if (element.isString()) return value(element.getString());
        else if (element.isBoolean()) return value(element.getBoolean());
        else return value(null);
    }
}
