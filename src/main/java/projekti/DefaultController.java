package projekti;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * WebDev with Java.
 * Okko Partanen
 * Default controller.
 * If user is not authenticated direct them to register page else direct them to front page.
 */
@Controller
public class DefaultController {
    
    @Autowired
    UserObjectRepository userRepository;    
    
    @GetMapping("/")
    public String index(Model model) {        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();            
        UserObject user = userRepository.findByUsername(auth.getName());
        if (user!=null) {
             model.addAttribute("user", userRepository.findAll());
             model.addAttribute("auth", user);
             return "index";    
        } else {
           return "register";
        }             
    }   
}
   
    
  

