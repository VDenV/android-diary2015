package ua.org.sedu.diary.anroiddiary.validator;

import android.widget.EditText;

/**
 * The or validator checks if one of passed validators is returning true.<br/>
 * Note: the message that will be shown is the one passed to the Constructor
 * 
 * @author Andrea B.
 */
public class OrValidator extends MultiValidator {

    public OrValidator(String message, Validator... validators) {
        super(message, validators);
    }

    @Override
    public boolean isValid(EditText et) {
        for (Validator v : getValidators()) {
            if (v.isValid(et)) {
                return true;
            }
        }
        return false;
    }

}
