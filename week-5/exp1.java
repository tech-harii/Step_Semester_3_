class LibraryMember {
    private String membershipPin;
    String branchCode;          // default access
    protected double finesOwed;
    public String displayName;
    public LibraryMember() {
    }
}
public class exp1 {
    static String classifyAccess(String fieldModifier,
                                 String accessorContext) {
        switch (fieldModifier) {
            case "private":
                return accessorContext.equals("SAME_CLASS")
                        ? "ALLOWED" : "DENIED";
            case "default":
                return accessorContext.equals("SAME_CLASS")
                        || accessorContext.equals("SAME_PACKAGE")
                        ? "ALLOWED" : "DENIED";
            case "protected":
                return accessorContext.equals("DIFFERENT_PACKAGE")
                        ? "DENIED" : "ALLOWED";
            case "public":
                return "ALLOWED";
            default:
                return "DENIED";
        }
    }
    static String summarizeByModifier(String[][] attempts) {
        String[] modifiers = {"private", "default",
                              "protected", "public"};
        int[] allowed = new int[4];
        int[] denied = new int[4];
        for (String[] attempt : attempts) {
            String modifier = attempt[0];
            String context = attempt[1];
            int index = -1;
            for (int i = 0; i < modifiers.length; i++) {
                if (modifiers[i].equals(modifier)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                if (classifyAccess(modifier, context).equals("ALLOWED")) {
                    allowed[index]++;
                } else {
                    denied[index]++;
                }
            }
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < modifiers.length; i++) {
            if (i > 0) {
                result.append(" | ");
            }
            result.append(modifiers[i])
                  .append(": ")
                  .append(allowed[i])
                  .append(" allowed / ")
                  .append(denied[i])
                  .append(" denied");
        }
        return result.toString();
    }
    public static void main(String[] args) {
        System.out.println(
            classifyAccess("private", "SAME_CLASS")
        );
        System.out.println(
            classifyAccess("protected", "DIFFERENT_PACKAGE")
        );
        String[][] attempts = {
            {"private", "SAME_CLASS"},
            {"private", "SAME_PACKAGE"},
            {"default", "SAME_PACKAGE"},
            {"default", "DIFFERENT_PACKAGE"},
            {"protected", "SAME_PACKAGE"},
            {"protected", "SAME_CLASS"},
            {"public", "DIFFERENT_PACKAGE"}
        };
        System.out.println(summarizeByModifier(attempts));
    }
}