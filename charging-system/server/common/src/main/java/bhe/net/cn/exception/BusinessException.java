package bhe.net.cn.exception;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -3661169107186711564L;

    public BusinessException(String message) {
        super(message);
    }
}
