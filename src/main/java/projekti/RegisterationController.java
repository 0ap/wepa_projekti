package projekti;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
* MOOC WebDev with Java Course project.
* Okko Partanen
* A class for handling registerations.
*/

@Controller
public class RegisterationController {

    @Autowired
    UserObjectRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    FileObjectRepository fileObjectRepository;

    @GetMapping("/register")
    public String register(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String add(@RequestParam String username, @RequestParam String password, @RequestParam String shortlink) {
        //Check that no users exists with the same name or short link. 
        if (userRepository.findByUsername(username) != null || userRepository.findByShortlink(shortlink) != null) {
            return "redirect:login/";
        }
        //Creating the user object
        UserObject userobj = new UserObject();
        userobj.setUsername(username);
        userobj.setPassword(passwordEncoder.encode(password));
        userobj.setShortlink(shortlink);
        userRepository.save(userobj);
        return "redirect:/login";
    }
}
