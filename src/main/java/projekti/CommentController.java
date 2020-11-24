package projekti;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
 *
 * MOOC WebDev with Java coursework
 * Okko Partanen
 * Class controlling comments on the users wall (profile)
 * and on profile picture and on image album. Also handles likes on comments.
 *
*/


@Controller
public class CommentController {

    @Autowired
    UserObjectRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    FileObjectRepository fileObjectRepository;
    
    //Adding a comment to users profile / wall.
    @PostMapping("/comment")
    public String addComment(@RequestParam String content, @RequestParam String profile) {
        Date date = new Date();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //get authenticated user
        UserObject user = userRepository.findByUsername(profile);
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setMadeBy(user);
        comment.setPostTime(date);
        comment.setLeftBy(auth.getName()); //set the user who left the comment
        comment.setLikes(0);
        user.addComment(comment);
        userRepository.save(user);
        commentRepository.save(comment);
        return "redirect:/user/" + user.getShortlink(); //redirect back to users profile
    }
    
    //A funnction for adding likes to comments on pictures
    @PostMapping("/likecomment")
    
    public String addCommentLike(@RequestParam Long id) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserObject user = userRepository.findByUsername(username);
        Comment comment = commentRepository.getById(id);
        UserObject user2 = comment.getMadeBy();
        
        //Check if the authenticated user has already liked the comment - if yes, let's not increase the likes.
        if (!comment.inUserList(user))
            comment.increaseLikes(user);          
            
        commentRepository.save(comment);
        return "redirect:/user/" + user2.getShortlink();
    }
    
    //A function for adding a like to a comment in a users wall / profile.
    @PostMapping("/likecommentProfile")
    public String addCommentLikeProfile(@RequestParam Long id, @RequestParam String shortlink) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserObject user = userRepository.findByUsername(username);
        Comment comment = commentRepository.getById(id);
        UserObject user2 = comment.getMadeBy();

        if (!comment.inUserList(user))
            comment.increaseLikes(user);      
            
        commentRepository.save(comment);
        return "redirect:/user/" + shortlink;
    }
    
    //A function for liking a comment in a image album.
    @PostMapping("/likecommentAlbum")
    public String addCommentLikeAlbum(@RequestParam Long id, @RequestParam String shortlink) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserObject user = userRepository.findByUsername(username);
        Comment comment = commentRepository.getById(id);
        UserObject user2 = comment.getMadeBy();

        if (!comment.inUserList(user))
            comment.increaseLikes(user);  
            
        commentRepository.save(comment);        
        return "redirect:/user/" + shortlink + "/album";
    }
    
    //A function for adding a comment to a image.
    @PostMapping("/imagecomment")
    public String commentImage(@RequestParam Long id, @RequestParam String comment, @RequestParam String shortlink) {

        UserObject toDirect = userRepository.findByShortlink(shortlink);
        Date date = new Date();
        FileObject fo = fileObjectRepository.findOneById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String current = auth.getName();
        UserObject user = userRepository.findByUsername(current);
        Comment newComment = new Comment();
        //Set content, the user who left the comment and timestamp.
        newComment.setContent(comment);
        newComment.setLeftBy(current);
        newComment.setPostTime(date);
        newComment.setMadeBy(user);
        fo.addComment(newComment);
        fileObjectRepository.save(fo);
        commentRepository.save(newComment);
        return "redirect:/user/" + toDirect.getShortlink() + "/album";

    }
    
    //Adding on comment on the profile picture.
    @PostMapping("/imagecommentProfile")
    public String commentImageProfile(@RequestParam Long id, @RequestParam String comment, @RequestParam String shortlink) {

        UserObject toDirect = userRepository.findByShortlink(shortlink);
        Date date = new Date();
        FileObject fo = fileObjectRepository.findOneById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String current = auth.getName();
        UserObject user = userRepository.findByUsername(current);
        Comment newComment = new Comment();
        newComment.setContent(comment);
        newComment.setLeftBy(current);
        newComment.setPostTime(date);
        newComment.setMadeBy(user);
        fo.addComment(newComment);
        fileObjectRepository.save(fo);
        commentRepository.save(newComment);
        return "redirect:/user/" + toDirect.getShortlink();

    }

}
