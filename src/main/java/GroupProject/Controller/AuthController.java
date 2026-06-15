package GroupProject.Controller;

import GroupProject.Entity.User;
import GroupProject.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    //SHOW login page
    @GetMapping("/login")
    public String showlogin(Model model){
        model.addAttribute("user", new User());
        return "auth/login";
    }

    //process login
    @PostMapping("/login")
    public String processLogin(@RequestParam String email, @RequestParam String password,
                               HttpSession session, Model model){
        User user = userService.login(email, password);
        if(user != null){
            session.setAttribute("loggedInUser", user);
            return "redirect:/dashboard";
        }
        model.addAttribute("error", "Invalid email or password!");
        return "auth/login";
    }

    //SHOW register page
    @GetMapping("/register")
    public String showregister(Model model){
        model.addAttribute("user", new User());
        return "auth/register";
    }

    //PROCESS register
    @PostMapping("/register")
    public String processRegister(@ModelAttribute User user, Model model){
        boolean success = userService.register(user);
        if(success){
            return "redirect:/login?registered=true";
        }
        model.addAttribute("error", "Email already exists!");
        return "auth/register";
    }

    //LOGOUT
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }

}
