package me.kqlqk.todo_list.controllers.rest;

import me.kqlqk.todo_list.dto.RecoveryDTO;
import me.kqlqk.todo_list.exceptions_handling.exceptions.user.UserNotFoundException;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.EmailSenderService;
import me.kqlqk.todo_list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/recovery")
public class RecoveryRestController {
    private final UserService userService;
    private final EmailSenderService emailSenderService;
    private final Map<Integer, String> recoveryPageIdEmail;

    @Autowired
    public RecoveryRestController(UserService userService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;
        recoveryPageIdEmail = new HashMap<>();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> sendEmail(@RequestBody @Valid RecoveryDTO recoveryDTO){
        if(userService.getByEmail(recoveryDTO.getEmail()) == null){
            throw new UserNotFoundException("User with email = " + recoveryDTO.getEmail() + " not found");
        }

        int tempPageId = (int) (Math.random() * 99999);
        recoveryPageIdEmail.put(tempPageId, recoveryDTO.getEmail());

        emailSenderService.sendEmail(
                "Password recovery",
                "Follow the link to reset your password." +
                        " http://localhost:8080/api/recovery/" + tempPageId  +
                        " If you didn't request a restore, please ignore this message.",
                recoveryDTO.getEmail());

        Map<String, String> response = new HashMap<>();
        response.put("info", "A message with further actions has been sent to your email.");

        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{id}")
    public ResponseEntity<?> changePassword(@PathVariable int id,
                                            @RequestBody @Valid RecoveryDTO recoveryDTO){
        if(!recoveryPageIdEmail.containsKey(id)){
            return ResponseEntity.notFound().build();
        }

        User user = userService.getByEmail(recoveryPageIdEmail.get(id));
        user.setPassword(recoveryDTO.getPassword());
        userService.update(user);

        recoveryPageIdEmail.remove(id);

        Map<String, String> response = new HashMap<>();
        response.put("info", "Password was successfully changed");

        return ResponseEntity.ok(response);
    }

    public Map<Integer, String> getRecoveryPageIdEmail() {
        return recoveryPageIdEmail;
    }
}
