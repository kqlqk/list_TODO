package unit.me.kqlqk.todo_list.service.impl;

import me.kqlqk.todo_list.service.impl.EmailSenderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailMessage;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailSenderServiceImplTest {
    @InjectMocks
    private EmailSenderServiceImpl emailSenderServiceImpl;

    @Mock
    private MailSender mailSender;


    @Test
    public void sendEmail_shouldSendsEmail(){
        emailSenderServiceImpl.sendEmail(generateValidMessage());

        verify(mailSender, times(1)).send((SimpleMailMessage) generateValidMessage());
    }

    @Test
    public void sendEmail_shouldThrowsMailSendException(){
        Assertions.assertThrows(MailSendException.class,
                () -> emailSenderServiceImpl.sendEmail("SubjectMayBeNull",null, ""));

        MailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo();
        mailMessage.setSubject(null);
        mailMessage.setText(null);

        Assertions.assertThrows(MailSendException.class, () -> emailSenderServiceImpl.sendEmail(mailMessage));
    }


    private MailMessage generateValidMessage(){
        MailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("randomMail@gmail.com");
        mailMessage.setSubject("test Subject");
        mailMessage.setText("test text");

        return mailMessage;
    }
}
