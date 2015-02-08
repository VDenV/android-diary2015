/**
 *
 */
package ua.org.sedu.diary.anroiddiary.exception;

import ua.org.sedu.diary.anroiddiary.R;
import android.util.AndroidException;

/**
 * @author voinovdenys
 */
public class LoginFailureException extends AndroidException {

    private static final long serialVersionUID = 1L;

    private static final int LOGIN_FAILURE_DIALOG_TITLE = R.string.LOGIN_FAILURE_WINDOW_HEADER;

    public LoginFailureException(String message) {
        super(message);
    }

    public static int getLoginFailureDialogTitle() {
        return LOGIN_FAILURE_DIALOG_TITLE;
    }
}
