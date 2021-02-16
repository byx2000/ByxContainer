package byx.container.factory.json.parser;

import byx.container.component.Component;
import byx.container.factory.json.JsonElement;

import java.util.HashMap;
import java.util.Map;

import static byx.container.factory.json.ReservedKey.*;
import static byx.container.component.Component.*;

public class MapParser implements Parser
{
    @Override
    public Component parse(JsonElement element, ParserContext context)
    {
        JsonElement mapElem = element.getElement(RESERVED_MAP);
        Map<Component, Component> componentMap = new HashMap<>();
        if (mapElem.isObject())
        {
            for (String key : mapElem.keySet())
            {
                componentMap.put(
                        value(key),
                        componentParser.parse(mapElem.getElement(key), context));
            }
        }
        else
        {
            for (int i = 0; i < mapElem.getLength(); ++i)
            {
                JsonElement item = mapElem.getElement(i);
                componentMap.put(
                        componentParser.parse(item.getElement(RESERVED_KEY), context),
                        componentParser.parse(item.getElement(RESERVED_VALUE), context));
            }
        }
        return map(componentMap);
    }
}
