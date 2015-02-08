/**
 *
 */
package ua.org.sedu.diary.anroiddiary.services;

import org.json.JSONException;
import org.json.JSONObject;

import ua.org.sedu.diary.anroiddiary.R;
import ua.org.sedu.diary.anroiddiary.exception.MessageDialogException;
import ua.org.sedu.diary.anroiddiary.util.JsonUtil;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Default abstract service that every service must extends. Contains commonly used methods accross all services.
 * 
 * @author voinovdenys
 */
public abstract class AbstractService {

    private final Context context;

    public AbstractService(final Context context) {
        this.context = context;
    }

    protected Context getContext() {
        return context;
    }

    protected void startActivity(Intent intent) {
        getContext().startActivity(intent);
    }

    protected JSONObject getJsonData(String json) throws JSONException {
        return JsonUtil.getJsonData(json);
    }

    protected SharedPreferences getSharedPreferences(String name, int mode) {
        return context.getSharedPreferences(name, mode);
    }

    protected void throwJsonReadingException(JSONException e) throws MessageDialogException {
        throw new MessageDialogException(getContext(), R.string.ERROR_READING_JSON_RESPONSE_MSG, e);
    }
}
