import java.util.ArrayList;
import java.util.List;

/**sub class of User. 
 * Contains all the acounts that belongs to the user
 */
public class Customer extends User {
    private List<Account> accounts;

    public Customer(){
        super(null, null);
        this.accounts = new ArrayList<Account>();
    }

    public Customer(String userID, String pin){
        super(userID, pin);
        this.accounts = new ArrayList<Account>();
    }

    /**
     * @return the user pin
     */
    public String getPin() {
        return super.pin;
    }

    /** sets the pin of the user */
    public void setPin(String pin) {
        super.pin = pin;
    }

    /**
    * @return the userID
    */
    public String getUserID() {
        return super.userID;
    }

    /** sets the userID */
    public void setUserID(String userID) {
        super.userID = userID;
    }

    /** get the list of accounts that belongs to the user */
    public List<Account> getAccount(){
        return this.accounts;
    }

    /**Add account to the list of accounts that belongs to the user
    * @param index index of the account list to retrieve
    * @return account that belongs to the index of the list
    */
    public Account getAccount(int index){
        return this.accounts.get(index);
    }

    /**Add account to the list of accounts that belongs to the user
    * @param account account to add to the list
    */
    public void addAccount(Account account){
        this.accounts.add(account);
    }
}