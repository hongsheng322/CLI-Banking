/** Throw if user can be found but pin does not match */
public class UserWrongPasswordException extends UserAuthenticationException {
    public UserWrongPasswordException(String username) {
        super(username);
    }
}
