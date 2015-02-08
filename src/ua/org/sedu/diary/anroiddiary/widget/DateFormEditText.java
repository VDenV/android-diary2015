/**
 *
 */
package ua.org.sedu.diary.anroiddiary.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * Date form field with validation and formatting options.
 * 
 * @author voinovdenys
 */
public class DateFormEditText extends FormEditText {

    private static final String DATE_SEPARATOR = "/";
    private TextWatcher textWatcher = new TextWatcher() {

        private boolean isDelete = false;

        @Override
        public void onTextChanged(CharSequence string, int start, int before, int count) {
            if (!TextUtils.isEmpty(string) && getError() != null) {
                setError(null);
            }
            isDelete = count < 0;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // do nothing
        }

        @Override
        public void afterTextChanged(Editable string) {
            String datePattern = getEditTextValidator().getDatePattern();
            if (datePattern != null && datePattern.contains(DATE_SEPARATOR)) {
                int totalLength = 0;
                String[] dateParts = datePattern.split(DATE_SEPARATOR);
                for (int i = 0; i < dateParts.length - 1; i++) {
                    totalLength += dateParts[i].length();
                    if (string.length() == totalLength) {
                        string.append(DATE_SEPARATOR);
                    }
                    totalLength++;
                }
                // TODO fix deletion
            }
        }
    };

    public DateFormEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addTextChangedListener(textWatcher);
    }

    public DateFormEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTextChangedListener(textWatcher);
    }

    public DateFormEditText(Context context) {
        super(context);
        addTextChangedListener(textWatcher);
    }

}
