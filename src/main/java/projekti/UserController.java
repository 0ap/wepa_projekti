package projekti;

import java.util.List;
import org.apache.commons.codec.binary.Base64;
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
import org.springframework.web.bind.annotation.ResponseBody;

/*
* MOOC WebDev with Java.
* Okko Partanen
* Controller for handling paths such as users pictures, picture albums and profiles / walls.
*
*/


@Controller
public class UserController {

    @Autowired
    UserObjectRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    FileObjectRepository fileObjectRepository;

    @GetMapping(path = "/user/kuva/{id}", produces = "image/png")
    @ResponseBody
    public byte[] get(@PathVariable String id) {
        UserObject b = userRepository.findByUsername(id);
        FileObject file = b.getProfilepic();
        return file.getContent();
    }

    @GetMapping(path = "/user/{id}")
    public String profile(Model model, @PathVariable String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserObject user = userRepository.findByUsername(auth.getName());
        UserObject c = userRepository.findByShortlink(id);
        List<Comment> comments = c.getComments();

        if (c.getPicture() != null) {
            String base64EncodedImage = Base64.encodeBase64String(c.getPicture().getContent());
            model.addAttribute("base64EncodedImage", base64EncodedImage);
        }

        model.addAttribute("user", c);
        model.addAttribute("comments", comments);
        model.addAttribute("friends", c.getFriends());
        model.addAttribute("auth", user.getUsername());
        return "profile";

    }

    @GetMapping(path = "/user/{id}/album")
    public String picAlbum(Model model, @PathVariable String id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserObject user = userRepository.findByUsername(auth.getName());
        UserObject c = userRepository.findByShortlink(id);
        List<FileObject> pictures = c.getPictures();
        model.addAttribute("imagealbum", pictures);
        model.addAttribute("user", c);
        model.addAttribute("auth", user.getUsername());
        return "album";

    }

    @PostMapping("/return")
    public String addComment() {
        return "redirect:/";
    }

    @PostMapping("/goToAlbum")
    public String addComment(@RequestParam String username) {
        UserObject user = userRepository.findByUsername(username);
        return "redirect:/user/" + user.getShortlink() + "/album";
    }

}
