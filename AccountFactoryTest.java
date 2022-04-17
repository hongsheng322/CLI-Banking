import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountFactoryTest {

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
    @DisplayName("SHOULD CHECK IF THE ACCOUNT WITH 5 VARIABLES IS SUCCESSFULLY CREATED WITH NO NULL VALUES")
    void testForCreationOfAccountWithFiveVariables() {
        AccountFactory accountFactory = new AccountFactory();
        assertNotNull(accountFactory.createAccount("CHECKING", "1231242", 52525, 3000, 1000,2000,2000));
    }

    @Test
    @DisplayName("SHOULD CHECK IF THE ACCOUNT WITH 3 VARIABLES IS SUCCESSFULLY CREATED WITH NO NULL VALUES")
    void testForCreationOfAccountWithThreeVariables() {
        AccountFactory accountFactory = new AccountFactory();
        assertNotNull(accountFactory.createAccount("CHECKING", "1231242", 52525));
    }

}