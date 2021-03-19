package byx.container.core;

public interface CachedContainer extends Container {
    void cacheObject(String id, Object obj);
}
