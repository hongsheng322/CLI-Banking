//create object without exposing the creation logic to the client and refer to newly created object using a common interface
/**Used for creating accounts using a common interface */
public class AccountFactory {
   /**
    * Use AccountFactory to create a new account object
    * @param accountType String to call the account type you want to create
    * @return Account object
    */
    public Account createAccount(String accountType, String accountNumber, double balance, double dailyLimit, double currentlimit, double overseaslLimit, double overseasCurrentlLimit){
      // balance is a double will not be null 
      if(accountType == null || accountNumber == null){ 
          return null;
       }
       else{
         if(accountType.equalsIgnoreCase("CHECKING")){
            return new CheckingAccount(accountNumber, balance, dailyLimit, currentlimit, overseaslLimit, overseasCurrentlLimit);
         } 
         else if(accountType.equalsIgnoreCase("SAVINGS")){
            return new SavingsAccount(accountNumber, balance, dailyLimit, currentlimit, overseaslLimit, overseasCurrentlLimit);
         }
       }
       return null;
    }

   /**
    * Use AccountFactory to create a new account object
    * @param accountType String to call the account type you want to create
    * @return Account object
    */
    public Account createAccount(String accountType, String accountNumber, double balance){
      // balance is a double will not be null 
      if(accountType == null || accountNumber == null){ 
          return null;
       }
       else{
         if(accountType.equalsIgnoreCase("CHECKING")){
            return new CheckingAccount(accountNumber, balance);
         } 
         else if(accountType.equalsIgnoreCase("SAVINGS")){
            return new SavingsAccount(accountNumber, balance);
         }
       }
       return null;
    }
 }
