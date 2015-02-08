package ua.org.sedu.diary.anroiddiary.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ua.org.sedu.diary.anroiddiary.R;
import ua.org.sedu.diary.anroiddiary.exception.MessageDialogException;
import ua.org.sedu.diary.anroiddiary.session.Credentials;
import ua.org.sedu.diary.anroiddiary.session.UserSessionHolder;
import ua.org.sedu.diary.anroiddiary.util.DefaultAsyncTask;
import ua.org.sedu.diary.anroiddiary.util.MessageDialogAsyncCallback;
import android.content.Intent;
import android.view.View;

/**
 * @author voinovdenys
 */
public class LoginScreen extends AbstractDiaryActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.login_screen;
    }

    @Override
    protected int getProgressBarMessageId() {
        return R.string.LOGIN_PROGRESS_BAR_MESSAGE;
    }

    @Override
    protected void init() {
        setHideKeyboardListeners();
        initWithCredentials();
    }

    private void setHideKeyboardListeners() {
        setHideKeyboardListener(getTextView(R.id.login));
        setHideKeyboardListener(getTextView(R.id.password));
    }

    private void initWithCredentials() {
        if (hasCredentials()) {
            final Credentials credentials = loadCredentials();
            if (!credentials.isLogout()) {
                fillLoginField(credentials);
                performLogin(credentials);
            }
        }
    }

    private boolean hasCredentials() {
        return getLoginService().hasCredentials();
    }

    private Credentials loadCredentials() {
        return getLoginService().loadCredentials();
    }

    private void fillLoginField(final Credentials credentials) {
        setTextViewValue(R.id.login, credentials.getLogin());
        setTextViewValue(R.id.password, credentials.getPassword());
    }

    /**
     * Executes login action
     * 
     * @param loginButton
     */
    public void onLogin(View loginButton) {
        hideKeyboard(getTextView(R.id.login));
        hideKeyboard(getTextView(R.id.password));

        if (credentialsAreEmpty()) {
            showErrorDialog(R.string.MESSAGE_NOT_ALL_FIELDS_FILLED);
        } else {
            performLogin(createCredentials());
        }
    }

    private boolean credentialsAreEmpty() {
        return checkTextViewIsEmpty(R.id.login) || checkTextViewIsEmpty(R.id.password);
    }

    private Credentials createCredentials() {
        return new Credentials(getTextViewValue(R.id.login), getTextViewValue(R.id.password));
    }

    private void performLogin(final Credentials credentials) {
        new DefaultAsyncTask(createProgressBar(), new MessageDialogAsyncCallback<String>(this) {

            @Override
            public String doInBackground() throws MessageDialogException {
                login(credentials);
                if (isChecked(R.id.rememberMe)) {
                    saveCredentials(credentials);
                }
                return getParentChildren();
            }

            @Override
            public void onSuccess(String parentChildrenJson) {
                processSuccessfulLogin(parentChildrenJson);
            }

        }).execute();
    }

    private void login(Credentials credentials) throws MessageDialogException {
        getLoginService().loginOnServer(credentials);
    }

    private void saveCredentials(final Credentials credentials) {
        getLoginService().saveCredentials(credentials);
    }

    private String getParentChildren() throws MessageDialogException {
        String userUid = UserSessionHolder.getUserUid();
        return getRemoteCallService().getParentChildren(userUid);
    }

    private void processSuccessfulLogin(String parentChildrenJson) {
        try {
            processChildrenResponse(parentChildrenJson);
        } catch (JSONException e) {
            showJsonErrorDialog(e);
        }
    }

    private void processChildrenResponse(String parentChildrenJson) throws JSONException {
        JSONArray children = getJsonDataArray(parentChildrenJson);
        if (children.length() == 1) {
            showChildDiary(children);
        } else if (children.length() > 1) {
            showChildSelectionScreen(children);
        } else {
            showErrorDialog(R.string.NO_CHILDREN_FOR_PARENT_MSG);
        }
    }

    private void showChildDiary(JSONArray children) throws JSONException {
        JSONObject child = children.getJSONObject(0);
        Intent intent = new Intent(this, DiaryScreen.class);
        intent.putExtra(DiaryScreen.CHILD_ID, child.getString("id"));
        intent.putExtra(DiaryScreen.PUPIL_NAME, child.getString("shortName"));
        intent.putExtra(DiaryScreen.PUPIL_CLASS, child.getString("classGroup"));
        startActivity(intent);
    }

    private void showChildSelectionScreen(JSONArray children) {
        Intent intent = new Intent(this, ChildSelectionScreen.class);
        intent.putExtra(ChildSelectionScreen.CHILDREN_JSON, children.toString());
        startActivity(intent);
    }

    /**
     * Registration form button event listener
     */
    public void showRegistrationForm(View view) {
        Intent intent = new Intent(this, RequestAccountScreen.class);
        startActivity(intent);
    }

}
