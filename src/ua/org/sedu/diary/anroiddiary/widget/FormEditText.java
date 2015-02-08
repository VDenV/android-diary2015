package ua.org.sedu.diary.anroiddiary.widget;

import ua.org.sedu.diary.anroiddiary.validator.Validator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * EditText Extension to be used in order to create forms in android.
 * 
 * @author Andrea Baccega <me@andreabaccega.com>
 */
public class FormEditText extends EditText {

    private DefaultEditTextValidator editTextValidator;

    public FormEditText(Context context) {
        super(context);
        // FIXME how should this constructor be handled
        throw new RuntimeException("Not supported");
    }

    public FormEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        editTextValidator = new DefaultEditTextValidator(this, attrs, context);
    }

    public FormEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        editTextValidator = new DefaultEditTextValidator(this, attrs, context);
    }

    /**
     * Add a validator to this FormEditText. The validator will be added in the
     * queue of the current validators.
     * 
     * @param theValidator
     * @throws IllegalArgumentException
     *             if the validator is null
     */
    public void addValidator(Validator theValidator) throws IllegalArgumentException {
        editTextValidator.addValidator(theValidator);
    }

    protected DefaultEditTextValidator getEditTextValidator() {
        return editTextValidator;
    }

    /**
     * Calling *testValidity()* will cause the EditText to go through
     * customValidators and call {@link #Validator.isValid(EditText)}
     * 
     * @return true if the validity passes false otherwise.
     */
    public boolean testValidity() {
        return editTextValidator.testValidity();
    }

}
