/**
 *
 */
package ua.org.sedu.diary.anroiddiary.exception;

import ua.org.sedu.diary.anroiddiary.R;
import ua.org.sedu.diary.anroiddiary.util.Msg;
import android.content.Context;

/**
 * @author voinovdenys
 */
public class MessageDialogException extends Exception {

    private static final long serialVersionUID = 1L;

    private final Context context;

    private final String message;

    private final String title;

    public MessageDialogException(Context context, int messageId) {
        this(context, context.getString(messageId));
    }

    public MessageDialogException(Context context, int messageId, Exception e) {
        super(e);
        this.context = context;
        this.message = context.getString(messageId);
        this.title = context.getString(R.string.ERROR_DIALOG_DEFAULT_TITLE);
    }

    public MessageDialogException(Context context, String message) {
        this.context = context;
        this.message = message;
        this.title = context.getString(R.string.ERROR_DIALOG_DEFAULT_TITLE);
    }

    public MessageDialogException(Context context, int messageId, int titleId) {
        this.context = context;
        this.message = context.getString(messageId);
        this.title = context.getString(titleId);
    }

    public void showError() {
        Msg.showErrorDialog(context, title, message);
    }

}
