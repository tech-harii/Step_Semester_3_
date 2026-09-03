public class exp4 {

    static String normalizeCode(String raw) {
        String trimmed = raw.trim();

        if (trimmed.length() < 3) {
            return trimmed.toUpperCase();
        }

        String firstThree = trimmed.substring(0, 3).toUpperCase();
        String rest = trimmed.substring(3);

        return firstThree + rest;
    }

    static String validateAndFormat(String code) {
        if (code.length() != 13) {
            return "Invalid: code must be exactly 13 characters";
        }

        String publisherCode = code.substring(0, 3);
        String body = code.substring(3);

        for (int i = 0; i < publisherCode.length(); i++) {
            if (!Character.isLetter(publisherCode.charAt(i))) {
                return "Invalid: publisher code must be 3 letters";
            }
        }

        for (int i = 0; i < body.length(); i++) {
            if (!Character.isDigit(body.charAt(i))) {
                return "Invalid: remaining 10 characters must be digits";
            }
        }

        String year = code.substring(3, 7);
        String catalog = code.substring(7, 13);

        StringBuilder display = new StringBuilder();
        display.append("[").append(publisherCode).append("] YEAR: ").append(year);
        display.append(" | CATALOG: ").append(catalog);

        return display.toString();
    }

    public static void main(String[] args) {
        System.out.println("Test 1:");
        String normalized1 = normalizeCode(" pen2026004251 ");
        System.out.println(validateAndFormat(normalized1));

        System.out.println("\nTest 2:");
        String normalized2 = normalizeCode("12N2026004251");
        System.out.println(validateAndFormat(normalized2));
    }
}