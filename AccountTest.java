import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AccountTest {

    @BeforeClass
    @DisplayName("STATIC INITIALIZER BEFORE THE TEST CASE")
    public static void setUpBeforeClass() throws Exception {
    }

    @org.junit.jupiter.api.BeforeEach
    @DisplayName("RUNS BEFORE EACH METHOD EXECUTION")
    void setUp() {
        System.out.println("EXECUTING A JUNIT TEST FILE");
    }

    @org.junit.jupiter.api.AfterEach
    @DisplayName("RUNS AFTER EACH METHOD EXECUTION")
    void tearDown() {
        System.out.println("SUCCESSFUL EXECUTION OF A JUNIT TEST FILE");
    }

    @AfterClass
    @DisplayName("TO CLEANUP ANY SETUP DONE")
    public static void cleanUpAfterClass() throws Exception {
        System.out.println("CLEANUP FINISHED");
    }

    @Test
    @DisplayName("SHOULD CALL FOR THE RETRIEVAL OF THE DAILY LIMIT")
    void testIfSetDailyLimitWorked() {
        AccountFactory accountFactory = new AccountFactory();
        double dailyLimit = 3000;
        Account tempAccount = accountFactory.createAccount("CHECKING", "1231242", 52525, dailyLimit, 1000,2000,2000);
        assertEquals(3000,tempAccount.getDailyLimit());
    }

    @Test
    @DisplayName("SHOULD CALL FOR THE DEPOSIT FUNCTION AND UPDATE BALANCE ACCORDINGLY TO FUNDS DEPOSITED")
    void testIfDepositFunctionWorked() throws InvalidAmountException {
        AccountFactory accountFactory = new AccountFactory();
        Account tempAccount = accountFactory.createAccount("CHECKING", "1231242", 52525, 3000, 1000,2000,2000);
        tempAccount.deposit(5000);
        assertEquals(57525,tempAccount.getBalance());
    }

    @Test
    @DisplayName("SHOULD CALL FOR THE DEPOSIT FUNCTION AND THROW ERROR DUE TO NEGATIVE AMOUNT ENTERED")
    void testIfDepositFunctionWithNegativeValueDoesNotWork() throws InvalidAmountException {
        AccountFactory accountFactory = new AccountFactory();
        Account tempAccount = accountFactory.createAccount("CHECKING", "1231242", 52525, 3000, 1000,2000,2000);
        
        Exception exception = assertThrows(InvalidAmountException.class, () -> {
            tempAccount.deposit(-5000);
        });
        
    }

    @org.junit.jupiter.api.Test
    @DisplayName("SHOULD CALL FOR THE WITHDRAW FUNCTION AND UPDATE BALANCE ACCORDINGLY TO FUNDS WITHDRAWN")
    void testIfWithdrawFunctionWorked() throws InsufficientFundsException, OverLimitException {
        AccountFactory accountFactory = new AccountFactory();
        Account tempAccount = accountFactory.createAccount("CHECKING", "1231242", 52525, 3000, 6000,2000,2000);
        Exception exception = assertThrows(OverLimitException.class, () -> {
            tempAccount.withdraw(7000);
        });
        assertEquals(52525,tempAccount.getBalance()); //Balance same due to error thrown
    }
    
    /*@org.junit.jupiter.api.Test
    @DisplayName("SHOULD CHECK IF THE ACCOUNT BALANCE IS UPDATED ACCORDINGLY TO THE INTEREST ACCRUED")
    void testIfCompoundInterestWorked() throws InterestNotCalculatedException {

        String date = new SimpleDateFormat("yyyy-MM-dd").format("1992-02-33");
        String str[] = date .split("-");
        int day = Integer.parseInt(str[2]);
        int month = Integer.parseInt(str[1]);
        int year= Integer.parseInt(str[0]);
        AccountFactory accountFactory = new AccountFactory();
        Account tempAccount = accountFactory.createAccount("CHECKING", "1231242", 52525, day, month,year,2000);

        //tempAccount.compoundInterest();
        assertEquals(0,tempAccount.getOverseaslLimit());
    }
    */

}