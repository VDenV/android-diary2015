/**
 *
 */
package ua.org.sedu.diary.anroiddiary.session;

import android.util.AndroidRuntimeException;

/**
 * @author voinovdenys
 */
public final class UserSessionHolder {

    private UserSessionHolder() {
        // restrict instantiation
    }

    private static UserSession userSession;

    public static void setUserSession(final Credentials credentials, final String basicUrl, final String userUid) {
        UserSessionHolder.userSession = new UserSession(credentials, basicUrl, userUid);
    }

    public static boolean isLoggedIn() {
        return userSession != null;
    }

    public static String getUserUid() {
        if (isLoggedIn()) {
            return userSession.getUserUid();
        }
        throw new AndroidRuntimeException("User is not logged in.");
    }

    public static String getBasicUrl() {
        if (isLoggedIn()) {
            return userSession.getBasicUrl();
        }
        throw new AndroidRuntimeException("User is not logged in.");
    }

    public static String getLogin() {
        if (isLoggedIn()) {
            return userSession.getLogin();
        }
        throw new AndroidRuntimeException("User is not logged in.");
    }

    public static String getPassword() {
        if (isLoggedIn()) {
            return userSession.getPassword();
        }
        throw new AndroidRuntimeException("User is not logged in.");
    }

    public static void removeUserSession() {
        userSession = null;
    }

    public static Credentials getCredentials() {
        return userSession.getCredentials();
    }
}
