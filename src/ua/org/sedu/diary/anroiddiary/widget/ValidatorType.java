/**
 *
 */
package ua.org.sedu.diary.anroiddiary.widget;

import ua.org.sedu.diary.anroiddiary.R;
import ua.org.sedu.diary.anroiddiary.validator.AlphaNumericValidator;
import ua.org.sedu.diary.anroiddiary.validator.AlphaValidator;
import ua.org.sedu.diary.anroiddiary.validator.CreditCardValidator;
import ua.org.sedu.diary.anroiddiary.validator.DateValidator;
import ua.org.sedu.diary.anroiddiary.validator.DomainValidator;
import ua.org.sedu.diary.anroiddiary.validator.EmailValidator;
import ua.org.sedu.diary.anroiddiary.validator.IpAddressValidator;
import ua.org.sedu.diary.anroiddiary.validator.NumericValidator;
import ua.org.sedu.diary.anroiddiary.validator.PhoneValidator;
import ua.org.sedu.diary.anroiddiary.validator.Validator;
import ua.org.sedu.diary.anroiddiary.validator.WebUrlValidator;

/**
 * Type of the validator for the FormEditText field.
 * 
 * @author voinovdenys
 */
public enum ValidatorType {

    TEST_NOCHECK(0),

    TEST_NUMERIC(1, R.string.ERROR_ONLY_NUMERIC_DIGITS_ALLOWED, NumericValidator.class),

    TEST_ALPHA(2, R.string.ERROR_ONLY_STANDARD_LETTERS_ARE_ALLOWED, AlphaValidator.class),

    TEST_ALPHANUMERIC(3, R.string.ERROR_THIS_FIELD_CANNOT_CONTAIN_SPECIAL_CHARACTER, AlphaNumericValidator.class),

    TEST_EMAIL(4, R.string.ERROR_EMAIL_ADDRESS_NOT_VALID, EmailValidator.class),

    TEST_CREDITCARD(5, R.string.ERROR_CREDITCARD_NUMBER_NOT_VALID, CreditCardValidator.class),

    TEST_PHONE(6, R.string.ERROR_PHONE_NOT_VALID, PhoneValidator.class),

    TEST_DOMAINNAME(7, R.string.ERROR_DOMAIN_NOT_VALID, DomainValidator.class),

    TEST_IPADDRESS(8, R.string.ERROR_IP_NOT_VALID, IpAddressValidator.class),

    TEST_WEBURL(9, R.string.ERROR_URL_NOT_VALID, WebUrlValidator.class),

    TEST_REGEXP(10),

    TEST_CUSTOM(11),

    TEST_DATE(12, R.string.ERROR_DATE_NOT_VALID, DateValidator.class);

    private int id;

    private int defaultErrorMessageId;

    private Class<? extends Validator> validatorClass;

    private ValidatorType(int id) {
        this.id = id;
    }

    private ValidatorType(int id, int defaultErrorMessageId, Class<? extends Validator> validatorClass) {
        this.id = id;
        this.defaultErrorMessageId = defaultErrorMessageId;
        this.validatorClass = validatorClass;
    }

    public int getId() {
        return id;
    }

    public static ValidatorType getById(int validatorId) {
        for (ValidatorType type : values()) {
            if (type.getId() == validatorId) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("Unsupported validator id: %s", validatorId));
    }

    public Class<? extends Validator> getValidatorClass() {
        return validatorClass;
    }

    public int getMessageId() {
        return defaultErrorMessageId;
    }

}
