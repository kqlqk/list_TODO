package me.kqlqk.todo_list.controllers;

import me.kqlqk.todo_list.dto.RecoveryDTO;
import me.kqlqk.todo_list.models.User;
import me.kqlqk.todo_list.service.EmailSenderService;
import me.kqlqk.todo_list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/recovery")
public class RecoveryController {
    private final UserService userService;
    private final EmailSenderService emailSenderService;
    private final Map<Integer, String> recoveryPageIdEmail;

    @Autowired
    public RecoveryController(UserService userService, EmailSenderService emailSenderService){
        this.userService = userService;
        this.emailSenderService = emailSenderService;
        recoveryPageIdEmail = new HashMap<>();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showRecoveryPage(Model model){
        model.addAttribute("recoveryDTO", new RecoveryDTO());

        return "recovery-pages/recovery";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String sendEmail(@ModelAttribute("recoveryDTO") @Valid RecoveryDTO recoveryDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "recovery-pages/recovery";
        }

        if(userService.getByEmail(recoveryDTO.getEmail()) == null){
            recoveryDTO.setFormCorrect(false);
            return "recovery-pages/recovery";
        }

        int pageId = (int) (Math.random() * 99999);
        recoveryPageIdEmail.put(pageId, recoveryDTO.getEmail());

        emailSenderService.sendEmail(
                "Password recovery",
                "Follow the link to reset your password." +
                        " http://localhost:8080/recovery/" + pageId  +
                        " If you didn't request a restore, please ignore this message.",
                recoveryDTO.getEmail());

        return "recovery-pages/successRecovery";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{pageId}")
    public String showChangingPasswordPage(@PathVariable int pageId, Model model){
        if(this.recoveryPageIdEmail.get(pageId) == null){
            return "redirect:/";
        }

        model.addAttribute("pageId", pageId);
        model.addAttribute("recoveryDTO", new RecoveryDTO());

        return "recovery-pages/changePassword";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{pageId}")
    public String changePassword(@ModelAttribute("recoveryDTO") @Valid RecoveryDTO recoveryDTO, BindingResult bindingResult,
                                 @PathVariable int pageId){
        if(bindingResult.hasErrors() || !recoveryDTO.getPassword().equals(recoveryDTO.getConfirmPassword())){
            return "recovery-pages/changePassword";
        }

        User user = userService.getByEmail(recoveryPageIdEmail.get(pageId));
        user.setPassword(recoveryDTO.getPassword());
        userService.update(user);

        recoveryPageIdEmail.remove(pageId);

        return "recovery-pages/passwordWasChanged";
    }

}
