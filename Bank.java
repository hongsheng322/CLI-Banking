import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

/** Singleton Bank*/
public class Bank {
    private static Bank singleton = new Bank( );
    private static HashMap<String, String> userMap;
    private static Customer customer = new Customer();
    private static Currency currency = Currency.SGD;
    private final static float ACCESS_FEE = 0.02f;

    /** Currencies accepted by the bank account.*/
    enum Currency {
        SGD,USD,EUR,JPY,AUD,GBP
    }

    /** A private Constructor prevents any other class from instantiating.*/
    private Bank() { 
        try {
        	checkServerTime();
		userMap = initUsersHashMap();
	} catch (ParseException | IOException e) {
            System.out.println(e);
	}
    }
 
    /** Static instance method */
    public static Bank getInstance( ) {
       return singleton;
    }

    /* Other methods protected by singleton */

    /** get current customer that is logged into the bank instance
     *  @return customer object
    */
    protected Customer getCustomer() {
        return customer;
    }

    /** 
    *  @param c customer object
    */
    protected void setCustomer(Customer c) {
        customer = c;
    }

    /** remove customer when logout */
    protected void removeCustomer() {
        setCustomer(new Customer());
    }

    /** @return access fee charged by the bank */
    protected float getAcessFee() {
        return ACCESS_FEE;
    }

    /** @return hashMap of user accounts */
    protected HashMap<String, String> getUserMap(){
        return userMap;
    }

    /** set the currency of ATM bank instance
     * @param id the choice of currency
     */
    protected void setCurrency(int id) {
        switch(id){
            case 1:
                currency = Currency.SGD;
                break;
            case 2:
                currency = Currency.USD;
                break;
            case 3:
                currency = Currency.EUR;
                break;
            case 4:
                currency = Currency.JPY;
                break;
            case 5:
                currency = Currency.AUD;
                break;
            case 6:
                currency = Currency.GBP;
                break; 
            default:
                currency = Currency.SGD;      
        }
    }

    /** @return curerency of the ATM bank instance */
    protected Currency getCurrency() {
        return currency;
    }

    /** get the location of the bank using currency
     * @return location of ATM in string */
    protected String getCurrencyLocation() {
        switch(getCurrency()){
            case SGD:
                return "Singapore";
            case USD:
                return "United States";
            case EUR:
                return "Europe";
            case JPY:
                return "Japan";
            case AUD:
                return "Australia";
            case GBP:
                return "United Kingdom"; 
            default:
                return "United States";
        }
    }

