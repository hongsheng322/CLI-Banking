import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**Account is the superclass for checking and savings account */
abstract class Account implements AccountInterface {
    protected double balance;
    protected String accountNumber;
    private double currentLimit;
    private double dailyLimit;
    private double overseaslLimit;
    private double overseasCurrentlLimit;
    private ArrayList<Transaction> transactions;

    //variables to calculate compound interest and daily limit
    private double compoundInterest;
    
    /**
    * get account type
    * @return  account type string
    */
    public abstract String getAccountType();

    /**
    * get csv String format
    * @return  csv string
    */
    public abstract String toCSV();

    /**
    * get Interest rate
    * @return  Interest rate (double)
    */
    public abstract double getInterest();

    /**
    * Constructor of Account
    * creates a new array list for transaction
    */
    public Account(){
        this.transactions = new ArrayList<Transaction>();
    }

    /**
    * get transactions arraylist
    * @return  ArrayList<Transaction>
    */
    public ArrayList<Transaction> getTransaction(){
        return transactions;
    }

    /**
    * get index of transactions arraylist
    * @return  ArrayList<Transaction>.get(index)
    */
    public Transaction getTransaction(int index){
        return transactions.get(index);
    }

    /**
    * Adds a new transaction to account
    */
    public void AddTransaction(Transaction transaction){
        transactions.add(transaction);
    }

    /**
    * Get balance of account
    * @return this.balance
    */
    public double getBalance(){
        return this.balance;
    }

    /**
    * Set balance of account
    * @param balance to set
    */
    public void setBalance(double balance){
        this.balance = balance;
    }

    /**
    * Get account number
    * @return this.accountNumber
    */
    public String getAccountNumber(){
        return this.accountNumber;
    }

    /**
    * Set account number
    * @param accountNumber number to set
    */
    public void setAccountNumber(String accountNumber){
        this.accountNumber = accountNumber;
    }

    /**
    * Get overseas limit
    * @return this.overseasCurrentlLimit
    */
    public double getOverseasCurrentlLimit() {
        return this.overseasCurrentlLimit;
    }

    /**
    * Set the current limit of overseas limit
    * @param overseasCurrentlLimit limit to set
    */
    public void setOverseasCurrentlLimit(double overseasCurrentlLimit) {
        this.overseasCurrentlLimit = overseasCurrentlLimit;
    }

    /**
    * Get getOverseaslLimit limit
    * @return this.getOverseaslLimit
    */
    public double getOverseaslLimit() {
        return overseaslLimit;
    }

    /**
    * Sets the overseas limit
    * @param overseaslLimit limit to set
    */
    public void setOverseaslLimit(double overseaslLimit) {
        this.overseaslLimit = overseaslLimit;
    }

    /**
    * Get current limit
    * @return this.current
    */
    public double getCurrentlimit() {
        return currentLimit;
    }

    /**
    * Sets the current limit
    * @param currentLimit limit to set
    */
    public void setCurrentlimit(double currentLimit) {
        this.currentLimit = currentLimit;
    }

    /**
    * Get daily limit
    * @return this.dailyLimit
    */
    public double getDailyLimit() {
        return dailyLimit;
    }

