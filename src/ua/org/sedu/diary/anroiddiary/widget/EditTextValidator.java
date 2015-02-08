package ua.org.sedu.diary.anroiddiary.widget;

import ua.org.sedu.diary.anroiddiary.validator.Validator;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Interface for encapsulating validation of an EditText control
 */
public interface EditTextValidator {

    /**
     * Add a validator to this FormEditText. The validator will be added in the
     * queue of the current validators.
     * 
     * @param theValidator
     * @throws IllegalArgumentException
     *             if the validator is null
     */
    public void addValidator(Validator theValidator) throws IllegalArgumentException;

    /**
     * This should be used with {@link #addTextChangedListener(TextWatcher)}. It
     * fixes the non-hiding error popup behaviour.
     */
    public TextWatcher getTextWatcher();

    public boolean isEmptyAllowed();

    /**
     * Resets the {@link Validator}s
     */
    public void resetValidators();

    /**
     * Calling *testValidity()* will cause the EditText to go through
     * customValidators and call {@link #Validator.isValid(EditText)}
     * 
     * @return true if the validity passes false otherwise.
     */
    public boolean testValidity();

}
