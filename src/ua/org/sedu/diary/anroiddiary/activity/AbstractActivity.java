/**
 *
 */
package ua.org.sedu.diary.anroiddiary.activity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ua.org.sedu.diary.anroiddiary.R;
import ua.org.sedu.diary.anroiddiary.exception.MessageDialogException;
import ua.org.sedu.diary.anroiddiary.util.JsonUtil;
import ua.org.sedu.diary.anroiddiary.util.Logger;
import ua.org.sedu.diary.anroiddiary.util.Msg;
import ua.org.sedu.diary.anroiddiary.util.StringUtils;
import ua.org.sedu.diary.anroiddiary.widget.FormEditText;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * Basic activity to be extended by every activity. Contains common methods used
 * by every activity.
 * 
 * @author voinovdenys
 */
public abstract class AbstractActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        init();
    }

    protected ProgressDialog createProgressBar() {
        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage(getString(getProgressBarMessageId()));
        return progress;
    }

    protected int getProgressBarMessageId() {
        return R.string.LOADING_PROGRESS_BAR_MESSAGE;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Integer optionsMenuLayoutId = getOptionsMenuLayoutId();
        return optionsMenuLayoutId != null ? createMenu(menu, optionsMenuLayoutId) : false;
    }

    protected Integer getOptionsMenuLayoutId() {
        return null;
    }

    private boolean createMenu(Menu menu, Integer optionsMenuLayoutId) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(optionsMenuLayoutId, menu);
        return true;
    }

    /**
     * Initialize screen. Called on activity creation.
     */
    protected abstract void init();

    /**
     * Returns main context layout id.
     * 
     * @return
     */
    protected abstract int getLayoutId();

    protected String getIntentParam(String param) {
        return getIntent().getExtras().getString(param);
    }

    protected void showDialog(int titleMsgId, String message, int iconId) {
        Msg.showDialog(this, getString(titleMsgId), message, iconId);
    }

    protected void showDialog(int titleId, int messageId) {
        Msg.showDialog(this, getString(titleId), getString(messageId));
    }

    protected void showCustomDialog(int titleId, int messageId, OnClickListener onClickListener) {
        Msg.showCustomDialog(this, getString(titleId), getString(messageId), onClickListener);
    }

    protected void showErrorDialog(int msgId) {
        Msg.showErrorDialog(this, getString(msgId));
    }

    protected void showServerUnvailableDialog() {
        Msg.showErrorDialog(this, getString(R.string.SERVER_UNAVAILABLE_MSG));
    }

    protected void showServerResponseErrorDialog() {
        Msg.showErrorDialog(this, getString(R.string.CANNOT_READ_SERVER_RESPONSE_MSG));
    }

    protected void showErrorDialog(int headerMsgId, String message) {
        Msg.showErrorDialog(this, getString(headerMsgId), message);
    }

    protected JSONObject getJsonData(String json) throws JSONException {
        return JsonUtil.getJsonData(json);
    }

    protected boolean isSuccess(String json) throws JSONException {
        return JsonUtil.isSuccess(json);
    }

    protected JSONArray getJsonDataArray(String json) throws JSONException {
        return JsonUtil.getJsonDataArray(json);
    }

    protected List<String> getListFromJson(String json, String listName) throws JSONException {
        return JsonUtil.getListFromJsonData(json, listName);
    }

    protected TextView getTextView(View parent, int textViewId) {
        return (TextView) parent.findViewById(textViewId);
    }

    protected TextView getTextView(int textViewId) {
        return (TextView) findViewById(textViewId);
    }

    protected Spinner getSpinner(View screen, int spinnerId) {
        return (Spinner) screen.findViewById(spinnerId);
    }

    protected boolean isChecked(int rememberme) {
        CheckBox checkBox = (CheckBox) findViewById(rememberme);
        return checkBox.isChecked();
    }

    protected void setTextViewValue(int textViewId, CharSequence value) {
        getTextView(textViewId).setText(value);
    }

    protected void setTextViewValue(View view, CharSequence value) {
        ((TextView) view).setText(value);
    }

    protected Spinner getSpinner(int spinnerId) {
        return (Spinner) findViewById(spinnerId);
    }

    protected String getFieldTextValue(int fieldId) {
        View field = findViewById(fieldId);
        if (field instanceof TextView) {
            return StringUtils.get((TextView) field);
        } else if (field instanceof Spinner) {
            return getSpinner(field).getSelectedItem().toString();
        }
        return null;
    }

    private Spinner getSpinner(View view) {
        return (Spinner) view;
    }

    protected ViewFlipper getViewFlipper(int viewFlipperId) {
        return (ViewFlipper) findViewById(viewFlipperId);
    }

    protected ListView getListView(int listViewId) {
        return (ListView) findViewById(listViewId);
    }

    protected ListView getListView(View parent, int listViewId) {
        return (ListView) parent.findViewById(listViewId);
    }

    protected String getTextViewValue(int textViewId) {
        return StringUtils.get(getTextView(textViewId));
    }

    public View getViewFromParent(View parent, int id) {
        return parent.findViewById(id);
    }

    protected boolean checkTextViewIsEmpty(int textViewId) {
        TextView textView = getTextView(textViewId);
        return StringUtils.isEmpty(textView);
    }

    protected void showJsonErrorDialog(JSONException e) {
        Logger.error(e.getMessage(), e);
        Msg.showErrorDialog(this, getString(R.string.ERROR_READING_JSON_RESPONSE_MSG));
    }

    protected void throwJsonReadingException(JSONException e) throws MessageDialogException {
        throw new MessageDialogException(this, R.string.ERROR_READING_JSON_RESPONSE_MSG, e);
    }

    protected boolean isFormEditField(View field) {
        return field instanceof FormEditText;
    }

    protected FormEditText getFormEditField(View field) {
        return (FormEditText) field;
    }

    protected boolean validateField(View field) {
        if (isFormEditField(field)) {
            return getFormEditField(field).testValidity();
        } else if (isSpinner(field)) {
            return getSpinner(field).getSelectedItem() != null;
        }
        throw new IllegalArgumentException("Not supported field type for validation.");
    }

    protected boolean isSpinner(View field) {
        return field instanceof Spinner;
    }

    protected ArrayAdapter<String> createSimpleAdapter(List<String> values) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, getSpinnerItemLayout(), values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    protected int getSpinnerItemLayout() {
        return R.layout.spinner_item;
    }

    protected void setHideKeyboardListener(final TextView textView) {
        textView.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });
    }

    protected void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
