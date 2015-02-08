/**
 *
 */
package ua.org.sedu.diary.anroiddiary.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility class for JSON parsing common methods.
 * 
 * @author voinovdenys
 */
public final class JsonUtil {

    private static final String SUCCESS_OBJECT = "success";
    private static final String JSON_MAIN_DATA_OBJECT = "data";

    private JsonUtil() {
        // restrict instantiation
    }

    public static JSONObject getJsonData(String response) throws JSONException {
        JSONObject json = getJsonObject(response);
        return json.getJSONObject(JSON_MAIN_DATA_OBJECT);
    }

    public static boolean isSuccess(String response) throws JSONException {
        JSONObject json = getJsonObject(response);
        return json.getBoolean(SUCCESS_OBJECT);
    }

    public static JSONArray getJsonDataArray(String response) throws JSONException {
        JSONObject json = getJsonObject(response);
        return json.getJSONArray(JSON_MAIN_DATA_OBJECT);
    }

    public static List<String> getListFromJsonData(String response, String listName) throws JSONException {
        JSONObject data = getJsonData(response);
        JSONArray jsonArray = data.getJSONArray(listName);
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        return list;
    }

    private static JSONObject getJsonObject(String response) throws JSONException {
        if (response == null) {
            throw new JSONException("Empty server response");
        }
        return new JSONObject(response);
    }
}
