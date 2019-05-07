package oktenweb.try_register_with_email.services;

public interface MailService {
    String send(String email, String message);
}
