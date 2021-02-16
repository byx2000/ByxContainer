package byx.container.factory.json;

import byx.container.ByxContainer;
import byx.container.Container;
import byx.container.component.Component;
import byx.container.exception.ByxContainerException;
import byx.container.exception.Message;
import byx.container.factory.ContainerFactory;
import byx.container.factory.json.parser.Parser;
import byx.container.factory.json.parser.ParserContext;
import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static byx.container.factory.json.ReservedKey.*;

/**
 * 从Json格式的配置文件创建容器
 */
public class JsonContainerFactory implements ContainerFactory
{
    private final String json;

    /**
     * 从文件流创建JsonContainerFactory
     * @param inputStream 文件流
     */
    public JsonContainerFactory(InputStream inputStream)
    {
        if (inputStream == null)
            throw new ByxContainerException(Message.parameterNotNull("inputStream"));
        this.json = readJsonFile(inputStream);
    }

    /**
     * 读取json文件
     */
    private static String readJsonFile(InputStream inputStream)
    {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)))
        {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                json.append(line);
            }
            return json.toString();
        }
        catch (Exception e)
        {
            throw new ByxContainerException("Error occurs when reading json file.", e);
        }
    }

    /**
     * 解析容器
     */
    private Container parseContainer(JsonElement element)
    {
        // 处理typeAlias
        Map<String, String> typeAlias = new HashMap<>();
        if (element.containsKey(RESERVED_TYPE_ALIAS))
        {
            JsonElement typeAliasElem = element.getElement(RESERVED_TYPE_ALIAS);
            for (String alias : typeAliasElem.keySet())
            {
                typeAlias.put(alias, typeAliasElem.getElement(alias).getString());
            }
        }

        Container container = new ByxContainer();
        ParserContext context = new ParserContext(container, new ArrayList<>(), typeAlias);
        JsonElement components = element.getElement(RESERVED_COMPONENTS);
        for (String key : components.keySet())
        {
            Component c = Parser.componentParser.parse(components.getElement(key), context);
            container.addComponent(key, c);
        }
        return container;
    }

    @Override
    public Container create()
    {
        return parseContainer(new JsonElementAdapterForFastjson(JSON.parse(json)));
    }
}
