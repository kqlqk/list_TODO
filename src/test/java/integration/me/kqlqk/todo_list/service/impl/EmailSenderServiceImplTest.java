package integration.me.kqlqk.todo_list.service.impl;

import annotations.TestService;
import me.kqlqk.todo_list.service.impl.EmailSenderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TestService
public class EmailSenderServiceImplTest {

    @Autowired
    private EmailSenderServiceImpl emailSenderService;


    @Test
    public void sendEmail() {
        emailSenderService.sendEmail("anySubject", "any text", "user@mail.com", "admin@mail.com");
    }
}