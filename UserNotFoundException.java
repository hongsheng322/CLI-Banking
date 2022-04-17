/** Throw if user cannot be found during login */
public class UserNotFoundException extends UserAuthenticationException {
    public UserNotFoundException(String username) {
        super(username);
    }
}