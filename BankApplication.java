import java.util.Scanner;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Objects;

//* Driver class for our bank application */
public class BankApplication {
    private static Bank bank = Bank.getInstance(); // instance of singleton bank
    private static Scanner input = new Scanner(System.in); // scanner for input
    private static String username = null; // username for login
    private static String pin = null; // pin for login
    private static Encryptor encryptor = new Encryptor();
    private static AccountFactory accountfactory = new AccountFactory(); // accountfactory for creating new accounts
    final static DecimalFormat df = new DecimalFormat("0.00"); // Defining the output for the dollar variables

    public static void main(String[] args){
        bank.setCurrency(selectCurrencyMenu());
        displayloginMenu();
        input.close();
        System.out.print(Unicode.TEXT_RESET);
    }

    //* prints the banner */
    public static void printQuickAccessMenu(){
        System.out.println("===========================================================");
        System.out.println( "BANK OF NOOK " + bank.getCurrencyLocation().toUpperCase());
        System.out.println("Welcome, "+ bank.getCustomer().getUserID());
        System.out.println("===========================================================");
        System.out.println("Quick access menu: (access these services by inputting the numbers)");
        System.out.println("1: Withdraw $10");
        System.out.println("2: Withdraw $50");
        System.out.println("3: Withdraw $100");
        System.out.println("4: Withdraw other amount");
        System.out.println("5: Deposit");
        System.out.println("6: View accounts (balance, transactions, transfer, settings)");
        System.out.println("7: Create New Account");
        System.out.println("8: Logout");
        System.out.println("===========================================================");
    }

