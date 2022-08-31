package me.kqlqk.todo_list.service;

import org.springframework.mail.MailMessage;
import org.springframework.stereotype.Component;

@Component
public interface EmailSenderService {
    void sendEmail(MailMessage mailMessage);
    void sendEmail(String subject, String text, String... to);

}
