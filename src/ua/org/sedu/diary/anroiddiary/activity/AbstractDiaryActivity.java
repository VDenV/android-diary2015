/**
 *
 */
package ua.org.sedu.diary.anroiddiary.activity;

import ua.org.sedu.diary.anroiddiary.services.LoginService;
import ua.org.sedu.diary.anroiddiary.services.RemoteCallService;

/**
 * Class to be extended by all activities in diary project.
 * 
 * @author voinovdenys
 */
abstract class AbstractDiaryActivity extends AbstractActivity {

    private RemoteCallService remoteCallService = new RemoteCallService(this);
    private LoginService loginService = new LoginService(this);

    protected RemoteCallService getRemoteCallService() {
        return remoteCallService;
    }

    protected LoginService getLoginService() {
        return loginService;
    }

}
