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

import javax.servlet.http.HttpServletRequest;
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
    public RecoveryController(UserService userService, EmailSenderService emailSenderService){
        this.userService = userService;
        this.emailSenderService = emailSenderService;
        recoveryIdsEmails = new HashMap<>();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showRecoveryPage(HttpServletRequest request, Model model){
        model.addAttribute("userValid", new UserDTO());

        return "recovery-pages/recovery";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String sendEmail(@ModelAttribute("userValid") UserDTO userDTO, HttpServletRequest request){
        if(userService.getByEmail(userDTO.getEmail()) != null){
            int tempId = (int) (Math.random() * 99999);
            recoveryIdsEmails.put(tempId, userDTO.getEmail());

            //throws NullPointerException which is caught in LoggingAspect.aroundExceptionInControllersLoggingAdvice()
            emailSenderService.sendEmail(
                    userDTO.getEmail(),
                    "Password recovery",
                    "follow this link to reset your password: http://localhost:8080/recovery/" + tempId);

            return "recovery-pages/successRecovery";
        }
        userDTO.setFormCorrect(false);

        return "recovery-pages/recovery";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{recoveryId}")
    public String showChangingPasswordPage(@PathVariable("recoveryId") int recoveryId, HttpServletRequest request, Model model){
        if(this.recoveryIdsEmails.get(recoveryId) == null){
            return "redirect:/";
        }
        model.addAttribute("recoveryId", recoveryId);
        model.addAttribute("userValid", new UserDTO());

        return "recovery-pages/changePassword";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{recoveryId}")
    public String changePassword(@ModelAttribute("userValid") @Valid UserDTO userDTO, BindingResult bindingResult,
                                 HttpServletRequest request,
                                 @PathVariable int recoveryId){
        if(bindingResult.hasErrors() || !userDTO.getPassword().equals(userDTO.getConfirmPassword())){
            return "recovery-pages/changePassword";
        }

        User user = userService.getByEmail(recoveryIdsEmails.get(recoveryId));
        user.setPassword(userDTO.getPassword());
        userService.update(user);

        recoveryIdsEmails.remove(recoveryId);

        return "recovery-pages/passwordWasChanged";
    }

}
