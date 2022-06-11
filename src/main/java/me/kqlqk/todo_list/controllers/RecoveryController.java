package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.EmailSenderService;
import me.kqlqk.todo_list.service.UserService;
import me.kqlqk.todo_list.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/recovery")
public class RecoveryController {
    private final UserService userService;
    private final EmailSenderService emailSenderService;
    private final Map<Integer, String> recoveryIdsEmails;

    @Autowired
    public RecoveryController(UserService userService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;
        recoveryIdsEmails = new HashMap<>();
    }

    @GetMapping
    public String showRecoveryPage(Model model){
        model.addAttribute("userValid", new UserValidation());
        return "recovery-pages/recovery";
    }

    @PostMapping
    public String sendEmail(@ModelAttribute("userValid") UserValidation userValidation){
        if(userService.getByEmail(userValidation.getEmail()) != null){
            int tempId = (int) (Math.random() * 99999);
            recoveryIdsEmails.put(tempId, userValidation.getEmail());

            emailSenderService.sendEmail(
                    userValidation.getEmail(),
                    "Password recovery",
                    "follow this link to reset your password: http://localhost:8080/recovery/" + tempId
                    );

            return "recovery-pages/successRecovery";
        }
        userValidation.setFormCorrect(false);
        return "recovery-pages/recovery";
    }

    @GetMapping("/{recoveryId}")
    public String showChangingPasswordPage(@PathVariable("recoveryId") int recoveryId, Model model){
        if(this.recoveryIdsEmails.get(recoveryId) == null){
            return "redirect:/";
        }
        model.addAttribute("recoveryId", recoveryId);
        model.addAttribute("userValid", new UserValidation());
        return "recovery-pages/changePassword";
    }

    @PostMapping("/{recoveryId}")
    public String changePassword(@ModelAttribute("userValid") @Valid UserValidation userValidation, BindingResult bindingResult, @PathVariable int recoveryId){
        if(bindingResult.hasErrors() || !userValidation.getPassword().equals(userValidation.getConfirmPassword())){
            return "recovery-pages/changePassword";
        }

        User user = userService.getByEmail(recoveryIdsEmails.get(recoveryId));
        user.setPassword(userValidation.getPassword());
        userService.update(user);

        recoveryIdsEmails.remove(recoveryId);

        return "recovery-pages/passwordWasChanged";
    }

}
