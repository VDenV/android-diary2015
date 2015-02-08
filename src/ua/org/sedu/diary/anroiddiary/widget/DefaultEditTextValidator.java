package ua.org.sedu.diary.anroiddiary.widget;

import ua.org.sedu.diary.anroiddiary.R;
import ua.org.sedu.diary.anroiddiary.validator.AndValidator;
import ua.org.sedu.diary.anroiddiary.validator.DateValidator;
import ua.org.sedu.diary.anroiddiary.validator.DummyValidator;
import ua.org.sedu.diary.anroiddiary.validator.EmptyValidator;
import ua.org.sedu.diary.anroiddiary.validator.MultiValidator;
import ua.org.sedu.diary.anroiddiary.validator.NotValidator;
import ua.org.sedu.diary.anroiddiary.validator.OrValidator;
import ua.org.sedu.diary.anroiddiary.validator.RegexpValidator;
import ua.org.sedu.diary.anroiddiary.validator.Validator;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Default implementation of an {@link EditTextValidator}
 */
public class DefaultEditTextValidator implements EditTextValidator {

    private TextWatcher textWatcher;

    /**
     * The custom validators setted using
     */
    protected MultiValidator multiValidator;

    protected String testErrorString;

    protected boolean emptyAllowed;

    protected EditText editText;

    protected ValidatorType testType;

    protected String classType;

    protected String customRegexp;

    protected String emptyErrorStringActual;

    protected String emptyErrorString;

    private String datePattern;

    private Context context;

    public DefaultEditTextValidator(EditText editText, AttributeSet attrs, Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FormEditText);
        emptyAllowed = typedArray.getBoolean(R.styleable.FormEditText_emptyAllowed, false);
        testType = getValidatorType(typedArray);
        testErrorString = typedArray.getString(R.styleable.FormEditText_testErrorString);
        classType = typedArray.getString(R.styleable.FormEditText_classType);
        customRegexp = typedArray.getString(R.styleable.FormEditText_customRegexp);
        emptyErrorString = typedArray.getString(R.styleable.FormEditText_emptyErrorString);
        datePattern = typedArray.getString(R.styleable.FormEditText_datePattern);
        typedArray.recycle();

