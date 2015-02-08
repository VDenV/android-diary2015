/**
 *
 */
package ua.org.sedu.diary.anroiddiary.util;

import ua.org.sedu.diary.anroiddiary.exception.MessageDialogException;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * @author voinovdenys
 */
public abstract class MessageDialogAsyncCallback<T> {

    private final Context context;
    private ProgressDialog progress;

    public MessageDialogAsyncCallback(Context context) {
        this.context = context;
    }

    public abstract T doInBackground() throws MessageDialogException;

    /**
     * Shows message dialog with error.
     * 
     * @param e
     */
    public void onError(MessageDialogException e) {
        e.showError();
    }

    public void onSuccess(T result) {
        // Default implementation is empty
    }

    public Context getContext() {
        return context;
    }

    void setProgress(ProgressDialog progress) {
        this.progress = progress;
    }

    public void hideProgressDialog() {
        progress.dismiss();
    }

}
