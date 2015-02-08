/**
 *
 */
package ua.org.sedu.diary.anroiddiary.util;

import ua.org.sedu.diary.anroiddiary.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * Utility for showing messages in the dialog.
 * 
 * @author voinovdenys
 */
public final class Msg {

    private Msg() {
        // restrict instantiation
    }

    public static void showDialog(final Context context, final String title, final String message, final Integer icon) {

        Builder builder = new AlertDialog.Builder(context).setMessage(message).setTitle(title)
                .setNeutralButton("OK", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        if (icon != null) {
            builder.setIcon(icon);
        }
        builder.create().show();
    }

    public static void showCustomDialog(final Context context, final String title, final String message,
            OnClickListener onClickListener) {
        new AlertDialog.Builder(context).setMessage(message).setTitle(title).setNeutralButton("OK", onClickListener)
                .create().show();
    }

    public static void showErrorDialog(final Context context, final String message) {
        showErrorDialog(context, context.getString(R.string.ERROR_DIALOG_DEFAULT_TITLE), message); // TODO add error icon
    }

    public static void showErrorDialog(final Context context, final String title, final String message) {
        showDialog(context, title, message, android.R.drawable.ic_dialog_alert); // TODO add error icon
    }

    public static void showDialog(final Context context, final String title, final String message) {
        showDialog(context, title, message, null);
    }
}
