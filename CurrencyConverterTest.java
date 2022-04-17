import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CurrencyConverterTest {

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
        CurrencyConverter converter = new CurrencyConverter();

        assertEquals(1355.8632,converter.convertCurrency(1000,"USD","SGD"));
    }
    

}
