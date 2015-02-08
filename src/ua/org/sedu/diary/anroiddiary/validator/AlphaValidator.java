package ua.org.sedu.diary.anroiddiary.validator;

public class AlphaValidator extends RegexpValidator {
    public AlphaValidator(String message) {
        super(message, "[a-zA-Z \\./-]*");
    }
}
