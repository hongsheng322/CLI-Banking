/**Superclass for Authentication related exception */
public class UserAuthenticationException extends Exception {
    public UserAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserAuthenticationException(String msg) {
        super(msg);
    }
}