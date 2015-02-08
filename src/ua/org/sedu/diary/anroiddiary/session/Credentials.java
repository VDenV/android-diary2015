/**
 *
 */
package ua.org.sedu.diary.anroiddiary.session;

/**
 * @author voinovdenys
 */
public final class Credentials {

    private final String login;

    private final String password;

    private boolean logout;

    public Credentials(String login, String password, boolean logout) {
        this(login, password);
        this.logout = logout;
    }

    public Credentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isLogout() {
        return logout;
    }

}
