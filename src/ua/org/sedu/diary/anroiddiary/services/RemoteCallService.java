/**
 *
 */
package ua.org.sedu.diary.anroiddiary.services;

import java.io.IOException;
import java.util.Map;
import org.apache.http.conn.ConnectTimeoutException;

import android.content.Context;
import ua.org.sedu.diary.anroiddiary.R;
import ua.org.sedu.diary.anroiddiary.exception.MessageDialogException;
import ua.org.sedu.diary.anroiddiary.remote.DiaryHttpClient;
import ua.org.sedu.diary.anroiddiary.session.Credentials;

/**
 * @author voinovdenys
 */
public class RemoteCallService extends AbstractService {

    public RemoteCallService(final Context context) {
        super(context);
    }

    public String loginAndGetResponse(final Credentials credentials) throws MessageDialogException {
        return getJson(new JsonMethodCallback() {

            @Override
            public String executeMethod() throws IOException {
                return DiaryHttpClient.login(credentials);
            }
        });
    }

    public String getParentChildren(final String parentUid) throws MessageDialogException {
        return getJson(new JsonMethodCallback() {

            @Override
            public String executeMethod() throws IOException {
                return DiaryHttpClient.getParentChildren(parentUid);
            }
        });
    }

    public String getChildData(final String childId, final String weekNumber, final String year) throws MessageDialogException {
        return getJson(new JsonMethodCallback() {

            @Override
            public String executeMethod() throws IOException {
                return DiaryHttpClient.getChildData(childId, weekNumber, year);
            }
        });
    }

    public String getWeeks(final String year) throws MessageDialogException {
        return getJson(new JsonMethodCallback() {

            @Override
            public String executeMethod() throws IOException {
                return DiaryHttpClient.getWeeks(year);
            }
        });
    }

    public String getRegistrationComboValues() throws MessageDialogException {
        return getJson(new JsonMethodCallback() {

            @Override
            public String executeMethod() throws IOException {
                return DiaryHttpClient.getRegistrationComboValues();
            }
        });
    }

    public String sendRegistrationRequest(final Map<String, String> params) throws MessageDialogException {
        return getJson(new JsonMethodCallback() {

            @Override
            public String executeMethod() throws IOException {
                return DiaryHttpClient.sendRegistrationRequest(params);
            }
        });
    }

    private String getJson(final JsonMethodCallback callback) throws MessageDialogException {
        try {
            return callback.executeMethod();
        } catch (ConnectTimeoutException e) {
            throw new MessageDialogException(getContext(), R.string.SERVER_UNAVAILABLE_MSG, e);
        } catch (IOException e) {
            throw new MessageDialogException(getContext(), R.string.CANNOT_READ_SERVER_RESPONSE_MSG, e);
        }
    }

    /**
     * Interface for the callback to skip repeated exception handling for all
     * server json calls.
     *
     * @author voinovdenys
     */
    private interface JsonMethodCallback {

        /**
         * Execute method which returns json response from the server.
         *
         * @throws ConnectTimeoutException
         * @throws IOException
         */
        String executeMethod() throws IOException;
    }
}
