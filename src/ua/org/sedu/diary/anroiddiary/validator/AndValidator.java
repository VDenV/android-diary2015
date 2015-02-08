package ua.org.sedu.diary.anroiddiary.validator;

import android.widget.EditText;

/**
 * The AND validator checks if all of the passed validators is returning true.<br/>
 * Note: the message that will be shown is the one of the first failing
 * validator
 * 
 * @author Andrea B.
 */
public class AndValidator extends MultiValidator {

    public AndValidator(Validator... validators) {
        super(null, validators);
    }

    public AndValidator() {
        super(null);
    }

    @Override
    public boolean isValid(EditText et) {
        for (Validator validator : getValidators()) {
            if (!validator.isValid(et)) {
                setErrorMessage(validator.getErrorMessage());
                return false;
            }
        }
        return true;
    }
}
