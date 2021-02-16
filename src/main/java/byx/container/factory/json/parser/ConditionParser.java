package byx.container.factory.json.parser;

import byx.container.component.Component;
import byx.container.factory.json.JsonElement;

import static byx.container.factory.json.ReservedKey.*;
import static byx.container.component.Component.*;

public class ConditionParser implements Parser
{
    @Override
    public Component parse(JsonElement element, ParserContext context)
    {
        Component predicate = componentParser.parse(element.getElement(RESERVED_IF), context);
        Component c1 = componentParser.parse(element.getElement(RESERVED_THEN), context);
        Component c2 = componentParser.parse(element.getElement(RESERVED_ELSE), context);
        return condition(predicate, c1, c2);
    }
}
