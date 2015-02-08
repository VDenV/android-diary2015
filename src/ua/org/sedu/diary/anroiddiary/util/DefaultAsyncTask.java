/**
 *
 */
package ua.org.sedu.diary.anroiddiary.util;

import ua.org.sedu.diary.anroiddiary.exception.MessageDialogException;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * @author voinovdenys
 */
public class DefaultAsyncTask extends AsyncTask<Void, Void, AsyncTaskResult> {

    private final ProgressDialog progress;
    private final MessageDialogAsyncCallback<String> callback;

    public DefaultAsyncTask(ProgressDialog progress, MessageDialogAsyncCallback<String> callback) {
        this.progress = progress;
        this.callback = callback;
        callback.setProgress(progress);
    }

    @Override
    protected void onPreExecute() {
        showProgressDailog();
        super.onPreExecute();
    }

    @Override
    protected AsyncTaskResult doInBackground(Void... params) {
        try {
            return AsyncTaskResult.onSuccess(callback.doInBackground());
        } catch (MessageDialogException e) {
            Logger.error(e.getMessage(), e);
            return AsyncTaskResult.onError(e);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
            return AsyncTaskResult.onError(wrapException(e));
        }
    }

    private MessageDialogException wrapException(Exception e) {
        return new MessageDialogException(callback.getContext(), e.getMessage());
    }

    @Override
    protected void onPostExecute(AsyncTaskResult result) {
        if (result.hasError()) {
            hideProgressDialog();
            callback.onError(result.getError());
        } else {
            callback.onSuccess(result.getResult());
            hideProgressDialog();
        }

    }

    private void showProgressDailog() {
        if (!progress.isShowing()) {
            progress.show();
        }
    }

    private void hideProgressDialog() {
        progress.dismiss();
    }

}
