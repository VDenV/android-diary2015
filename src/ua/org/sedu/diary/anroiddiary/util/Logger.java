/**
 *
 */
package ua.org.sedu.diary.anroiddiary.util;

import android.util.Log;

/**
 * @author voinovdenys
 * 
 */
public final class Logger {

    private static final String DIARY_LOG_ERROR = "DiaryLogError";
    private static final String DIARY_LOG = "DiaryLog";

    public static void error(String message, Throwable e) {
        Log.e(DIARY_LOG_ERROR, message, e);
    }

    public static void info(Object message) {
        Log.i(DIARY_LOG, message.toString());
    }
}
