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

    @GetMapping
    public String showRecoveryPage(HttpServletRequest request, Model model){
        logger.debug("was get request to /recovery by " + request.getRemoteAddr());

        model.addAttribute("userValid", new UserDTO());

        return "recovery-pages/recovery";
    }

    @PostMapping
    public String sendEmail(@ModelAttribute("userValid") UserDTO userDTO, HttpServletRequest request){
        logger.debug("was post request to /recovery by " + request.getRemoteAddr());

        if(userService.getByEmail(userDTO.getEmail()) != null){
            int tempId = (int) (Math.random() * 99999);
            recoveryIdsEmails.put(tempId, userDTO.getEmail());

            try{
                emailSenderService.sendEmail(
                        userDTO.getEmail(),
                        "Password recovery",
                        "follow this link to reset your password: http://localhost:8080/recovery/" + tempId
                );
            }
            catch (NullPointerException e){
                logger.warn(request.getRemoteAddr() + " got " + e);
                return "recovery-pages/badRecovery";
            }

            return "recovery-pages/successRecovery";
        }
        userDTO.setFormCorrect(false);

        return "recovery-pages/recovery";
    }

    @GetMapping("/{recoveryId}")
    public String showChangingPasswordPage(@PathVariable("recoveryId") int recoveryId, HttpServletRequest request, Model model){
        logger.debug("was get request to /recovery/" + recoveryId + " by " + request.getRemoteAddr());

        if(this.recoveryIdsEmails.get(recoveryId) == null){
            return "redirect:/";
        }
        model.addAttribute("recoveryId", recoveryId);
        model.addAttribute("userValid", new UserDTO());

        return "recovery-pages/changePassword";
    }

    @PostMapping("/{recoveryId}")
    public String changePassword(@ModelAttribute("userValid") @Valid UserDTO userDTO, BindingResult bindingResult,
                                 HttpServletRequest request,
                                 @PathVariable int recoveryId){
        logger.debug("was post request to /recovery/" + recoveryId + " by " + request.getRemoteAddr());

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
