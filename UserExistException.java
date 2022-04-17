/**Throw if username exist during account creation exception */
public class UserExistException extends UserAuthenticationException {
    public UserExistException(String username) {
        super(username);
    }
}