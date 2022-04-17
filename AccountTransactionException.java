/**Superclass for Transaction related exception */
public class AccountTransactionException extends Exception {
    public AccountTransactionException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public AccountTransactionException(String msg) {
        super(msg);
    }
}