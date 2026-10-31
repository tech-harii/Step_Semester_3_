import java.util.Stack;

public class BannerPrinter {

    public static String expandBanner(String code) {
        Stack<Integer> countStack = new Stack<>();
        Stack<StringBuilder> textStack = new Stack<>();

        StringBuilder current = new StringBuilder();
        int count = 0;

        for (char ch : code.toCharArray()) {

            if (Character.isDigit(ch)) {
                // Handle multi-digit numbers
                count = count * 10 + (ch - '0');
            }

            else if (ch == '[') {
                countStack.push(count);
                textStack.push(current);

                count = 0;
                current = new StringBuilder();
            }

            else if (ch == ']') {
                int repeat = countStack.pop();
                StringBuilder previous = textStack.pop();

                for (int i = 0; i < repeat; i++) {
                    previous.append(current);
                }

                current = previous;
            }

            else {
                current.append(ch);
            }
        }

        return current.toString();
    }

    public static void main(String[] args) {
        System.out.println(expandBanner("3[ab]2[c]"));
        System.out.println(expandBanner("2[x3[y]]z"));
    }
}