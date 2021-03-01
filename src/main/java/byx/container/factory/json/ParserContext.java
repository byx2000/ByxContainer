package byx.container.factory.json;

import byx.container.core.Container;
import byx.container.core.Component;
import byx.container.exception.ByxContainerException;
import byx.container.exception.Message;

import java.util.List;
import java.util.Map;

/**
 * 解析器上下文
 *
 * @author byx
 */
public class ParserContext {
    private final Container container;
    private final List<Map<String, Component>> scopes;
    private final Map<String, String> typeAlias;

    public ParserContext(Container container, List<Map<String, Component>> scopes, Map<String, String> typeAlias) {
        this.container = container;
        this.scopes = scopes;
        this.typeAlias = typeAlias;
    }

    /**
     * 获取容器
     */
    public Container getContainer() {
        return container;
    }

    public Component resolveComponentRef(String id) {
        for (int i = scopes.size() - 1; i >= 0; --i) {
            if (scopes.get(i).containsKey(id)) {
                return scopes.get(i).get(id);
            }
        }
        return null;
    }

    /**
     * 压入作用域
     */
    public void pushScope(Map<String, Component> scope) {
        scopes.add(scope);
    }

    /**
     * 弹出作用域
     */
    public void popScope() {
        scopes.remove(scopes.size() - 1);
    }

    /**
     * 根据全限定类名获取Class对象
     */
    public Class<?> getClass(String className) {
        try {
            if (typeAlias.containsKey(className)) {
                className = typeAlias.get(className);
            }
            return Class.forName(className);
        } catch (Exception e) {
            throw new ByxContainerException(Message.invalidClassName(className), e);
        }
    }
}
