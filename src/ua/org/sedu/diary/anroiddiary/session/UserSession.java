/**
 *
 */
package ua.org.sedu.diary.anroiddiary.session;

/**
 * @author voinovdenys
 */
public class UserSession {

    private final Credentials credentials;

    private final String basicUrl;

    private final String userUid;

    UserSession(Credentials credentials, String basicUrl, String userUid) {
        this.credentials = credentials;
        this.basicUrl = basicUrl;
        this.userUid = userUid;
    }

    String getLogin() {
        return credentials.getLogin();
    }

    String getPassword() {
        return credentials.getPassword();
    }

    String getBasicUrl() {
        return basicUrl;
    }

    String getUserUid() {
        return userUid;
    }

    public Credentials getCredentials() {
        return credentials;
    }

}
