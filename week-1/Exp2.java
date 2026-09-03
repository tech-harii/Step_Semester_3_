public class Exp2 {
    static void checkTypingAccuracy(String original, String typed) {
        int length = original.length();
        int matched = 0;
        int firstMismatchIndex = -1;
        for (int i = 0; i < length; i++) {
            char originalChar = original.charAt(i);
            char typedChar = typed.charAt(i);

            if (originalChar == typedChar) {
                matched++;
            } else if (firstMismatchIndex == -1) {
                firstMismatchIndex = i;
            }
        }
        double accuracy = ((double) matched / length) * 100;
        StringBuilder result = new StringBuilder();
        result.append("Matched: ").append(matched).append("/").append(length);
        result.append(" | Accuracy: ").append(String.format("%.2f", accuracy)).append("%");
        if (firstMismatchIndex == -1) {
            result.append(" | No Mismatches");
        } else {
            result.append(" | First Mismatch at position ").append(firstMismatchIndex + 1);
            result.append(" ('").append(original.charAt(firstMismatchIndex)).append("' vs '");
            result.append(typed.charAt(firstMismatchIndex)).append("')");
        }
        System.out.println(result.toString());
    }

    public static void main(String[] args) {
        System.out.println("Test 1:");
        checkTypingAccuracy("hello world", "hello worlt");

        System.out.println("\nTest 2:");
        checkTypingAccuracy("coding", "coding");
    }
}