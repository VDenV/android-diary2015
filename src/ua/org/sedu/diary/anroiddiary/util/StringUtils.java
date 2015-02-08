package ua.org.sedu.diary.anroiddiary.util;

import android.widget.TextView;

/**
 * Utility class for TextView utility methods
 * 
 * @author voinovdenys
 */
public final class StringUtils {

    private StringUtils() {
        // restrict instantiation
    }

    public static boolean isEmpty(TextView field) {
        if (field.getText() == null) {
            return false;
        }
        String value = field.getText().toString().trim();
        return value.length() == 0;
    }

    public static boolean isEqual(String expected, TextView field) {
        if (field.getText() == null) {
            return false;
        }
        String value = field.getText().toString().trim();
        return value.equals(expected);
    }

    public static String get(TextView field) {
        if (!isEmpty(field)) {
            return field.getText().toString().trim();
        }
        return "";
    }
}
