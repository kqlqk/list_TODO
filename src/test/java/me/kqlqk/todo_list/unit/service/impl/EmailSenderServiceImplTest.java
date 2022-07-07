package me.kqlqk.todo_list.unit.service.impl;

import me.kqlqk.todo_list.service.impl.EmailSenderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;

import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
public class EmailSenderServiceImplTest {

    @Mock
    private EmailSenderServiceImpl emailSenderServiceImpl;

    @Test
    public void sendEmail_shouldSendEmail(){
        MailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("randomMail@todolist.org");
        mailMessage.setSubject("test Subject");
        mailMessage.setText("test text");

        emailSenderServiceImpl.sendEmail(mailMessage);

        verify(emailSenderServiceImpl, Mockito.times(1)).sendEmail(mailMessage);
    }
}
