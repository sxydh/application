package bhe.net.cn.exception;

public class AuthException extends RuntimeException {

    private static final long serialVersionUID = -7930084416644014920L;

    public AuthException(String message) {
        super(message);
    }
}
