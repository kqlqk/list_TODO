package me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailMessage;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {
    private final MailSender mailSender;

    @Autowired
    public EmailSenderServiceImpl(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(MailMessage mailMessage) {
        SimpleMailMessage simpleMessage = (SimpleMailMessage) mailMessage;

        if(simpleMessage.getSubject() == null || simpleMessage.getTo() == null){
            throw new MailSendException("'to' and 'text' cannot be null, \n" +
                    "to = " + Arrays.toString(simpleMessage.getTo()) + "\n" +
                    "text = " + simpleMessage.getText());
        }

        mailSender.send(simpleMessage);
    }

    @Override
    public void sendEmail(String subject, String text, String... to) {
        if(to == null || text == null){
            throw new MailSendException("'to' and 'text' cannot be null, \n" +
                    "to = " + to + "\n" +
                    "text = " + text);
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
