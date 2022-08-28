package integration.me.kqlqk.todo_list.service.impl;

import integration.me.kqlqk.todo_list.IntegrationServiceParent;
import me.kqlqk.todo_list.service.impl.EmailSenderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailSenderServiceImplTest extends IntegrationServiceParent {

    @Autowired
    private EmailSenderServiceImpl emailSenderService;


    @Test
    public void sendEmail() {
        emailSenderService.sendEmail("anySubject", "any text", "user@mail.com", "admin@mail.com");
    }
}