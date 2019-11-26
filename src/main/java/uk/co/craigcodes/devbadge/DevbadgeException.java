package uk.co.craigcodes.devbadge;

public class DevbadgeException extends RuntimeException {

    public DevbadgeException(String msg, Throwable cause) {
        super(msg, cause);
    }
    public DevbadgeException(String msg) {
        super(msg);
    }
}
