/**
 *
 */
package ua.org.sedu.diary.anroiddiary.remote;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import ua.org.sedu.diary.anroiddiary.session.Credentials;
import ua.org.sedu.diary.anroiddiary.session.UserSessionHolder;
import android.os.Build;
import android.util.Base64;

/**
 * @author voinovdenys
 */
public class DiaryHttpClient {

    private static final int DEFAULT_TIMEOUT = 5000;
    private static final int DEFAULT_RESPONSE_TIMEOUT = 30000;

    private static final String LOGIN_PAGE_URL = "http://www.diary.s-edu.org.ua:90/diaryLogin/external/login.html";
    private static final String REGISTRATION_BASE_URL = "http://www.diary.s-edu.org.ua:85/rm/registration/";
    private static final String REGISTRATION_COMBO_URL = REGISTRATION_BASE_URL + "get-form-combos.json";
    private static final String REGISTRATION_SUBMIT_URL = REGISTRATION_BASE_URL
            + "save-external-father-registration-request.json";

    private static final String GET_PARENT_CHILDREN_URL = "external/father/children.html";
    private static final String GET_CHILD_DATA_URL = "external/father/scores.html";
    private static final String GET_WEEKS_URL = "external/father/weeks.html";

    private static DefaultHttpClient httpClient;

    private static DefaultHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new DefaultHttpClient();
            setConnectionParams();
        }
        return httpClient;
    }

    private static void setConnectionParams() {
        HttpParams params = httpClient.getParams();
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        params.setBooleanParameter("http.authentication.preemptive", true);
        HttpConnectionParams.setConnectionTimeout(params, DEFAULT_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, DEFAULT_RESPONSE_TIMEOUT);
    }

    public static String login(final Credentials credentials) throws IOException {
        return executeBasicAuthorizedRequest(LOGIN_PAGE_URL, credentials, null);
    }

    private static String execute(HttpPost httpPost) throws IllegalStateException, IOException {
        HttpResponse httpResponse = getHttpClient().execute(httpPost);
        return IOUtils.toString(httpResponse.getEntity().getContent());
    }

    public static String getParentChildren(String userUid) throws IOException {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("fatherUid", userUid));

        return executeAuthorizedRequest(GET_PARENT_CHILDREN_URL, params);
    }

    public static String getChildData(String pupilId, String weekNumber, String year) throws IOException {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("pupilId", pupilId));
        params.add(new BasicNameValuePair("week", weekNumber));
        params.add(new BasicNameValuePair("year", year));
        params.add(new BasicNameValuePair("localeCountry", "uk_UA")); // TODO get locale from phone

        return executeAuthorizedRequest(GET_CHILD_DATA_URL, params);
    }

    private static String executeAuthorizedRequest(String url, List<NameValuePair> params) throws IOException {
        return executeBasicAuthorizedRequest(getPostLoginUrl(url), UserSessionHolder.getCredentials(), params);
    }

    private static String executeBasicAuthorizedRequest(String url, Credentials credentials, List<NameValuePair> params)
            throws IOException {
        authorizeClient(credentials);
        HttpPost httpPost = prepareAuthorizedPostRequest(url, params, credentials);
        String response = execute(httpPost);
        unauthorizeClient();
        return response;
    }

    private static void unauthorizeClient() {
        getHttpClient().getCredentialsProvider().clear();
    }

    private static void authorizeClient(Credentials credentials) {
        getHttpClient().getCredentialsProvider().setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_SCHEME),
                new UsernamePasswordCredentials(credentials.getLogin(), credentials.getPassword()));
    }

    private static HttpPost prepareAuthorizedPostRequest(String url, List<NameValuePair> params, Credentials credentials)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("user-agent", getUserAgent());
        httpPost.addHeader("Authorization", getAuthorizationHeader(credentials));
        if (params != null) {
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        }
        return httpPost;
    }

    private static String getUserAgent() {
        return String.format("Android %s; Device: %s %s", Build.VERSION.RELEASE, Build.MANUFACTURER, Build.PRODUCT);
    }

    private static String getPostLoginUrl(String url) {
        return UserSessionHolder.getBasicUrl().replace("localhost", "10.0.2.2") + url;
    }

    private static String getAuthorizationHeader(Credentials credentials) {
        String auth = String.format("%s:%s", credentials.getLogin(), credentials.getPassword());
        return String.format("Basic %s", Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP));
    }

    public static String getWeeks(String year) throws IOException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("localeCountry", "uk_UA")); // TODO get locale from phone
        params.add(new BasicNameValuePair("year", year));
        return executeAuthorizedRequest(GET_WEEKS_URL, params);
    }

    public static String getRegistrationComboValues() throws IllegalStateException, IOException {
        HttpPost httpPost = new HttpPost(REGISTRATION_COMBO_URL);
        httpPost.addHeader("user-agent", "Android " + Build.VERSION.RELEASE);
        return execute(httpPost);
    }

    public static String sendRegistrationRequest(Map<String, String> params) throws IllegalStateException, IOException {
        HttpPost httpPost = new HttpPost(REGISTRATION_SUBMIT_URL);
        httpPost.setEntity(encodeParams(params));
        httpPost.addHeader("user-agent", "Android " + Build.VERSION.RELEASE);
        return execute(httpPost);
    }

    private static UrlEncodedFormEntity encodeParams(Map<String, String> params) throws UnsupportedEncodingException {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (Entry<String, String> param : params.entrySet()) {
            nvps.add(new BasicNameValuePair(param.getKey(), param.getValue()));
        }
        return new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
    }
}
