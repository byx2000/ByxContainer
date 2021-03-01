package byx.container.factory.json;

import java.util.Set;

/**
 * 对JSON读取操作的封装
 *
 * @author byx
 */
public interface JsonElement {
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

    int getLength();

    JsonElement getElement(int index);

    JsonElement getElement(String key);

    boolean containsKey(String key);

    Set<String> keySet();

    String getJsonString();

    default boolean isPrimitive() {
        return isInteger() || isDouble() || isString() || isBoolean() || isNull();
    }
}