    /**User Interface for main menu 
     * @throws ArgumentOutOfRangeException
     * @throws AccountTransactionException
     */ 
    public static void displayMainMenu() {
        int userInput = 0;
        boolean userIsDone = false;

        // print main menu
        printQuickAccessMenu();

        while (userIsDone != true){
            int choice;
            try {
                System.out.print("Your selection: ");
                String userInputStr = input.nextLine();
                //System.out.println("\nYour input is: "+ userInputStr);
                
                userInput = 0;
                userInput = Integer.parseInt(userInputStr);

                if (userInput < 1 || userInput > 8 ) {
                    userIsDone = false;
                    throw new ArgumentOutOfRangeException(1, 8);
                }
                else {
                    switch (userInput) {
                        case 1:
                            // perform account withdraw $10
                            choice = selectAccount();
                            System.out.println("Withdrawing $10...");
                            overseasWithdrawal(10, choice);
                            break;
                        case 2:
                            // perform account withdraw $50
                            choice = selectAccount();
                            System.out.println("Withdrawing $50...");
                            overseasWithdrawal(50, choice);
                            break;
                        case 3:
                            // perform account withdraw $100
                            choice = selectAccount();
                            System.out.println("Withdrawing $100...");
                            overseasWithdrawal(100, choice);
                            break;
                        case 4:
                            // perform account withdraw
                            choice = selectAccount();
                            overseasWithdrawal(bank.getCustomer().getAccount(choice));
                            break;
                        case 5:
                            // perform account deposit
                            choice = selectAccount();
                            DepositMenu(bank.getCustomer().getAccount(choice));
                            break;
                        case 6:
                            // account functions
                            Account currentAccount;
                            // let users select their account
                            currentAccount = bank.getCustomer().getAccount(selectAccount());
                            startAccountFunctions(currentAccount);
                            break;
                        case 7:
                            // create new account
                            createAccount(bank.getCustomer());
                            break;
                        case 8:
                            // logout from current session
                            System.out.println("Logging out...");
                            System.out.println("Thank you for using our services, we hope to see you again!");
                            System.out.println("===========================================================");
                            userIsDone = true;
                            bank.removeCustomer();
                            displayloginMenu();
                            break;
                    }
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Input cannot be parsed to a Number!");
            }
            catch (ArgumentOutOfRangeException e){
            }
            printQuickAccessMenu(); // print the menu again once you loop back here
        }
    }

    /**Select country/currency for the ATM
     * @returns an interger choice for the Bank to set currency
     */ 
    private static int selectCurrencyMenu(){
        int choice = -1;
        String leftAlignFormat = " %-20s %-8s %-30s  %n";

        while(true){
            System.out.println("===========================================================");
            System.out.println("Select Current Location");
            System.out.println("===========================================================");
            System.out.format(leftAlignFormat, "1: Singapore", "SGD$","(Singapore Dollar)");
            System.out.format(leftAlignFormat, "2: United States", "USD$","(U.S. Dollar)");
            System.out.format(leftAlignFormat, "3: Europe", "€", "(Euro)");
            System.out.format(leftAlignFormat, "4: Japan", "¥", "(Japanese Yen)");
            System.out.format(leftAlignFormat, "5: Australia", "AUD$","(Australian Dollar)");
            System.out.format(leftAlignFormat, "6: United Kingdom", "£","(Great British Pound)");
            System.out.println("===========================================================");
            System.out.print("Your selection (1-6): ");
    
            try {
                if(input.hasNextInt()) 
                {
                   choice = input.nextInt();
                }
                input.nextLine();
                if (choice < 1 || choice > 6){
                    throw new IllegalArgumentException("Please select a valid choice");
                }
                else{
                    return choice;
                }
            }catch (IllegalArgumentException e){
                System.out.println(e);
            }
        }
    }

    /** Allows customer to deposit the desired amount to his bank account.
    * Deposits directly without conversion if user is currently in SG
    * @param account account to deposit money into
    * @throws AccountTransactionException 
    */ 
    private static void DepositMenu(Account account) {
        CurrencyConverter converter = new CurrencyConverter();
        double amount = 0;
        double convertedAmount = 0;
        double accessFee = 0;
        String leftAlignFormat = "%-30s %-15s%n";
        while(true){
            System.out.println("===========================================================");
            System.out.println("Deposit");
            System.out.println("===========================================================");
            System.out.print("How much do you want to deposit?: ");
            try {
                if(input.hasNextDouble()) 
                {
                    amount = input.nextDouble();
                }
                else
                {
                    throw new InvalidAmountException();
                }
                input.nextLine();
            }
            catch (AccountTransactionException e) {
                System.out.println(e);
            }

            try {
                System.out.println("Please wait a moment. . .");
                switch (bank.getCurrency()) {
                    case SGD:
                        account.deposit(amount);
                        System.out.format(leftAlignFormat, "Depositing", "$"+ df.format(amount) );
                        System.out.format(leftAlignFormat, "Balance", "$"+ df.format(account.getBalance()));
                        promptEnterKey();
                        return;
                    case USD:
                        convertedAmount = converter.convertCurrency(amount, "USD", "SGD");
                        System.out.format(leftAlignFormat, "Amount to Deposit:", "USD$" + df.format(amount));
                        break;
                    case EUR:
                        convertedAmount = converter.convertCurrency(amount, "EUR", "SGD");   
                        System.out.format(leftAlignFormat, "Amount to Deposit:", "€" + df.format(amount));
                        break;
                    case JPY:
                        convertedAmount = converter.convertCurrency(amount, "JPY", "SGD");
                        System.out.format(leftAlignFormat, "Amount to Deposit:", "¥" + df.format(amount));
                        break;
                    case AUD:
                        convertedAmount = converter.convertCurrency(amount, "AUD", "SGD");
                        System.out.format(leftAlignFormat, "Amount to Deposit:", "AUD$" + df.format(amount));
                        break;
                    case GBP:
                        convertedAmount = converter.convertCurrency(amount, "GBP", "SGD");
                        System.out.format(leftAlignFormat, "Amount to Deposit:", "£" + df.format(amount));;
                        break;
                }
                accessFee = bank.getAcessFee() * convertedAmount;
                account.withdraw(accessFee);
                account.deposit(convertedAmount);
            } catch (AccountTransactionException e) {
                System.out.println(e);
            }
            System.out.format(leftAlignFormat, "Amount in SGD:", "SGD$" + df.format(convertedAmount));
            System.out.format(leftAlignFormat, "Access Fee:", "SGD$" + df.format(accessFee));
            System.out.format(leftAlignFormat, "Remaining balance:", "SGD$" + df.format(account.getBalance()));
            promptEnterKey();
            break;
        }
    }

    /** Converts currency and returns value in SGD.
    * Withdraws directly if user is currently in SG
    * @throws AccountTransactionException 
    */ 
    private static void overseasWithdrawal(Account account) {
        CurrencyConverter converter = new CurrencyConverter();
        double amount = 0;
        double convertedAmount = 0;
        double accessFee = 0;
        String leftAlignFormat = "%-30s %-15s%n";

        while(true){
            try {
                System.out.println("===========================================================");
                System.out.print("Please select the amount to withdraw: ");
                if(input.hasNextDouble()) 
                {
                    amount = input.nextDouble();
                }
                else
                {
                    throw new InvalidAmountException();
                }
                input.nextLine();
            }
            catch (AccountTransactionException e) {
                System.out.println(e);
            }

            try {
                System.out.println("Please wait a moment. . .");
                switch (bank.getCurrency()) {
                    case SGD:
                        account.withdraw(amount);
                        System.out.format(leftAlignFormat, "Withdrawing", "$"+ df.format(amount) );
                        System.out.format(leftAlignFormat, "Remaining Balance", "$"+ df.format(account.getBalance()));
                        promptEnterKey();
                        return;
                    case USD:
                        convertedAmount = converter.convertCurrency(amount, "USD", "SGD");
                        System.out.format(leftAlignFormat, "Amount to withdraw:", "USD$" + df.format(amount));
                        break;
                    case EUR:
                        convertedAmount = converter.convertCurrency(amount, "EUR", "SGD");   
                        System.out.format(leftAlignFormat, "Amount to withdraw:", "€" + df.format(amount));
                        break;
                    case JPY:
                        convertedAmount = converter.convertCurrency(amount, "JPY", "SGD");
                        System.out.format(leftAlignFormat, "Amount to withdraw:", "¥" + df.format(amount));
                        break;
                    case AUD:
                        convertedAmount = converter.convertCurrency(amount, "AUD", "SGD");
                        System.out.format(leftAlignFormat, "Amount to withdraw:", "AUD$" + df.format(amount));
                        break;
                    case GBP:
                        convertedAmount = converter.convertCurrency(amount, "GBP", "SGD");
                        System.out.format(leftAlignFormat, "Amount to withdraw:", "£" + df.format(amount));;
                        break;
                }
                accessFee = bank.getAcessFee() * convertedAmount;
                account.overseasWithdraw(convertedAmount + accessFee, bank.getCurrencyLocation());
                //account.withdraw(accessFee, "access fee");
            } catch (AccountTransactionException e) {
                System.out.println(e);
                break;
            }
            System.out.format(leftAlignFormat, "Amount in SGD:", "SGD$" + df.format(convertedAmount));
            System.out.format(leftAlignFormat, "Access Fee:", "SGD$" + df.format(accessFee));
            System.out.format(leftAlignFormat, "Total Amount:", "SGD$" + df.format(convertedAmount + accessFee));
            System.out.format(leftAlignFormat, "Remaining balance:", "SGD$" + df.format(account.getBalance()));
            promptEnterKey();
            break;
        }
    }

    /** Converts currency and returns value in SGD.
    * Withdraws directly if user is currently in SG
    * @param amount the amount to convert
    * @throws AccountTransactionException 
    */ 
    private static void overseasWithdrawal(double amount, int choice) {
        CurrencyConverter converter = new CurrencyConverter();
        double convertedAmount = 0;
        double accessFee = 0;
        String leftAlignFormat = "%-30s %-15s%n";

        while(true){
            try {
                System.out.println("Please wait a moment. . .");
                switch (bank.getCurrency()) {
                    case SGD:
                        bank.getCustomer().getAccount(choice).withdraw(amount);
                        System.out.format(leftAlignFormat, "Withdrawing", "$"+ df.format(amount) );
                        System.out.format(leftAlignFormat, "Remaining Balance", "$"+ df.format(bank.getCustomer().getAccount(choice).getBalance()));
                        promptEnterKey();
                        return;
                    case USD:
                        convertedAmount = converter.convertCurrency(amount, "USD", "SGD");
                        System.out.format(leftAlignFormat, "Amount to withdraw:", "USD$" + df.format(amount));
                        break;
                    case EUR:
                        convertedAmount = converter.convertCurrency(amount, "EUR", "SGD");   
                        System.out.format(leftAlignFormat, "Amount to withdraw:", "€" + df.format(amount));
                        break;
                    case JPY:
                        convertedAmount = converter.convertCurrency(amount, "JPY", "SGD");
                        System.out.format(leftAlignFormat, "Amount to withdraw:", "¥" + df.format(amount));
                        break;
                    case AUD:
                        convertedAmount = converter.convertCurrency(amount, "AUD", "SGD");
                        System.out.format(leftAlignFormat, "Amount to withdraw:", "AUD$" + df.format(amount));
                        break;
                    case GBP:
                        convertedAmount = converter.convertCurrency(amount, "GBP", "SGD");
                        System.out.format(leftAlignFormat, "Amount to withdraw:", "£" + df.format(amount));;
                        break;
                }
                accessFee = bank.getAcessFee() * convertedAmount;
                bank.getCustomer().getAccount(choice).overseasWithdraw(convertedAmount + accessFee, bank.getCurrencyLocation());
                //account.withdraw(accessFee, "access fee");
            } catch (AccountTransactionException e) {
                System.out.println(e);
                break;
            }
            System.out.format(leftAlignFormat, "Amount in SGD:", "SGD$" + df.format(convertedAmount));
            System.out.format(leftAlignFormat, "Access Fee:", "SGD$" + df.format(accessFee));
            System.out.format(leftAlignFormat, "Total Amount:", "SGD$" + df.format(convertedAmount + accessFee));
            System.out.format(leftAlignFormat, "Remaining balance:", "SGD$" + df.format(bank.getCustomer().getAccount(choice).getBalance()));
            promptEnterKey();
            break;
        }
    }

    /** User Interface for Accounts 
    * @throws IllegalArgumentException 
    */ 
    public static void startAccountFunctions(Account currentAccount) {
        int userInput = 0;

        while (true) {
            System.out.println("===========================================================");
            String leftAlignFormat = "| %-30s | %-16s | %n";
            System.out.println("My Account");
            System.out.format("+--------------------------------+------------------+%n");
            System.out.format(leftAlignFormat,"Account Number", currentAccount.getAccountNumber());
            System.out.format("+--------------------------------+------------------+%n");
            System.out.format(leftAlignFormat,"Account Type", currentAccount.getAccountType());
            System.out.format("+--------------------------------+------------------+%n");
            System.out.format(leftAlignFormat,"Account Balance" , "$"+ df.format(currentAccount.getBalance()));
            System.out.format("+--------------------------------+------------------+%n");
            System.out.format(leftAlignFormat,"Interest" , df.format(currentAccount.getInterest()*100) + "%");
            System.out.format("+--------------------------------+------------------+%n");
            if (currentAccount.getDailyLimit() != 0){
                System.out.format(leftAlignFormat,"Daily Limit" , "$"+ df.format(currentAccount.getDailyLimit()));
                System.out.format("+--------------------------------+------------------+%n");
                System.out.format(leftAlignFormat,"Remaining Limit" , "$"+ df.format(currentAccount.getCurrentlimit()));
                System.out.format("+--------------------------------+------------------+%n");
            }
            if (currentAccount.getOverseaslLimit() != 0){
                System.out.format(leftAlignFormat,"Overseas Daily Limit" , "$"+ df.format(currentAccount.getOverseaslLimit()));
                System.out.format("+--------------------------------+------------------+%n");
                System.out.format(leftAlignFormat,"Overseas Remaining Limit" , "$"+ df.format(currentAccount.getOverseasCurrentlLimit()));
                System.out.format("+--------------------------------+------------------+%n");
            }
            System.out.println("1: Bank Transfer");
            System.out.println("2: View Transaction History");
            System.out.println("3: Account Settings (Set withdraw limit, Change pin, Set Overseas Withdraw limit)");
            System.out.println("4: Return");
            System.out.println("===========================================================");

            System.out.print("Your Choice: ");
            if(input.hasNextInt()) 
            {
                userInput = input.nextInt();
            }
            input.nextLine();

            if (userInput<1 || userInput>4) {
                System.out.println("Please enter a valid input!");
                throw new IllegalArgumentException("Please select one of the choices");
            }
            else {
                switch (userInput) {
                    case 1:
                        initiateTransfer(currentAccount);
                        promptEnterKey();
                        break;
                    case 2:          
                        currentAccount.printTransactionTable();
                        promptEnterKey();
                        break;
                    case 3:
                        accountSettings(currentAccount);
                        break;
                    case 4:
                        displayMainMenu();
                        break;
                }
            }
        }
    }

    /** User Interface for transfer 
     * @throws AccountTransactionException
     * @throws BankAccountNotFoundException
     * */ 
    public static void initiateTransfer(Account fromAccount) {
        int userInput = 0;
        System.out.println("===========================================================");
        System.out.println("Which Type Of Transfer Would You Like?");
        System.out.println("1: Transfer to other account");
        System.out.println("2: Inter-account Transfer");
        System.out.println("===========================================================");
        System.out.print("\nYour selection: ");
        String userInputStr = input.nextLine();
        //System.out.println("\nYour input is: "+userInputStr);
        try {
            userInput = Integer.parseInt(userInputStr);
        } catch (NumberFormatException e) {
            System.out.println("Input cannot be parsed to a Number!");
        }
        if (userInput < 1 || userInput > 2) {
            System.out.println("Please enter a valid input!");
        }
        else if (userInput == 1) {
            //perform transfer to other account
            try {
            // transfer ammount
            System.out.println("You are performing a transfer to another bank account.");
            System.out.print("Please input the amount you want to send: $");
            Double sendAmount = input.nextDouble();
            input.nextLine();

            //key in account number
            System.out.print("Key in the account number: ");
            String toAccountNo = input.nextLine();

            //transaction details
            System.out.print("Enter transaction details: ");
            String transactionDetail = input.nextLine();

            System.out.println("You are sending $"+sendAmount+" to "+toAccountNo);

            // withdraw fromAccount
            fromAccount.withdraw(sendAmount, "transfer to account " + toAccountNo);
            /* 
                Deposit toAccount
               - check if account exist
               - deposit to account */

            Account toAccount = csvReadWrite.viewAccount(toAccountNo);

            if (toAccount == null)
                throw new BankAccountNotFoundException(toAccountNo);

            toAccount.deposit(sendAmount, transactionDetail);

            } catch (AccountTransactionException e) {
                System.out.println(e);
            }  catch (ParseException e) {
                System.out.println(e);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        else if (userInput == 2) {
            // Inter-account from savings to current account (one way only)
            // transfer to this account
            // perform inter-bank transfer
            try {
                Account toAccount = bank.getCustomer().getAccount(selectAccount(fromAccount));
                System.out.print("Please input the amount you want to send: $");
                float transferAmount = input.nextFloat();
                input.nextLine();
                System.out.println("You are sending $"+ transferAmount+ " from Account " + fromAccount.getAccountNumber() +" to Account " + toAccount.getAccountNumber());
                fromAccount.withdraw(transferAmount);
                toAccount.deposit(transferAmount);
                String leftAlignFormat = "%-50s %-15s%n";
    
                System.out.format(leftAlignFormat, "Account " + toAccount.getAccountNumber()+ " Balance After Transfer:", "$" + toAccount.getBalance());
                System.out.format(leftAlignFormat, "Account " + fromAccount.getAccountNumber()+ " Balance After Transfer:", "$" + fromAccount.getBalance());
            } catch (AccountTransactionException e) {
                System.out.println(e);
            }
        } 
    }

    /** User Interface for account settings 
     * @throws ArgumentOutOfRangeException
     * @throws IllegalArgumentException
    */
    public static void accountSettings(Account currentAccount){
        int userInput = 0;

        while(true){
            System.out.println("===========================================================");
            System.out.println("Options Menu");
            System.out.println("1: Set Daily limit");
            System.out.println("2: Set Overseas Withdrawal limit");
            System.out.println("3: Change pin");
            System.out.println("4: Return");
            System.out.println("===========================================================");
    
            System.out.print("\nYour selection: ");
            
            //System.out.println("\nYour input is: "+userInputStr);
            try {
                String userInputStr = input.nextLine();
                userInput = Integer.parseInt(userInputStr);
                if (userInput < 1 || userInput > 4) {
                    throw new ArgumentOutOfRangeException(1, 4);
                }
                else if (userInput == 1) {
                    setDailyLimit(currentAccount);
                }
                else if (userInput == 2) {
                    setOverseasLimit(currentAccount);
                }
                else if (userInput == 3) {
                    changepin(currentAccount);
                }
                else{
                    startAccountFunctions(currentAccount);
                    break;
                }
            } 
            catch (NumberFormatException e) {
                System.out.println("Please select a choice!");
            }
            catch (ArgumentOutOfRangeException e) {
                System.out.println(e);
            }
        }
    }

    /** Change pin of current account
     * @param currentAccount selected account to change pin
     * @throws IllegalArgumentException
    */
    public static void changepin(Account currentAccount){
        // change pin
        String pin;
        String confirmPin;
        while(true){
            try{
                System.out.println("===========================================================");
                System.out.println("Choose a 6 digit pin");
                // System.out.print("Enter pin: ");
                // pin = input.nextLine();
                char[] c_pin = System.console().readPassword("Enter pin : ");
                pin = new String(c_pin);
                if(!bank.checkPin(pin)){
                    continue;
                }
                // re-enter pin to confirm
                System.out.println("===========================================================");
                char[] c_rpin = System.console().readPassword("Re-Enter Pin : ");
                confirmPin = new String(c_rpin);
                if (!confirmPin.equals(pin)){
                    System.out.println("pin Does not Match, please try again!");
                }
                else{        
                    pin = encryptor.encryptString(pin);
                    bank.getUserMap().put(username, pin);
                    //update new pin to CSV
                    csvReadWrite.updateUser(username, pin);
                    break;
                }
            }
            catch(IllegalArgumentException e){
                System.out.println(e);
            }
            catch(IOException e){
                System.out.println(e);
            }
            catch(NoSuchAlgorithmException e){
                System.out.println(e);
            }
        }
        accountSettings(currentAccount);
    }

    /** Change daily limit of the account
     * @param currentAccount selected account to set daily limit
     * @throws InvalidAmountException
    */
    public static void setDailyLimit(Account currentAccount){
        // set the withdraw limit
        System.out.print("Enter Daily Withdraw Limit: ");
        double limit = input.nextDouble();
        input.nextLine();
        try{
            if(limit <= 0){
                throw new InvalidAmountException();
            }
            else{
                currentAccount.setDailyLimit(limit);
                currentAccount.setCurrentlimit(limit);
            	//CSV update account dailyLimit
                csvReadWrite.updateAccount(currentAccount);
                System.out.println("Daily Withdraw Limit Has Been Updated.");
                promptEnterKey();
            }
        }catch (InvalidAmountException e) {
            System.out.println(e);
        } catch (IOException e) {
        	 System.out.println(e);
		}
        accountSettings(currentAccount);
    }

    /** Change Overseas daily limit of the account
    * @param currentAccount selected account to set Overseas daily limit
    * @throws InvalidAmountException
    */
    public static void setOverseasLimit(Account currentAccount){
        // set the Overseas withdraw limit
        System.out.print("Enter Overseas Withdraw Limit (resets daily): ");
        double limit = input.nextDouble();
        input.nextLine();
        try{
            if(limit <= 0){
                throw new InvalidAmountException();
            }
            else{
                currentAccount.setOverseasCurrentlLimit(limit);
                currentAccount.setOverseaslLimit(limit);
            	//CSV update account dailyLimit
                csvReadWrite.updateAccount(currentAccount);
                System.out.println("Overseas Withdraw Limit Has Been Updated.");
                promptEnterKey();
            }
        }catch (InvalidAmountException e) {
            System.out.println(e);
        } catch (IOException e) {
        	 System.out.println(e);
		}
        accountSettings(currentAccount);
    }

    /**User Interface for login menu
     * @throws IllegalArgumentException
    */
    public static void displayloginMenu(){
        int choice = -1;

        while(true){
            try{
                System.out.println("===========================================================");
                System.out.println( "BANK OF NOOK " + bank.getCurrencyLocation().toUpperCase());
                System.out.println("===========================================================");
                System.out.println("1: Login");
                System.out.println("2: Create User Account");
                System.out.println("3: Change Text Color");
                System.out.println("4: Shut Down");
                System.out.println("===========================================================");
                System.out.print("Enter choice (1-4): ");
                if(input.hasNextInt()) 
                {
                   choice = input.nextInt();
                }
                input.nextLine();
                if (choice > 4 || choice < 1){
                    throw new IllegalArgumentException("Please select one of the choices");
                }
                else if(choice == 1){
                    login();
                    // exit the loop after user login
                    displayMainMenu();
                }
                else if(choice == 2){
                    createUser();
                }
                else if(choice == 3){
                    changeColorMenu();
                }
                else{
                    System.out.println("System shutting down . . .");
                    System.exit(0);
                    return;
                }
            }
            catch(IllegalArgumentException | IOException e){
                System.out.println(e);
            }

        }
    }

    /**User Interface to login
     * @throws UserNotFoundException
     * @throws UserWrongpinException
    */
    public static void login(){
        do{
            try{
                System.out.print("Enter Username: ");
                username = input.nextLine();
                char[] c_pin = System.console().readPassword("Enter pin : ");
                pin = new String(c_pin);
                pin = encryptor.encryptString(pin);

                if(Objects.equals(bank.getUserMap().get(username), pin)) {
                    System.out.println("Logging in . . .");
                    // initialize customer
                    bank.initCustomer(username);
                    bank.getCustomer().setUserID(username);
                    bank.getCustomer().setPin(pin);
                }
                else if(Objects.equals(bank.getUserMap().get(username), null)) {
                    throw new UserNotFoundException("Account does not exist!");
                }
                else {
                    throw new UserWrongPasswordException("Account with " + username + " does not match with the pin!");
                }
            }
            catch (UserAuthenticationException | ParseException | IOException | NoSuchAlgorithmException e){
                System.out.println(e);
            }

        } while(!Objects.equals(bank.getUserMap().get(username), pin));
    }

    /**User Interface to create User
    * @throws IOException 
    * @throws IllegalArgumentException
    * @throws InvalidAmountException
    */
    public static void createUser() throws IOException{
        String username = null;
        String pin = null;
        String confirmPin = null;
        boolean flag = false;
        Account account = null;
        int choice = -1;
        double initialDeposit = 0;

        // add username
        do{
            flag = false;
            try{
                System.out.println("===========================================================");
                System.out.println("Choose a username 6-20 characters long");
                System.out.print("Enter Username: ");
                username = input.nextLine();
                flag = bank.checkUserName(username);
            }
            catch(IllegalArgumentException e){
                System.out.println(e);
            }
        } while (flag != true);

        // add pin
        do{
            flag = false;
            try{
                System.out.println("===========================================================");
                System.out.println("Choose a 6 digit pin");
                char[] c_pin = System.console().readPassword("Enter pin : ");
                pin = new String(c_pin);
                if(!bank.checkPin(pin)){
                    continue;
                }
                // re-enter pin to confirm
                System.out.println("===========================================================");
                char[] c_rpin = System.console().readPassword("Re-Enter Pin : ");
                confirmPin = new String(c_rpin);
                if (!confirmPin.equals(pin)){
                    System.out.println("pin Does not Match, please try again!");
                }
                else{
                    flag = true;
                }
            }
            catch(IllegalArgumentException e){
                System.out.println(e);
            }
        } while (flag != true);

        // choose account type
        do{
            flag = false;
            try{
                System.out.println("===========================================================");
                System.out.println("Open an account");
                System.out.println("1)Savings Account");
                System.out.println("2)Checking Account");
                System.out.println("===========================================================");
                System.out.print("Enter Choice: ");
                if(input.hasNextInt()) 
                {
                   choice = input.nextInt();
                }
                input.nextLine();

                if (choice > 2 || choice < 1){
                    throw new IllegalArgumentException("Please select 1 or 2");
                }
                else{
                    flag = true;
                }
            }
            catch(IllegalArgumentException e){
                System.out.println(e);
            }
        } while (flag != true);

        // initial deposit
        do{
            flag = false;
            try{
                if(choice == 1){
                    System.out.println("You have selected Savings Account");
                }
                else{
                    System.out.println("You have selected checking Account");
                }
                System.out.print("Enter Initial amount: ");
                initialDeposit = input.nextDouble();
                input.nextLine();

                if(initialDeposit <= 0){
                    throw new InvalidAmountException();
                }
                else{
                    flag = true;
                }
            }
            catch(InvalidAmountException e){
            }
        } while (flag != true);
        try {
            pin = encryptor.encryptString(pin);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        User tempUser = new User(username, pin);
        Customer customer = new Customer(username, pin);
        long accountNumber = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        //System.out.println(accountNumber);
        if (choice == 1){
            account = accountfactory.createAccount("SAVINGS", Long.toString(accountNumber), initialDeposit);
        }
        else{
            account = accountfactory.createAccount("CHECKING", Long.toString(accountNumber), initialDeposit);
        }

        //set daily and current limit
        account.setDailyLimit(10000);
        account.setCurrentlimit(10000);
        //append customer to CSV
        csvReadWrite.addUser(customer);
        //append account to CSV
        csvReadWrite.addAccount(account,username);

        //add to usermap
        bank.getUserMap().put(tempUser.getUserID(), tempUser.getPin());

        System.out.println("Account number " + accountNumber + " has successfully been created!");
        System.out.println("===========================================================");
    }

    /**
    * Creates a new account for customer and adds to the account list
    * @param customer the current customer
    */
    public static void createAccount(Customer customer){
        double initialDeposit = 0;
        boolean flag = false;
        int choice = -1;
        Account account = null;

        // choose account type
        do{
            flag = false;
            try{
                System.out.println("===========================================================");
                System.out.println("Open an account");
                System.out.println("1)Savings Account");
                System.out.println("2)Checking Account");
                System.out.println("===========================================================");
                System.out.print("Enter Choice: ");
                if(input.hasNextInt()) 
                {
                   choice = input.nextInt();
                }
                input.nextLine();

                if (choice > 2 || choice < 1){
                    throw new IllegalArgumentException("Please select 1 or 2");
                }
                else{
                    flag = true;
                }
            }
            catch(IllegalArgumentException e){
                System.out.println(e);
            }
        } while (flag != true);

        // initial deposit
        do{
            flag = false;
            try{
                if(choice == 1){
                    System.out.println("You have selected Savings Account");
                }
                else{
                    System.out.println("You have selected checking Account");
                }
                System.out.print("Enter Initial amount: ");
                initialDeposit = input.nextDouble();
                input.nextLine();

                if(initialDeposit <= 0){
                    throw new InvalidAmountException();
                }
                else{
                    flag = true;
                }
            }
            catch(InvalidAmountException e){
            }
        } while (flag != true);

        long accountNumber = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        //System.out.println(accountNumber);
        if (choice == 1){
            account = accountfactory.createAccount("SAVINGS", Long.toString(accountNumber), initialDeposit);
        }
        else{
            account = accountfactory.createAccount("CHECKING", Long.toString(accountNumber), initialDeposit);
        }
        //set daily and current limit
        account.setDailyLimit(10000);
        account.setCurrentlimit(10000);
        //add account to CSV
        try {
            csvReadWrite.addAccount(account,customer.getUserID());
            bank.getCustomer().addAccount(account);
            System.out.println("Account number " + accountNumber + " has successfully been created!");
        } catch (IOException e) {
            System.out.println(e);
        }
        promptEnterKey();
    }

    /**
    * select from all account from customer and returns the index of account selected
    * @return returns the index of selected account
    */
    public static int selectAccount(){
        int choice = -1;
        if (bank.getCustomer().getAccount().size() == 0)
        {
            throw new IllegalArgumentException("Customer does not have an account!");
        }
        
        while(true){
            try{
                System.out.println("Select Account");
                System.out.println("===========================================================");
                String leftAlignFormat = "| %-2s | %-18s | %-15s | %-15s |%n";
    
                System.out.format("+----+--------------------+-----------------+-----------------+%n");
                System.out.format("| ID | Account Type       | Account number  | Balance         |%n");
                System.out.format("+----+--------------------+-----------------+-----------------+%n");
                for (int i = 0; i < bank.getCustomer().getAccount().size(); i++) {
                    System.out.format(leftAlignFormat, i+1, bank.getCustomer().getAccount(i).getAccountType(), bank.getCustomer().getAccount(i).getAccountNumber(), df.format(bank.getCustomer().getAccount(i).getBalance()));
                    System.out.format("+----+--------------------+-----------------+-----------------+%n");
                }
                System.out.print("Select An Account: ");
                if(input.hasNextInt()) 
                {
                   choice = input.nextInt();
                }
                input.nextLine();

                if (choice < 1 || choice > bank.getCustomer().getAccount().size()){
                    throw new IllegalArgumentException("Please select an account");
                }
                else{
                    return choice - 1;
                }
            }
            catch(IllegalArgumentException e){
                System.out.println(e);
            }
        }
    }
    
    /**
    * select account excluding the parameter account
    * @param currentaccount  Account to exclude
    * @return returns the index of selected account
    */
    public static int selectAccount(Account currentaccount){
        int choice= 1;
        if (bank.getCustomer().getAccount().size() == 0)
        {
            throw new IllegalArgumentException("Customer does not have other accounts!");
        }
        
        while(true){
            try{
                System.out.println("Select Account");
                System.out.println("===========================================================");
                String leftAlignFormat = "| %-2s | %-18s | %-15s | %-15s |%n";
    
                System.out.format("+----+--------------------+-----------------+-----------------+%n");
                System.out.format("| ID | Account Type       | Account number  | Balance         |%n");
                System.out.format("+----+--------------------+-----------------+-----------------+%n");
                for (int i = 0; i < bank.getCustomer().getAccount().size(); i++) {
                    if(Objects.equals(bank.getCustomer().getAccount(i), currentaccount))
                        continue;
                    if(i >= bank.getCustomer().getAccount().indexOf(currentaccount)){
                        System.out.format(leftAlignFormat, i, bank.getCustomer().getAccount(i).getAccountType(), bank.getCustomer().getAccount(i).getAccountNumber(), df.format(bank.getCustomer().getAccount(i).getBalance()));
                    }
                    else{
                        System.out.format(leftAlignFormat, i+1, bank.getCustomer().getAccount(i).getAccountType(), bank.getCustomer().getAccount(i).getAccountNumber(), bank.getCustomer().getAccount(i).getBalance());
                    }
                    System.out.format("+----+--------------------+-----------------+-----------------+%n");
                }
                System.out.print("Select An Account: ");
                if(input.hasNextInt()) 
                {
                   choice = input.nextInt();
                }
                input.nextLine();

                if (choice < 1 || choice > bank.getCustomer().getAccount().size() - 1){
                    throw new IllegalArgumentException("Please select an account");
                }
                else{
                    if(choice <= bank.getCustomer().getAccount().indexOf(currentaccount))
                        return choice -1;
                    else
                        return choice;
                }
            }
            catch(IllegalArgumentException e){
                System.out.println(e);
            }
        }
    }

    /**
    * Menu to change text color
    * @throws IllegalArgumentException
    */
    public static void changeColorMenu(){
        System.out.println("===========================================================");
        System.out.println("Select a color");
        System.out.println("1) White");
        System.out.println("2) Green");
        System.out.println("3) Yellow");
        System.out.println("4) Cyan");
        System.out.println("5) Red");
        System.out.println("6) Purple");
        System.out.println("===========================================================");

        int userInput = 0;
        System.out.print("your selection:");
        if(input.hasNextInt()) 
        {
            userInput = input.nextInt();
        }
        input.nextLine();

        switch (userInput) {
            case 1:
                System.out.print(Unicode.TEXT_RESET);
                break;
            case 2:
                System.out.print(Unicode.TEXT_GREEN);
                break;
            case 3:
                System.out.print(Unicode.TEXT_YELLOW);
                break;
            case 4:
                System.out.print(Unicode.TEXT_CYAN);
                break;
            case 5:
                System.out.print(Unicode.TEXT_RED);
                break;
            case 6:
                System.out.print(Unicode.TEXT_PURPLE);
                break;
            default:
                throw new IllegalArgumentException("Please select a valid input");
        }
    }
    
    /**
     * Prompts the user to hit ENTER
     */
     public static void promptEnterKey(){
         System.out.print("Press \"ENTER\" to continue...");
         input = new Scanner(System.in);
         input.nextLine();
      }
}