    /**
     * Check server time from CSV and update limit and compound interest respectively
      * @throws IOException 
      * @throws ParseException 
     */
    protected static void checkServerTime() throws ParseException, IOException{
    	Date newdate = new Date();
    	Date prevdate = csvReadWrite.viewServerTime();
    	if (new SimpleDateFormat("yyyy-MM-dd").format(prevdate).equals(new SimpleDateFormat("yyyy-MM-dd").format(newdate))) {
    		csvReadWrite.updateServerTime(newdate);
    		return;
    	}
    	csvReadWrite.updateServerTime(newdate);
    	csvReadWrite.refreshlimit();
    	updateInterest(csvReadWrite.viewLastCompoundTime(),newdate);    	
    }
     	/**
	* calculate and update interest for all accounts
    	* @param LastCompoundTime   newdate
	* @throws IOException 
        * @throws ParseException 
   	 */
	protected static void updateInterest(Date LastCompoundTime,Date newdate) throws ParseException, IOException{
    	 List<Account> accountList = new ArrayList<Account>();
    	 accountList = csvReadWrite.viewAllAccount();
    	 if(accountList.size()!=0) {
    		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    			
    			String[] str = sdf.format(LastCompoundTime).split("-");
    			int day1 = Integer.parseInt(str[2]);
    			int month1 = Integer.parseInt(str[1]);
    			int year1= Integer.parseInt(str[0]);
    			
    			str = sdf.format(newdate).split("-");
    			int day2 = Integer.parseInt(str[2]);
    			int month2 = Integer.parseInt(str[1]);
    			int year2= Integer.parseInt(str[0]);				
    			
    			int tempm = 0;
    			
    			if(year1 == year2) {//same year
    				if(month1 == month2) {//same month
    					if(islastday(month2,day2)) { //check if is last day of the month
    						//yes, calculate interest for the month and update transaction
    						for (Account acc : accountList) 
    							acc.compoundInterest(sdf.parse(String.format("%d-%d-%d", year1,month1+1,1)),getlastday(month1)-day2+1,true);
    					}
    					else {
    						//no, calculate interest for the month till newdate
    						for (Account acc : accountList) 
    							acc.compoundInterest(newdate,day2-day1+1,false);
    					}
    					
    				 }else {//different month
    					 for (Account acc : accountList) 
 							acc.compoundInterest(sdf.parse(String.format("%d-%d-%d", year2,month1+1,1)),getlastday(month1)-day2+1,true);
    					 if(islastday(month2, day2)) {
						 
    						//yes, calculate interest for the all months and update transaction
    						 for(int i=month1+1; i <= month2+1;i++) {
    							 System.out.println(i);
    							 for (Account acc : accountList) 
    		 							acc.compoundInterest(sdf.parse(String.format("%d-%d-%d", year2,i,1)),getlastday(month1)-day2+1,true);
    							 
    						 } 
    					 }else {
    						//no, calculate interest for the all months and update transaction
    						 for(int i=month1+1; i <= month2;i++) {
    							 System.out.println(i);
    							 for (Account acc : accountList) 
 		 							acc.compoundInterest(sdf.parse(String.format("%d-%d-%d", year2,i,1)),getlastday(month1)-day2+1,true);
    						 
    						 }
						 //calculate interest for the month till newdate
						for (Account acc : accountList) 
		 					acc.compoundInterest(sdf.parse(String.format("%d-%d-%d", year2,month2,day2)),getlastday(month2)-day2+1,false);
    					 }
    				 }
    			 }else {//different year
				//calculate interest for the month untill end of the month for LastCompoundTime
    				 for (Account acc : accountList) 
							acc.compoundInterest(sdf.parse(String.format("%d-%d-%d", year1,month1+1,1)),getlastday(month1)-day2+1,true);
    				 tempm = month1+1;
    				 if(islastday(month2, day2)) {//check if is last day of the month
    					//yes, calculate interest for the years
    					 for (int y=year1;y<year2;y++) {
    						 for(int m = tempm; m <=12;m++) {
    							//the months of the year and update transaction
    							 for (Account acc : accountList) 
    								acc.compoundInterest(sdf.parse(String.format("%d-%d-%d", y,m,1)),getlastday(m),true);
    						 }
    						 if(tempm==12) {
    							 tempm=1;
    						 }
    					 }
    					 //calculate interest for newdate year until latest month
    					 for(int i=1; i <= month2;i++) {
    						 System.out.println(i);
    						 for (Account acc : accountList) 
							acc.compoundInterest(sdf.parse(String.format("%d-%d-%d", year2,i,1)),getlastday(i),true);    						 
    					 } 
    				 }else {
    					 for (int y=year1;y<year2;y++) {//check if is last day of the month
    						//yes, calculate interest for the years
    						 for(int m = tempm; m <=12;m++) {
    							 for (Account acc : accountList) 
 								acc.compoundInterest(sdf.parse(String.format("%d-%d-%d", y,m,1)),getlastday(m),true);  
    						 }
    						 if(tempm==12) {
    							 tempm=1;
    						 }
    					 }
    					 //calculate interest for newdate year until latest month
    					 for(int i=1; i <= month2;i++) {
    						 System.out.println(i);
    						 for (Account acc : accountList) 
							acc.compoundInterest(sdf.parse(String.format("%d-%d-%d", year2,i,1)),getlastday(i),true);  
    						 
    					 } 
					 //calculate interest for the month till newdate
    					 for (Account acc : accountList) 
						acc.compoundInterest(sdf.parse(String.format("%d-%d-%d", year2,month2,day2)),getlastday(month2)-day2+1,false);  
    				 }
    			 }
    		}
    	 else {//no account
    		 return;
    	 }
    	 csvReadWrite.updateLastCompoundTime(newdate);
	} 
     	/**
	* check is day is last day of month
    	* @param month  day
   	 */
	public static boolean islastday(int month,int day) {
	    switch (month) {
	        case 1: if(day == 31) return true;
	        case 2: if(day == 28||day ==29) return true;
	        case 3: if(day == 31) return true;
	        case 4: if(day == 30) return true;
	        case 5: if(day == 31) return true;
	        case 6: if(day == 30) return true;
	        case 7: if(day == 31) return true;
	        case 8: if(day == 31) return true;
	        case 9: if(day == 30) return true;
	        case 10: if(day == 31) return true;
	        case 11: if(day == 30) return true;
	        case 12: if(day == 31) return true;
	    }
	    return false;
	}
	/**
	* get the last day of month
    	* @param month
   	 */
	public static int getlastday(int month) {
	    switch (month) {
	        case 1: return 31;
	        case 2: return 28;
	        case 3:  return 31;
	        case 4: return 30;
	        case 5: return 31;
	        case 6: return 30;
	        case 7: return 31;
	        case 8: return 31;
	        case 9: return 30;
	        case 10: return 31;
	        case 11: return 30;
	        case 12: return 31;
	    }
	    return 30;
    }

