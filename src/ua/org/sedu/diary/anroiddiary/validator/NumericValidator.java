package ua.org.sedu.diary.anroiddiary.validator;

import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;

/**
 * A validator that returns true only if the input field contains only numbers.
 * 
 * @author Andrea Baccega <me@andreabaccega.com>
 */
public class NumericValidator extends Validator {

    public NumericValidator(String customErrorMessage) {
        super(customErrorMessage);
    }

    @Override
    public boolean isValid(EditText editText) {
        Editable text = editText.getText();
        return TextUtils.isDigitsOnly(text);
    }
}
