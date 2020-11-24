package projekti;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 
 * MOOC WebDev with Java coursework
 * Okko Partanen
 * FileObject class handles the pictures.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileObject extends AbstractPersistable<Long> {

    @Lob
    private byte[] content;

    private int likes;
    private String description;

    @ManyToMany(cascade = {CascadeType.ALL})
    private List<UserObject> likedThis;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Comment> comments;

    public void increaseLikes(UserObject user) {
        this.likes = this.likes + 1;
        this.likedThis.add(user);

    }

    public List<UserObject> getListUsers() {
        return this.likedThis;
    }

    @Override
    public String toString() {
        return "";
    }

    public String getBase64() {
        String base64EncodedImage = Base64.encodeBase64String(this.getContent());
        return base64EncodedImage;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public List<Comment> getComments() {
        return this.comments;
    }

}
