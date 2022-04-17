/**sub class of Account */
class CheckingAccount extends Account{
    //all instances share the same value, and can’t be altered after first initialized
    private final static double INTEREST_RATE = 0.005;
    private final static String ACCOUNT_TYPE = "CHECKING";

    public CheckingAccount(){
        this.setAccountNumber(null);
        this.setBalance(0);
    }

    public CheckingAccount(String accountNumber,double balance){
        this.setAccountNumber(accountNumber);
        this.setBalance(balance);
    }

    public CheckingAccount(String accountNumber,double balance, double dailyLimit, double currentlimit, double overseaslLimit, double overseasCurrentlLimit){
        this.setAccountNumber(accountNumber);
        this.setBalance(balance);
        this.setCurrentlimit(currentlimit);
        this.setDailyLimit(dailyLimit);
        this.setOverseaslLimit(overseaslLimit);
        this.setOverseasCurrentlLimit(overseasCurrentlLimit);
    }

    @Override
    public String toString(){
        return "Account Type: " + this.getAccountType() + "\n" +
                "Account Number: " + this.getAccountNumber() + "\n" +
                "Balance: " + this.getBalance() + "\n" + 
                "Interest Rate: " + (this.getInterest() * 100) + "%\n"; 
    }
    
    /**parse data to CSV format 
     * @return CSV transaction string
     */ 
     public String toCSV() {
        return accountNumber+"',"+ACCOUNT_TYPE+","+balance+","+this.getDailyLimit()+","+this.getCurrentlimit()+","+this.getOverseaslLimit()+","+this.getOverseasCurrentlLimit();
     }
    
    @Override
    public String getAccountType(){
        return ACCOUNT_TYPE;
    };

    public double getInterest(){
        return INTEREST_RATE;
    }
}
