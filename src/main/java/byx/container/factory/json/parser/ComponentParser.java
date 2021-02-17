package byx.container.factory.json.parser;

import byx.container.component.Component;
import byx.container.exception.ByxContainerException;
import byx.container.exception.Message;
import byx.container.factory.json.JsonElement;

import static byx.container.factory.json.parser.Parser.*;

public class ComponentParser implements Parser
{
    @Override
    public Component parse(JsonElement element, ParserContext context)
    {
        if (element.isPrimitive()) return primitiveParser.parse(element, context);

        for (String key : parsers.keySet())
        {
            if (element.containsKey(key))
            {
                processLocals(element, context);
                Component c = parsers.get(key).parse(element, context);
                c = processProperties(element, context, c);
                c = processSetters(element, context, c);
                c = processSingleton(element, c);
                c = processPostProcessor(element, context, c);
                context.popScope();
                return c;
            }
        }

        throw new ByxContainerException(Message.unknownComponentType(element.getJsonString()));
    }
}
