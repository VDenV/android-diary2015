package ua.org.sedu.diary.anroiddiary.validator;

import java.util.regex.Pattern;

import android.widget.EditText;

/**
 * Base class for regexp based validators.
 * 
 * @see DomainValidator
 * @see EmailValidator
 * @see IpAddressValidator
 * @see PhoneValidator
 * @see WebUrlValidator
 * @see RegexpValidator
 * @author Andrea Baccega <me@andreabaccega.com>
 */
public class PatternValidator extends Validator {

    private Pattern pattern;

    public PatternValidator(String customErrorMessage, Pattern pattern) {
        super(customErrorMessage);
        if (pattern == null) {
            throw new IllegalArgumentException("_pattern must not be null");
        }
        this.pattern = pattern;
    }

    @Override
    public boolean isValid(EditText et) {
        return pattern.matcher(et.getText()).matches();
    }

}
