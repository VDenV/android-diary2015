package ua.org.sedu.diary.anroiddiary.validator;

import android.widget.EditText;

/**
 * It's a validator that applies the "NOT" logical operator to the validator passed in the constructor.
 * 
 * @author Andrea Baccega <me@andreabaccega.com>
 */
public class NotValidator extends Validator {

    private Validator validator;

    public NotValidator(String errorMessage, Validator validator) {
        super(errorMessage);
        this.validator = validator;
    }

    @Override
    public boolean isValid(EditText editText) {
        return !validator.isValid(editText);
    }

}