        setEditText(editText);
        this.context = context;
    }

    private ValidatorType getValidatorType(TypedArray typedArray) {
        return ValidatorType.getById(getValidatorId(typedArray));
    }

    private int getValidatorId(TypedArray typedArray) {
        return typedArray.getInt(R.styleable.FormEditText_testType, getDefaultValidatorId());
    }

    private int getDefaultValidatorId() {
        return ValidatorType.TEST_NOCHECK.getId();
    }

    @Override
    public void addValidator(Validator validator) throws IllegalArgumentException {
        if (validator == null) {
            throw new IllegalArgumentException("Validator argument should not be null");
        }
        multiValidator.enqueue(validator);
    }

    public String getClassType() {
        return classType;
    }

    public String getCustomRegexp() {
        return customRegexp;
    }

    public EditText getEditText() {
        return editText;
    }

    public String getTestErrorString() {
        return testErrorString;
    }

    @Override
    public TextWatcher getTextWatcher() {
        if (textWatcher == null) {
            textWatcher = new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                    // not used
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // not used
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(s) && editText.getError() != null) {
                        editText.setError(null);
                    }
                }
            };
        }
        return textWatcher;
    }

    @Override
    public boolean isEmptyAllowed() {
        return emptyAllowed;
    }

    @Override
    public void resetValidators() {
        setEmptyErrorString(emptyErrorString);

        multiValidator = new AndValidator();
        Validator validator = getSelectedValidator();
        addValidator(createMultiValidator(validator));
    }

    private MultiValidator createMultiValidator(Validator validator) {
        MultiValidator tmpValidator;
        if (emptyAllowed) {
            tmpValidator = new OrValidator(validator.getErrorMessage(),
                    new NotValidator(null, new EmptyValidator(null)), validator);
        } else {
            // If the xml tells us that this is a required field, we will add the EmptyValidator.
            tmpValidator = new AndValidator();
            tmpValidator.enqueue(new EmptyValidator(emptyErrorStringActual));
            tmpValidator.enqueue(validator);
        }
        return tmpValidator;
    }

    private Validator getSelectedValidator() {
        Validator validator;
        switch (testType) {
            case TEST_NOCHECK:
                validator = new DummyValidator();
                break;
            case TEST_CUSTOM:
                validator = loadCustomClass();
                break;
            case TEST_REGEXP:
                validator = new RegexpValidator(testErrorString, customRegexp);
                break;
            case TEST_DATE:
                validator = new DateValidator(getErrorMessage(testType.getMessageId()), datePattern);
                break;
            default:
                validator = createValidatorFromClass(testType.getValidatorClass(),
                        getErrorMessage(testType.getMessageId()));
                break;
        }
        return validator;
    }

    private String getErrorMessage(int defaultErrorMessageId) {
        return TextUtils.isEmpty(testErrorString) ? context.getString(defaultErrorMessageId) : testErrorString;
    }

    private Validator loadCustomClass() {
        Class<? extends Validator> customValidatorClass = getCustomValidatorClass();
        return createValidatorFromClass(customValidatorClass, testErrorString);
    }

    private Validator createValidatorFromClass(Class<? extends Validator> customValidatorClass, String errorMessage) {
        Validator validator = null;
        try {
            validator = customValidatorClass.getConstructor(String.class).newInstance(errorMessage);
        } catch (Exception e) {
            throw new AndroidRuntimeException(String.format("Unable to construct custom validator (%s)",
                    customValidatorClass));
        }
        return validator;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Validator> getCustomValidatorClass() {
        validateMandatoryAttributes();
        Class<? extends Validator> customValidatorClass;
        try {
            Class<?> loadedClass = getClass().getClassLoader().loadClass(classType);
            assertValidatorClass(loadedClass);
            customValidatorClass = (Class<? extends Validator>) loadedClass;
        } catch (ClassNotFoundException e) {
            throw new AndroidRuntimeException(String.format("Unable to load class for custom validator (%s).",
                    classType));
        }
        return customValidatorClass;
    }

    private void assertValidatorClass(Class<?> loadedClass) {
        if (!Validator.class.isAssignableFrom(loadedClass)) {
            throw new RuntimeException(String.format("Custom validator (%s) does not extend %s", classType,
                    Validator.class.getName()));
        }
    }

    private void validateMandatoryAttributes() {
        if (classType == null) {
            throw new RuntimeException("Trying to create a custom validator but no classType has been specified.");
        }
        if (TextUtils.isEmpty(testErrorString)) {
            throw new RuntimeException(String.format(
                    "Trying to create a custom validator (%s) but no error string specified.", classType));
        }
    }

    public void setClassType(String classType, String testErrorString) {
        testType = ValidatorType.TEST_CUSTOM;
        this.classType = classType;
        this.testErrorString = testErrorString;
        resetValidators();
    }

    public void setCustomRegexp(String customRegexp) {
        testType = ValidatorType.TEST_REGEXP;
        this.customRegexp = customRegexp;
        resetValidators();
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
        this.editText.removeTextChangedListener(getTextWatcher());
        this.editText.addTextChangedListener(getTextWatcher());
    }

    public void setEmptyAllowed(boolean emptyAllowed) {
        this.emptyAllowed = emptyAllowed;
        resetValidators();
    }

    public void setEmptyErrorString(String emptyErrorString) {
        if (!TextUtils.isEmpty(emptyErrorString)) {
            emptyErrorStringActual = emptyErrorString;
        } else {
            emptyErrorStringActual = context.getString(R.string.ERROR_FIELD_MUST_NOT_BE_EMPTY);
        }
    }

    public void setTestErrorString(String testErrorString) {
        this.testErrorString = testErrorString;
        resetValidators();
    }

    public void setTestType(ValidatorType testType) {
        this.testType = testType;
        resetValidators();
    }

    /**
     * Calling *testValidity()* will cause the EditText to go through
     * customValidators and call {@link #Validator.isValid(EditText)}
     * 
     * @return true if the validity passes false otherwise.
     */
    @Override
    public boolean testValidity() {
        resetValidators();
        boolean isValid = multiValidator.isValid(editText);
        if (!isValid) {
            if (multiValidator.hasErrorMessage()) {
                editText.setError(multiValidator.getErrorMessage());
            }
        }
        return isValid;
    }

    public String getDatePattern() {
        return datePattern;
    }

}
