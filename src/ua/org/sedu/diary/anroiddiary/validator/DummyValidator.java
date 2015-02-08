package ua.org.sedu.diary.anroiddiary.validator;

import android.widget.EditText;

/**
 * This is a dummy validator. It just returns true on each input.
 * 
 * @author Andrea Baccega <me@andreabaccega.com>
 */
public class DummyValidator extends Validator {

    public DummyValidator() {
        super(null);
    }

    @Override
    public boolean isValid(EditText et) {
        return true;
    }
}
