package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.dto.UserDTO;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.EmailSenderService;
import me.kqlqk.todo_list.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(RecoveryController.class);

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
        logger.info("was get request to /recovery");
        model.addAttribute("userValid", new UserDTO());
        return "recovery-pages/recovery";
    }

    @PostMapping
    public String sendEmail(@ModelAttribute("userValid") UserDTO userDTO){
        logger.info("was post request to /recovery");
        if(userService.getByEmail(userDTO.getEmail().toLowerCase()) != null){
            int tempId = (int) (Math.random() * 99999);
            recoveryIdsEmails.put(tempId, userDTO.getEmail());

            emailSenderService.sendEmail(
                    userDTO.getEmail(),
                    "Password recovery",
                    "follow this link to reset your password: http://localhost:8080/recovery/" + tempId
                    );

            return "recovery-pages/successRecovery";
        }
        userDTO.setFormCorrect(false);
        return "recovery-pages/recovery";
    }

    @GetMapping("/{recoveryId}")
    public String showChangingPasswordPage(@PathVariable("recoveryId") int recoveryId, Model model){
        logger.info("was get request to /recovery/" + recoveryId);
        if(this.recoveryIdsEmails.get(recoveryId) == null){
            return "redirect:/";
        }
        model.addAttribute("recoveryId", recoveryId);
        model.addAttribute("userValid", new UserDTO());
        return "recovery-pages/changePassword";
    }

    @PostMapping("/{recoveryId}")
    public String changePassword(@ModelAttribute("userValid") @Valid UserDTO userDTO, BindingResult bindingResult, @PathVariable int recoveryId){
        logger.info("was post request to /recovery/" + recoveryId);

        if(bindingResult.hasErrors() || !userDTO.getPassword().equals(userDTO.getConfirmPassword())){
            return "recovery-pages/changePassword";
        }

        User user = userService.getByEmail(recoveryIdsEmails.get(recoveryId).toLowerCase());
        user.setPassword(userDTO.getPassword());
        userService.update(user);

        recoveryIdsEmails.remove(recoveryId);

        return "recovery-pages/passwordWasChanged";
    }

}
