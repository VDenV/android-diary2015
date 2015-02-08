package ua.org.sedu.diary.anroiddiary.validator;

import java.util.regex.Pattern;

import android.os.Build;
import android.util.Patterns;

/**
 * Validates a web url in the format:
 * scheme + authority + path
 * 
 * @author Andrea Baccega <me@andreabaccega.com>
 */
public class WebUrlValidator extends PatternValidator {

    public WebUrlValidator(String customErrorMessage) {
        super(customErrorMessage, Build.VERSION.SDK_INT >= 8 ? Patterns.WEB_URL : Pattern.compile(".*"));
    }
}
