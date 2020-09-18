package projekti;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/comment")
    public String addComment(@RequestParam String content, @RequestParam String profile) {
        Date date = new Date();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserObject user = userRepository.findByUsername(profile);
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setMadeBy(user);
        comment.setPostTime(date);
        comment.setLeftBy(auth.getName());
        comment.setLikes(0);
        user.addComment(comment);
        userRepository.save(user);
        commentRepository.save(comment);
        return "redirect:/user/" + user.getShortlink();
    }

    @PostMapping("/likecomment")
    public String addCommentLike(@RequestParam Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserObject user = userRepository.findByUsername(username);
        Comment comment = commentRepository.getById(id);
        UserObject user2 = comment.getMadeBy();

        if (comment.inUserList(user)) {
            commentRepository.save(comment);
        } else {
            comment.increaseLikes(user);
            commentRepository.save(comment);
        }

        return "redirect:/user/" + user2.getShortlink();
    }
    
    @PostMapping("/likecommentProfile")
    public String addCommentLikeProfile(@RequestParam Long id, @RequestParam String shortlink) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserObject user = userRepository.findByUsername(username);
        Comment comment = commentRepository.getById(id);
        UserObject user2 = comment.getMadeBy();

        if (comment.inUserList(user)) {
            commentRepository.save(comment);
        } else {
            comment.increaseLikes(user);
            commentRepository.save(comment);
        }

        return "redirect:/user/" + shortlink;
    }

    @PostMapping("/likecommentAlbum")
    public String addCommentLikeAlbum(@RequestParam Long id, @RequestParam String shortlink) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserObject user = userRepository.findByUsername(username);
        Comment comment = commentRepository.getById(id);
        UserObject user2 = comment.getMadeBy();

        if (comment.inUserList(user)) {
            commentRepository.save(comment);
        } else {
            comment.increaseLikes(user);
            commentRepository.save(comment);
        }
        return "redirect:/user/" + shortlink + "/album";
    }

    @PostMapping("/imagecomment")
    public String commentImage(@RequestParam Long id, @RequestParam String comment, @RequestParam String shortlink) {

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
        return "redirect:/user/" + toDirect.getShortlink() + "/album";

    }
    
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
