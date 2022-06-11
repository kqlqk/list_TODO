package me.kqlqk.todo_list.service;

import org.springframework.mail.MailMessage;

public interface EmailSenderService {
    void sendEmail(MailMessage mailMessage);
    void sendEmail(String to, String subject, String text);

}
