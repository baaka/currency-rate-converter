package pl.cleankod.util.logging;

public class LogTailUtil {
    public static String maskLast(String value) {
        return maskLast(value, 4);
    }

    public static String maskLast(String value, int visibleChars) {
        if (value == null || value.isEmpty()) {
            return "";
        }

        String cleaned = value.replaceAll("\\s+", "");

        if (cleaned.length() < visibleChars) {
            return "****";
        }

        return "**** **** **** " + cleaned.substring(cleaned.length() - visibleChars);
    }
}
