/**Contract for Account class */
interface AccountInterface {
    // throws exception is amount < 0
    void deposit(double amount)throws InvalidAmountException;
    // throws exception if amount < 0 after withdraw
    void withdraw(double amount) throws InsufficientFundsException, OverLimitException;
    //get account type
    String getAccountType();
}
