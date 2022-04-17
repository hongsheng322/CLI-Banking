/**customer picks a choice that is out of range */
public class ArgumentOutOfRangeException extends Exception {
    public ArgumentOutOfRangeException(int min, int max){
        System.out.println("Please enter a valid input! Your choice must not be less than " + min + " or more than " + max + "!");
    }
}
