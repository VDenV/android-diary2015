package ua.org.sedu.diary.anroiddiary.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract class for a multivalidator.
 * 
 * @see AndValidator
 * @see OrValidator
 * @author Andrea Baccega <me@andreabaccega.com>
 */
public abstract class MultiValidator extends Validator {

    private final List<Validator> validators;

    public MultiValidator(String message, Validator... validators) {
        super(message);
        if (validators == null) {
            throw new NullPointerException("None of the validators are specified");
        }
        this.validators = Arrays.asList(validators);
    }

    public MultiValidator(String message) {
        super(message);
        this.validators = new ArrayList<Validator>();
    }

    public void enqueue(Validator newValidator) {
        validators.add(newValidator);
    }

    protected List<Validator> getValidators() {
        return validators;
    }

}
