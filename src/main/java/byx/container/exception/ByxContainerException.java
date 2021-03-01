package byx.container.exception;

/**
 * ByxContainer异常基类
 *
 * @author byx
 */
public class ByxContainerException extends RuntimeException {
    public ByxContainerException(String msg) {
        super(msg);
    }

    public ByxContainerException(String msg, Exception e) {
        super(msg, e);
    }
}
