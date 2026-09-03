public class exp1 {

    static void checkPinLength(String pin) {
        if (pin.length() != 4) {
            System.out.println("Invalid PIN — must be exactly 4 digits.");
        } else {
            System.out.println("PIN length OK.");
        }
    }

    public static void main(String[] args) {
        System.out.println("Test 1:");
        checkPinLength("482");

        System.out.println("\nTest 2:");
        checkPinLength("4820");
    }
}