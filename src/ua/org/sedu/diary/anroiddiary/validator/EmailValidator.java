package ua.org.sedu.diary.anroiddiary.validator;

import java.util.regex.Pattern;

import android.os.Build;
import android.util.Patterns;

/**
 * This validates an email using regexps.
 * Note that if an email passes the validation with this validator it doesn't mean it's a valid email - it means it's a
 * valid email <strong>format</strong>
 * 
 * @author Andrea Baccega <me@andreabaccega.com>
 */
public class EmailValidator extends PatternValidator {

    private static final String EMAIL_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+";

    public EmailValidator(String customErrorMessage) {
        super(customErrorMessage, getPattern());
    }

    private static Pattern getPattern() {
        return Build.VERSION.SDK_INT >= 8 ? Patterns.EMAIL_ADDRESS : Pattern.compile(EMAIL_PATTERN);
    }
}
