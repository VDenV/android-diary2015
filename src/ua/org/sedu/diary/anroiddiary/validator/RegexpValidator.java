package ua.org.sedu.diary.anroiddiary.validator;

import java.util.regex.Pattern;

/**
 * Used for validating the user input using a regexp.
 * 
 * @author Andrea Baccega <me@andreabaccega.com>
 */
public class RegexpValidator extends PatternValidator {

    public RegexpValidator(String errorMessage, String regexp) {
        super(errorMessage, Pattern.compile(regexp));
    }
}
