import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CheckingAccountTest {

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
    @DisplayName("SHOULD CHECK IF THE RETURNED SAVINGS ACCOUNT DETAILS IS CORRECT")
    void testIfCanReturnCheckingAccountDetails() {
        CheckingAccount CheckingAccount = new CheckingAccount("124124",5333,1000,2000,3000,4000);

        assertNotNull(CheckingAccount.toString());
    }

    /*@Test
    @DisplayName("SHOULD CHECK IF THE RETURNED ACCOUNT DATA CAN BE PARSED TO CSV FORMAT")
    void testIfCanReturnCheckingAccountDataToCSV() {
        CheckingAccount CheckingAccount = new CheckingAccount(String.valueOf(123124),100000,1000,2000);
        assertEquals("123124',CHECKING,100000.0,1000.0,2000.0,0,0",CheckingAccount.toCSV());
    */
}