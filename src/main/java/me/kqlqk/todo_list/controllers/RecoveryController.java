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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents endpoints for guests
 */
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

    /**
     * Represents <b>"/recovery" [GET]</b> endpoint
     */
    @RequestMapping(method = RequestMethod.GET)
    public String getRecoveryPage(Model model){
        model.addAttribute("recoveryDTO", new RecoveryDTO());

        return "recovery-pages/recovery";
    }

    /**
     * Represents <b>"/recovery" [POST]</b> endpoint
     */
    @RequestMapping(method = RequestMethod.POST)
    public String sendEmail(@ModelAttribute("recoveryDTO") @Valid RecoveryDTO recoveryDTO,
                            BindingResult bindingResult,
                            HttpServletResponse response){
        if(bindingResult.hasErrors() || userService.getByEmail(recoveryDTO.getEmail()) == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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

    /**
     * Represents <b>"/recovery/{recovery id}" [GET]</b> endpoint
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{pageId}")
    public String getChangingPasswordPage(@PathVariable int pageId, Model model){
        if(this.recoveryPageIdEmail.get(pageId) == null){
            return "redirect:/";
        }

        model.addAttribute("pageId", pageId);
        model.addAttribute("recoveryDTO", new RecoveryDTO());

        return "recovery-pages/changePassword";
    }

    /**
     * Represents <b>"/recovery/{recovery id}" [POST]</b> endpoint
     */
    @RequestMapping(method = RequestMethod.POST, value = "/{pageId}")
    public String changePassword(@ModelAttribute("recoveryDTO") @Valid RecoveryDTO recoveryDTO, BindingResult bindingResult,
                                 @PathVariable int pageId, HttpServletResponse response){
        if(this.recoveryPageIdEmail.get(pageId) == null){
            return "redirect:/";
        }
        if(bindingResult.hasErrors() || !recoveryDTO.getPassword().equals(recoveryDTO.getConfirmPassword())){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "recovery-pages/changePassword";
        }

        User user = userService.getByEmail(recoveryPageIdEmail.get(pageId));
        user.setPassword(recoveryDTO.getPassword());
        userService.update(user);

        recoveryPageIdEmail.remove(pageId);

        return "recovery-pages/passwordWasChanged";
    }

    /**
     * @return map that contains pageId-email
     */
    public Map<Integer, String> getRecoveryPageIdEmail() {
        return recoveryPageIdEmail;
    }
}
