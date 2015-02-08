/**
 *
 */
package ua.org.sedu.diary.anroiddiary.util;

import ua.org.sedu.diary.anroiddiary.exception.MessageDialogException;

/**
 * @author voinovdenys
 */
public final class AsyncTaskResult {

    private MessageDialogException error;

    private Object result;

    private AsyncTaskResult(Object result) {
        this.result = result;
    }

    private AsyncTaskResult(MessageDialogException error) {
        this.error = error;
    }

    public boolean hasError() {
        return getError() != null;
    }

    public MessageDialogException getError() {
        return error;
    }

    public static AsyncTaskResult onSuccess(Object result) {
        return new AsyncTaskResult(result);
    }

    public static AsyncTaskResult onError(MessageDialogException e) {
        return new AsyncTaskResult(e);
    }

    public String getResult() {
        return result != null ? result.toString() : null;
    }

}
