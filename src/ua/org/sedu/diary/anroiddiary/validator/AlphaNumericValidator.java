package ua.org.sedu.diary.anroiddiary.validator;

public class AlphaNumericValidator extends RegexpValidator {
    public AlphaNumericValidator(String message) {
        super(message, "[a-zA-Z0-9 \\./-]*");
    }

}
