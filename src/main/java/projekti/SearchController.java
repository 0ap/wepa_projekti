package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @Autowired
    UserObjectRepository userRepository;

    @PostMapping("/search")
    public String setProfile(Model model, @RequestParam String username) {
        UserObject user = userRepository.findByUsername(username);
        model.addAttribute("user", user);
        return "search";
    }

}
