/**
 *
 */
package ua.org.sedu.diary.anroiddiary.services;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import ua.org.sedu.diary.anroiddiary.activity.LoginScreen;
import ua.org.sedu.diary.anroiddiary.exception.MessageDialogException;
import ua.org.sedu.diary.anroiddiary.session.Credentials;
import ua.org.sedu.diary.anroiddiary.session.UserSessionHolder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * @author voinovdenys
 */
public class LoginService extends RemoteCallService {

    private static final String PREFERENCES = "DiaryPreferences";

    public LoginService(final Context context) {
        super(context);
    }

    public void loginOnServer(final Credentials credentials) throws MessageDialogException {
        try {
            String response = loginAndGetResponse(credentials);
            if (StringUtils.isNotEmpty(response)) {
                JSONObject data = getJsonData(response);
                validateLoginResponse(data);
                UserSessionHolder.setUserSession(credentials, data.getString("message"), data.getString("userUid"));
            }
        } catch (JSONException e) {
            throwJsonReadingException(e);
        }
    }

    private void validateLoginResponse(final JSONObject data) throws MessageDialogException, JSONException {
        if (data.getBoolean("failure")) {
            throw new MessageDialogException(getContext(), data.getString("message"));
        }
    }

    public void logout() {
        UserSessionHolder.removeUserSession();
        saveLogoutProperty();
        Intent intent = new Intent(getContext(), LoginScreen.class);
        startActivity(intent);
    }

    private void saveLogoutProperty() {
        SharedPreferences.Editor editor = getDiarySharedPreferencesEditor();
        editor.putBoolean("logout", true);
        editor.commit();
    }

    private SharedPreferences getDiarySharedPreferences() {
        return getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    public boolean hasCredentials() {
        SharedPreferences settings = getDiarySharedPreferences();
        String savedLogin = settings.getString("login", null);
        String savedPassword = settings.getString("password", null);
        return StringUtils.isNotEmpty(savedLogin) && StringUtils.isNotEmpty(savedPassword);
    }

    public void saveCredentials(final Credentials credentials) {
        SharedPreferences.Editor editor = getDiarySharedPreferencesEditor();
        editor.putString("login", credentials.getLogin());
        editor.putString("password", credentials.getPassword());
        editor.putBoolean("logout", false);
        editor.commit();
    }

    private SharedPreferences.Editor getDiarySharedPreferencesEditor() {
        return getDiarySharedPreferences().edit();
    }

    public Credentials loadCredentials() {
        SharedPreferences settings = getDiarySharedPreferences();
        String savedLogin = settings.getString("login", null);
        String savedPassword = settings.getString("password", null);
        boolean logout = settings.getBoolean("logout", false);
        return new Credentials(savedLogin, savedPassword, logout);
    }
}
