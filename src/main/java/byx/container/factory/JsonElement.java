package byx.container.factory;

import java.util.Set;

public interface JsonElement
{
    boolean isInteger();
    boolean isDouble();
    boolean isString();
    boolean isBoolean();
    boolean isNull();
    boolean isArray();
    boolean isObject();
    int getInteger();
    double getDouble();
    String getString();
    boolean getBoolean();
    boolean containsKey(String key);
    JsonElement getArray();
    JsonElement getObject();
    JsonElement getElement(int index);
    JsonElement getElement(String key);
    int getLength();
    Set<String> keySet();

    default boolean isPrimitive()
    {
        return isInteger() || isDouble() || isString() || isBoolean() || isNull();
    }
}
