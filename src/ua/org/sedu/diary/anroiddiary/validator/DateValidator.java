/**
 *
 */
package ua.org.sedu.diary.anroiddiary.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ua.org.sedu.diary.anroiddiary.util.StringUtils;
import android.util.AndroidRuntimeException;
import android.widget.EditText;

/**
 * @author voinovdenys
 */
public class DateValidator extends Validator {

    private String dateFormat;

    public DateValidator(String errorMessage, String dateFormat) {
        super(errorMessage);
        validatePattern(dateFormat);
        this.dateFormat = dateFormat;
    }

    private void validatePattern(String datePattern) {
        try {
            new SimpleDateFormat(datePattern, Locale.getDefault());
        } catch (Exception e) {
            throw new AndroidRuntimeException("Date pattern is wrong: " + datePattern);
        }
    }

    @Override
    public boolean isValid(EditText textView) {
        try {
            new SimpleDateFormat(dateFormat, Locale.getDefault()).parse(StringUtils.get(textView));
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

}
