package byx.container.factory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.Set;

/**
 * 将Fastjson中的对象适配成JsonElement的适配器
 */
public class JsonElementAdapterForFastjson implements JsonElement
{
    private final Object obj;

    public JsonElementAdapterForFastjson(Object obj)
    {
        this.obj = obj;
    }

    @Override
    public boolean isInteger()
    {
        return obj instanceof Integer;
    }

    @Override
    public boolean isDouble()
    {
        return obj instanceof BigDecimal;
    }

    @Override
    public boolean isString()
    {
        return obj instanceof String;
    }

    @Override
    public boolean isBoolean()
    {
        return obj instanceof Boolean;
    }

    @Override
    public boolean isNull()
    {
        return obj == null;
    }

    @Override
    public boolean isArray()
    {
        return obj instanceof JSONArray;
    }

    @Override
    public boolean isObject()
    {
        return obj instanceof JSONObject;
    }

    @Override
    public int getInteger()
    {
        return (int) obj;
    }

    @Override
    public double getDouble()
    {
        return ((BigDecimal)obj).doubleValue();
    }

    @Override
    public String getString()
    {
        return (String) obj;
    }

    @Override
    public boolean getBoolean()
    {
        return (boolean) obj;
    }

    @Override
    public boolean containsKey(String key)
    {
        return ((JSONObject)obj).containsKey(key);
    }

    @Override
    public JsonElement getArray()
    {
        return this;
    }

    @Override
    public JsonElement getObject()
    {
        return this;
    }

    @Override
    public JsonElement getElement(int index)
    {
        return new JsonElementAdapterForFastjson(((JSONArray)obj).get(index));
    }

    @Override
    public JsonElement getElement(String key)
    {
        return new JsonElementAdapterForFastjson(((JSONObject)obj).get(key));
    }

    @Override
    public int getLength()
    {
        return ((JSONArray)obj).size();
    }

    @Override
    public Set<String> keySet()
    {
        return ((JSONObject)obj).keySet();
    }
}
