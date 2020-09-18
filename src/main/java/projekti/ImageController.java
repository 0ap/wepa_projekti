/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageController {

    @Autowired
    UserObjectRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    FileObjectRepository fileObjectRepository;

    @PostMapping("/deletepicture")
    public String deletePicture(@RequestParam Long id, @RequestParam String username) {

        FileObject fo = fileObjectRepository.getOne(id);
        UserObject user = userRepository.findByUsername(username);
        if (id.equals(user.getProfilepic().getId()))
            user.deleteProfilePicture();
        
        user.deletePicture(fo);
        userRepository.save(user);
        return "redirect:/user/" + user.getShortlink() + "/album";
    }

    @PostMapping("/setasprofile")
    public String setProfile(@RequestParam Long id, @RequestParam String username) {
        FileObject fo = fileObjectRepository.findOneById(id);
        UserObject user = userRepository.findByUsername(username);
        user.setProfilepic(fo);
        userRepository.save(user);
        return "redirect:/user/" + user.getShortlink() + "/album";
    }

    @PostMapping("/likepicture")
    public String addPictureLike(@RequestParam String userToLike, @RequestParam Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserObject user = userRepository.findByUsername(username);
        UserObject user2 = userRepository.findByUsername(userToLike);
        FileObject picture = fileObjectRepository.findOneById(id);

        if (picture.getLikedThis().contains(user)) {

        } else {
            picture.increaseLikes(user);
            fileObjectRepository.save(picture);
        }
        return "redirect:/user/" + user2.getShortlink() + "/album";
    }
    
     

    @PostMapping("/likepictureProfile")
    public String addPictureLikeProfile(@RequestParam String userToLike, @RequestParam Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserObject user = userRepository.findByUsername(username);
        UserObject user2 = userRepository.findByShortlink(userToLike);
        FileObject picture = fileObjectRepository.findOneById(id);

        if (picture.getLikedThis().contains(user)) {

        } else {
            picture.increaseLikes(user);
            fileObjectRepository.save(picture);
        }
        return "redirect:/user/" + user2.getShortlink();
    }

    @PostMapping("/addPicutre")
    public String addPicture(@RequestParam("file") MultipartFile file, @RequestParam String desc) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserObject user = userRepository.findByUsername(username);
        FileObject fo = new FileObject();
        fo.setContent(file.getBytes());
        fo.setDescription(desc);
        
        

        if (user.getProfilepic() == null) {
            user.setProfilepic(fo);
        }

        user.addPicture(fo);
        userRepository.save(user);
        return "redirect:/user/" + user.getShortlink() + "/album";
    }
}
