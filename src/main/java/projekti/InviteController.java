package projekti;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class InviteController {

    @Autowired
    UserObjectRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    FileObjectRepository fileObjectRepository;

    @PostMapping("/askasfriend")
    public String askasfriend(@RequestParam String username) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String current = auth.getName();
        UserObject curr = userRepository.findByUsername(current);
        UserObject user = userRepository.findByUsername(username);
        List<UserObject> friendInvites = user.getInvites();

        if (friendInvites.contains(curr) || curr.equals(user)
                || curr.getInvites().contains(user)
                || curr.getFriends().contains(user)
                || user.getFriends().contains(curr)) {

        } else {
            user.addInvite(curr);
            userRepository.save(user);

        }
        return "redirect:/";
    }

    @PostMapping("/acceptInvite")
    public String acceptFriend(@RequestParam String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String current = auth.getName();
        UserObject curr = userRepository.findByUsername(current);
        UserObject user = userRepository.findByUsername(username);
        curr.deleteInvite(user);
        if (curr.getFriends().contains(user)) {
            return "redirect:/";
        } else {
            user.addFriend(curr);
            curr.addFriend(user);
            curr.deleteInvite(user);
            userRepository.save(curr);
            return "redirect:/";
        }
    }

    @PostMapping("/rejectInvite")
    public String rejectFriend(@RequestParam String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String current = auth.getName();
        UserObject curr = userRepository.findByUsername(current);
        UserObject user = userRepository.findByUsername(username);
        curr.deleteInvite(user);
        userRepository.save(curr);
        return "redirect:/";
    }

    @GetMapping(path = "/invites/{id}")
    public String invites(Model model, @PathVariable String id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String current = auth.getName();
        UserObject a = userRepository.findByUsername(current);
        UserObject c = userRepository.findByShortlink(id);

        if (a.getShortlink().equals(c.getShortlink())) {

            List<UserObject> invites = c.getInvites();
            model.addAttribute("invites", invites);
            model.addAttribute("user", c);
            return "invites";
        } else {
            return "redirect:/";
        }

    }
}