    /**
    * Sets the daily limit
    * @param dailyLimit limit to set
    */
    public void setDailyLimit(double dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    /**
    * Deposit to account and writes to CSV
    * @param amount amount to deposit
    * @throws InvalidAmountException
    */
    public void deposit(double amount) throws InvalidAmountException{
        if(amount <= 0){
            throw new InvalidAmountException();
        }

        amount = Math.round(amount * 100.0) / 100.0;
        balance += amount;
        balance = Math.round(balance * 100.0) / 100.0; //round off values to 2dp

        //update to CSV
        try {
            csvReadWrite.updateAccount(getAccountNumber(), toCSV());
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        // create a new transaction
        Transaction t = new Transaction(accountNumber, new Date() , "Deposit to ATM", "", new Date(),
        "", String.format("%.2f",amount), String.format("%.2f",balance));

        // add transaction to CSV
        try {
		csvReadWrite.addTransaction(t);
        	getTransaction().add(t);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Deposit to account and writes to CSV
    * @param amount amount to deposit
    * @param message custom message for transaction
    * @throws InvalidAmountException
    */
    public void deposit(double amount, String message) throws InvalidAmountException{

        if(amount <= 0){
            throw new InvalidAmountException();
        }

        amount = Math.round(amount * 100.0) / 100.0;
        balance += amount;
        balance = Math.round(balance * 100.0) / 100.0; //round off values to 2dp

        //update to CSV
        try {
            csvReadWrite.updateAccount(getAccountNumber(), toCSV());
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        // create a new transaction
        Transaction t = new Transaction(accountNumber, new Date() , message, "", new Date(),
        "", String.format("%.2f",amount), String.format("%.2f",balance));

        // add transaction to CSV
        try {
		csvReadWrite.addTransaction(t);
        	getTransaction().add(t);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Withdraw from account and writes to CSV
    * @param amount amount to Withdraw
    * @throws InsufficientFundsException
    * @throws OverLimitException
    */
    public void withdraw(double amount) throws InsufficientFundsException, OverLimitException {
        if (amount > getBalance()) {
            throw new InsufficientFundsException(amount);
        }
        else {
            if (amount > currentLimit)
            {
                throw new OverLimitException("you have exceeded Current limit! Remaining daily limit:" + currentLimit);
            }

            amount = Math.round(amount * 100.0) / 100.0;
            balance -= amount;

            // no need to deduct daily limit if it is not set
            if(dailyLimit !=0){
                currentLimit -= amount;
            }

            //round off values to 2dp
            balance = Math.round(balance * 100.0) / 100.0;
            currentLimit = Math.round(currentLimit * 100.0) / 100.0;

            // update account to CSV
            try {
                csvReadWrite.updateAccount(this.getAccountNumber(), this.toCSV());
            } 
            catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // create a new transaction for interest
            Transaction t = new Transaction(accountNumber, new Date() , "Withdraw from ATM", "", new Date(),
            String.format("%.2f",amount), "", String.format("%.2f",balance));

            // add the transaction to CSV
            try {
		csvReadWrite.addTransaction(t);
        	getTransaction().add(t);
            } 
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    /**
    * Withdraw from account and writes to CSV
    * @param amount amount to Withdraw
    * @param message custom message for transaction
    * @throws InsufficientFundsException
    * @throws OverLimitException
    */
    public void withdraw(double amount, String message) throws InsufficientFundsException, OverLimitException {

        if (amount > getBalance()) {
            throw new InsufficientFundsException(amount);
        }
        else {
            if (amount > currentLimit)
            {
                throw new OverLimitException("you have exceeded Current limit! Remaining daily limit:" + currentLimit);
            }

            amount = Math.round(amount * 100.0) / 100.0;
            balance -= amount;

            // no need to deduct daily limit if it is not set
            if(dailyLimit !=0){
                currentLimit -= amount;
            }

            //round off values to 2dp
            balance = Math.round(balance * 100.0) / 100.0;
            currentLimit = Math.round(currentLimit * 100.0) / 100.0;

            // update account to CSV
            try {
                csvReadWrite.updateAccount(this.getAccountNumber(), this.toCSV());
            } 
            catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // create a new transaction for interest
            Transaction t = new Transaction(accountNumber, new Date() , message, "", new Date(),
            String.format("%.2f",amount), "", String.format("%.2f",balance));

            // add the transaction to CSV
            try {
		csvReadWrite.addTransaction(t);
        	getTransaction().add(t);
            } 
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    /**
    * Withdraw from account and writes to CSV
    * @param amount amount to Withdraw
    * @param location location of ATM
    * @throws InsufficientFundsException
    * @throws OverLimitException
    */
    public void overseasWithdraw(double amount, String location) throws InsufficientFundsException, OverLimitException {
        if (amount > getBalance()) {
            throw new InsufficientFundsException(amount);
        }
        else {
            if(overseasCurrentlLimit == 0){
                throw new OverLimitException("you have not set a Overseas Withdrawal limit!");
            }
            if (amount > overseasCurrentlLimit)
            {
                throw new OverLimitException("you have exceeded Overseas Withdrawal limit!");
            }
            
            //round off to the nearest 2 dp
            amount = Math.round(amount * 100.0) / 100.0;
            balance -= amount;

            // no need to deduct daily limit if it is not set
            if(dailyLimit !=0){
                overseasCurrentlLimit -= amount;
            }

            //round off values to 2dp
            balance = Math.round(balance * 100.0) / 100.0;
            overseasCurrentlLimit = Math.round(overseasCurrentlLimit * 100.0) / 100.0;

            // update account to CSV
            try {
                csvReadWrite.updateAccount(this.getAccountNumber(), this.toCSV());
            } 
            catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // create a new transaction for interest
            Transaction t = new Transaction(accountNumber, new Date() , "Overseas Withdraw from " + location, "", new Date(),
            String.format("%.2f",amount), "", String.format("%.2f",balance));

            // add the transaction to CSV
            try {
		        csvReadWrite.addTransaction(t);
        	    getTransaction().add(t);
            } 
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    /**
    * Compounds interest and writes to CSV
    * @throws IOException
    */
    public void compoundInterest(Date date, int days, boolean updateTransaction) throws IOException{
    	compoundInterest = csvReadWrite.getInterest(getAccountNumber());
    	if(updateTransaction) {
            compoundInterest = balance * Math.pow(1 + (getInterest()/365),days)+compoundInterest-balance;
            balance = balance+compoundInterest;
            // create a new transaction for interest
            Transaction t = new Transaction(accountNumber, date , "Interest", "", date,
            "", String.format("%.2f",compoundInterest), String.format("%.2f",balance));

            // add the transaction to CSV
            try {
		        csvReadWrite.addTransaction(t);
        	    getTransaction().add(t);
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
            compoundInterest = 0;

            // update account to CSV
            try {
                csvReadWrite.updateAccount(getAccountNumber(), String.format("%.2f",balance),String.format("%.2f",compoundInterest));
            } 
            catch (IOException e) {
                e.printStackTrace();
                return;
            }

            
        }else {
        	
        	compoundInterest = balance * Math.pow(1 + (getInterest()/365),days)+compoundInterest-balance;
        	// update account to CSV
            try {
                csvReadWrite.updateAccount(getAccountNumber(), String.format("%.2f",balance),String.format("%.2f",compoundInterest));
            } 
            catch (IOException e) {
                e.printStackTrace();
            }        	
        }
    	return;
    }
    /**
    * Prints out the transaction of current account
    */
    public void printTransactionTable(){
        if (getTransaction().isEmpty()){
            System.out.println("Account does not have any transactions yet!");
            return;
        }

		String leftAlignFormat = "| %-15s | %-10s | %-50s | %-15s | %-15s | %-17s | %-15s | %-15s | %n";

		System.out.format("+-----------------+------------+----------------------------------------------------+-----------------+-----------------+-------------------+-----------------+-----------------+%n");
		System.out.format("| Account Number  | Date       | Transaction Detail                                 | Cheque Number   | Value Date      | Withdrawal Amount | Deposit Amount  | Balance Amount  |%n");
		System.out.format("+-----------------+------------+----------------------------------------------------+-----------------+-----------------+-------------------+-----------------+-----------------+%n");
		for (int i = 0; i < getTransaction().size(); i++) {
			Date date = getTransaction(i).getDate();
			Date vdate = getTransaction(i).getDate();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");  
			String strDate = dateFormat.format(date); 
			String strvDate = dateFormat.format(vdate);
			System.out.format(leftAlignFormat, getTransaction(i).getAccountNo(), strDate, getTransaction(i).getTransactionDetail(),
			getTransaction(i).getChequeNo(), strvDate, getTransaction(i).getWithdrawAMT(), getTransaction(i).getDepositAMT(),
			getTransaction(i).getBalanceAMT());
		}
		System.out.format("+-----------------+------------+----------------------------------------------------+-----------------+-----------------+-------------------+-----------------+-----------------+%n");
	}
}

