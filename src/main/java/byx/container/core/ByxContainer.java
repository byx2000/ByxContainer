package byx.container.core;

import byx.container.exception.ByxContainerException;
import byx.container.exception.Message;
import byx.container.util.ReflectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IOC容器实现类
 * 利用Map来管理组件
 *
 * @author byx
 */
public class ByxContainer implements CachedContainer {
    private final Map<String, Component> components = new ConcurrentHashMap<>();
    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    @Override
    public void addComponent(String id, Component component) {
        if (component == null) {
            throw new ByxContainerException(Message.parameterNotNull("component"));
        }
        components.put(id, component);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getObject(String id) {
        checkComponentExist(id);
        Component component = components.get(id);

        // 首先从缓存中取
        if (cache.containsKey(id) && component instanceof SingletonComponent) {
            T obj = (T) cache.get(id);
            cache.remove(id);
            return obj;
        }

        return (T) component.create();
    }

    @Override
    public <T> T getObject(Class<T> type) {
        // 首先从缓存中取
        for (Object o : cache.values()) {
            if (ReflectUtils.getWrap(type).isAssignableFrom(ReflectUtils.getWrap(o.getClass()))) {
                return type.cast(o);
            }
        }

        List<Component> res = new ArrayList<>();
        components.forEach((id, c) -> {
            if (c.getType() != null && ReflectUtils.getWrap(type).isAssignableFrom(ReflectUtils.getWrap(c.getType()))) {
                res.add(c);
            }
        });

        if (res.size() == 0) {
            throw new ByxContainerException(Message.componentNotFoundWithType(type));
        } else if (res.size() > 1) {
            throw new ByxContainerException(Message.multiComponentsWithType(type));
        }

        return type.cast(res.get(0).create());
    }

    @Override
    public Class<?> getType(String id) {
        checkComponentExist(id);
        return components.get(id).getType();
    }

    private void checkComponentExist(String id) {
        if (!components.containsKey(id)) {
            throw new ByxContainerException(Message.componentNotFoundWithId(id));
        }
    }

    @Override
    public void cacheObject(String id, Object obj) {
        cache.put(id, obj);
    }
}