    /**
    * Initialise customer info, customer accounts and transactions from CSV
    * @param username   username string
     * @throws IOException 
     * @throws ParseException 
    */
    protected void initCustomer(String username) throws ParseException, IOException{
    	List<Account> accountList = new ArrayList<Account>();
    	List<Transaction> transactionList = new ArrayList<Transaction>();
    	accountList = csvReadWrite.viewUserAccounts(username);
    	if(accountList.size()!=0) {
    		for (int i = 0; i<accountList.size();i++){
    			getCustomer().addAccount(accountList.get(i));
    			transactionList = csvReadWrite.viewAccountTransaction(accountList.get(i).getAccountNumber());
    			if(transactionList.size() !=0) {
    				for (Transaction tran :transactionList){
    	    			getCustomer().getAccount(i).AddTransaction(tran);
    				}
    			}
    		}
    	}    	
    }
    
    /**Iinitialize username and password to UserMap
     * @throws IOException 
     * @throws ParseException */
    protected HashMap<String, String> initUsersHashMap() throws ParseException, IOException{
        return csvReadWrite.viewUser();
    }

    /**
    * checks username for length and illegal characters, returns true if pass
    * @param s     username string
    * @return      returns true if all conditions are met
    */
    protected boolean checkUserName(String s) {
        // return false is string is empty
        try{
            if (s == null){
                return false;
             }
             // checks if user name exist
             if (getUserMap().containsKey(s)){
                throw new UserExistException("Username exist!");
             }
             // checks if username is too short
             int len = s.length();
             if (len < 6){
                throw new IllegalArgumentException("Username too short!");
             }
             // checks if username is too long
             else if  (len > 20) {
                 throw new IllegalArgumentException("Username too long!");
             }
             // checks whether the character is neither a letter nor a digit
             for (int i = 0; i < len; i++) {
                if ((Character.isLetterOrDigit(s.charAt(i)) == false)) {
                     throw new IllegalArgumentException("Username must contain only alphabets and numbers!");
                 }
             }
             // return true if all conditions are met
             return true;
        } catch (UserExistException e) {
            System.out.println(e);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
        return false;
    }

    /**
    * checks the password string for length and illegal characters, returns true is pass
    * @param s     password string
    * @return      returns true if all conditions are met
    */
    protected boolean checkPin(String s) {
        if (s == null){
           return false;
        }
        int len = s.length();
        if (len != 6){
            throw new IllegalArgumentException("length of pin is not 6!");
        }
        for (int i = 0; i < len; i++) {
           // checks whether the character is neither a letter nor a digit
           if ((Character.isDigit(s.charAt(i)) == false)) {
            throw new IllegalArgumentException("Illegal Character in pin!");
            }
        }
        return true;
    }
}
