/**If Bank cannot find the account number to transfer to, throw this exception */
public class BankAccountNotFoundException extends AccountTransactionException {
    public BankAccountNotFoundException(String accountNo){
        super("Account Number: " + accountNo + " not found!");
    }
}
