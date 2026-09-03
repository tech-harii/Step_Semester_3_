public class Exp3 {
    static void findLongestStreak(String signalLog) {
        char longestColor = signalLog.charAt(0);
        int longestLength = 1;
        char currentColor = signalLog.charAt(0);
        int currentLength = 1;
        for (int i = 1; i < signalLog.length(); i++) {
            char c = signalLog.charAt(i);
            if (c == currentColor) {
                currentLength++;
            } else {
                currentColor = c;
                currentLength = 1;
            }
            if (currentLength > longestLength) {
                longestLength = currentLength;
                longestColor = currentColor;
            }
        }
        System.out.println("Longest Streak: '" + longestColor + "' repeated " + longestLength + " times");
    }
    public static void main(String[] args) {
        System.out.println("Test 1:");
        findLongestStreak("RRGGGYRR");
 
        System.out.println("\nTest 2:");
        findLongestStreak("RRRRYYGG");
    }
}
 