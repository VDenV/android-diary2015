package ua.org.sedu.diary.anroiddiary.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.json.JSONException;

import ua.org.sedu.diary.anroiddiary.R;
import ua.org.sedu.diary.anroiddiary.exception.MessageDialogException;
import ua.org.sedu.diary.anroiddiary.util.DefaultAsyncTask;
import ua.org.sedu.diary.anroiddiary.util.DefaultWebViewClient;
import ua.org.sedu.diary.anroiddiary.util.MessageDialogAsyncCallback;
import ua.org.sedu.diary.anroiddiary.widget.FormEditText;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.AndroidRuntimeException;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * @author voinovdenys
 */
public class RequestAccountScreen extends AbstractDiaryActivity implements OnEditorActionListener,
        OnFocusChangeListener {

    private static final String BIRTHDAY_DATE_FORMAT = "dd/MM/yyyy";
    private static final SparseArray<String> REGISTARTION_PARAM_MAP = new SparseArray<String>();
    static {
        REGISTARTION_PARAM_MAP.put(R.id.fatherFirstAndMiddleName, "fatherFirstAndMiddleName");
        REGISTARTION_PARAM_MAP.put(R.id.fatherLastName, "fatherLastName");
        REGISTARTION_PARAM_MAP.put(R.id.pupilFirstAndMiddleName, "pupilFirstAndMiddleName");
        REGISTARTION_PARAM_MAP.put(R.id.pupilLastName, "pupilLastName");
        REGISTARTION_PARAM_MAP.put(R.id.childBirthday, "pupilDateOfBirth");
        REGISTARTION_PARAM_MAP.put(R.id.schoolCitySpinner, "town");
        REGISTARTION_PARAM_MAP.put(R.id.school, "school");
        REGISTARTION_PARAM_MAP.put(R.id.classGroupSpinner, "classGroup");
        REGISTARTION_PARAM_MAP.put(R.id.addressField, "address");
        REGISTARTION_PARAM_MAP.put(R.id.emailAddress, "email");
        REGISTARTION_PARAM_MAP.put(R.id.phoneNumber, "phoneNumber");
    }

    private Dialog rulesDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.request_account_screen;
    }

    @Override
    protected int getSpinnerItemLayout() {
        return R.layout.registration_spinner_item;
    }

    @Override
    protected void init() {
        new DefaultAsyncTask(createProgressBar(), new MessageDialogAsyncCallback<String>(this) {

            @Override
            public String doInBackground() throws MessageDialogException {
                return getRemoteCallService().getRegistrationComboValues();
            }

            @Override
            public void onSuccess(String comboJson) {
                initSpinners(comboJson);
            }

        }).execute();
        setFormFieldListeners();
    }

    private String loadRules() {
        InputStream is = null;
        try {
            is = getAssets().open("rules.html");
            return IOUtils.toString(is);
        } catch (IOException e) {
            throw new AndroidRuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    private void initSpinners(String comboJson) {
        try {
            List<String> classGroups = getListFromJson(comboJson, "classGroups");
            List<String> cities = getListFromJson(comboJson, "cities");
            initClassGroupSpinner(classGroups);
            initSchoolCitySpinner(cities);
        } catch (JSONException e) {
            showJsonErrorDialog(e);
        }
    }

    private void initClassGroupSpinner(List<String> schools) {
        initSpinner(R.id.classGroupSpinner, schools);
    }

    private void initSchoolCitySpinner(List<String> cities) {
        initSpinner(R.id.schoolCitySpinner, cities);
    }

    private void initSpinner(int spinnerId, List<String> values) {
        Spinner spinner = getSpinner(spinnerId);
        spinner.setAdapter(createSimpleAdapter(values));
    }

    private void setFormFieldListeners() {
        for (View field : getFormFields()) {
            setFormEditFieldListeners(field);
            setBirthdayFieldFocusListener(field);
        }
    }

    private void setFormEditFieldListeners(View field) {
        if (isFormEditField(field)) {
            FormEditText formEditField = getFormEditField(field);
            formEditField.setOnEditorActionListener(this);
            formEditField.setOnFocusChangeListener(this);
            setHideKeyboardListener(formEditField);
        }
    }

    private void setBirthdayFieldFocusListener(View field) {
        if (field.getId() == R.id.childBirthday) {
            field.setOnFocusChangeListener(this);
        }
    }

    private List<View> getFormFields() {
        List<View> formFields = new ArrayList<View>();
        ViewGroup requestAccountForm = ((ViewGroup) findViewById(R.id.accountRequstForm));
        populateFormFieldsList(formFields, requestAccountForm);
        return formFields;
    }

    private void populateFormFieldsList(List<View> formFields, ViewGroup requestAccountForm) {
        for (int i = 0; i < requestAccountForm.getChildCount(); i++) {
            View view = requestAccountForm.getChildAt(i);
            if (view != null && view.getId() != View.NO_ID) {
                formFields.add(view);
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        return actionId != EditorInfo.IME_ACTION_NEXT || !validateFormField(textView);
    }

    private boolean validateFormField(TextView textView) {
        FormEditText formEditText = (FormEditText) textView;
        return formEditText.testValidity();
    }

    /**
     * Called when child birthday field is clicked.
     * 
     * @param birthdayField
     */
    public void onChildBirthdateClick(final View birthdayField) {
        new DatePickerDialog(this, new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, monthOfYear, dayOfMonth);
                setTextViewValue(birthdayField, new DateTime(cal.getTimeInMillis()).toString(BIRTHDAY_DATE_FORMAT));
            }
        }, 2012, 2, 1).show();
    }

    @Override
    public void onFocusChange(View field, boolean hasFocus) {
        validateFormField(field, hasFocus);
        showDateDialogIfBirthdayFieldIsFocused(field, hasFocus);
    }

    private void validateFormField(View field, boolean hasFocus) {
        if (!hasFocus && isFormEditField(field)) {
            validateFormField(getFormEditField(field));
        }
    }

    private void showDateDialogIfBirthdayFieldIsFocused(View field, boolean hasFocus) {
        if (hasFocus && field.getId() == R.id.childBirthday) {
            onChildBirthdateClick(field);
        }
    }

    /**
     * Action on send registration form button
     * 
     * @param button
     */
    public void onSendRegistration(View button) {
        if (validateAllField()) {
            showRulesDialog();
        } else {
            showErrorDialog(R.string.MESSAGE_NOT_ALL_FIELDS_FILLED);
        }
    }

    private boolean validateAllField() {
        boolean allValid = true;
        for (View field : getFormFields()) {
            allValid = allValid && validateField(field);
        }
        return allValid;
    }

    private void showRulesDialog() {
        Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.RULES_DIALOG_TITLE);
        builder.setPositiveButton(R.string.ACCEPT_RULES_BUTTON_LABEL, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onAcceptRules();
            }
        });
        builder.setNegativeButton(R.string.DECLINE_RULES_BUTTON_LABEL, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDialog(R.string.TITLE_RULES_REJECTED, R.string.MESSAGE_RULES_REJECTED);
            }
        });
        WebView webView = new WebView(this);
        webView.setWebViewClient(new DefaultWebViewClient(createProgressBar()));
        webView.loadDataWithBaseURL(null, loadRules(), "text/html", "utf-8", null);
        builder.setView(webView).create().show();
    }

    /**
     * Called when accept button in the rules dialog is pressed.
     */
    public void onAcceptRules() {
        new DefaultAsyncTask(createProgressBar(), new MessageDialogAsyncCallback<String>(this) {

            @Override
            public String doInBackground() throws MessageDialogException {
                String response = getRemoteCallService().sendRegistrationRequest(getRequestFieldValues());
                processRegistrationResponse(response);
                return null;
            }

            @Override
            public void onSuccess(String response) {
                hideProgressDialog();
                showCustomDialog(R.string.TITLE_REGISTRATION_MESSAGE_SENT, R.string.MESSAGE_REGISTRATION_MESSAGE_SENT,
                        new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showLoginScreen();
                            }
                        });
            }

        }).execute();
    }

    private Map<String, String> getRequestFieldValues() {
        Map<String, String> params = new HashMap<String, String>();
        for (View view : getFormFields()) {
            if (view.getId() != View.NO_ID) {
                params.put(REGISTARTION_PARAM_MAP.get(view.getId()), getFieldTextValue(view.getId()));
            }
        }
        return params;
    }

    private void processRegistrationResponse(String response) throws MessageDialogException {
        try {
            if (!isSuccess(response)) {
                throw new MessageDialogException(this, R.string.MESSAGE_REGISTRATION_MESSAGE_FAILURE,
                        R.string.TITLE_REGISTRATION_MESSAGE_FAILURE);
            }
        } catch (JSONException e) {
            throwJsonReadingException(e);
        }
    }

    private void showLoginScreen() {
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
    }
}
