package projekti;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class UserObject extends AbstractPersistable<Long> {

    private String username;
    private String password;
    private String shortlink;

    @OneToOne(cascade = {CascadeType.ALL})
    private FileObject profilepic;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Comment> profileComments;

    @ManyToMany
    private List<UserObject> invites;

    @ManyToMany
    private List<UserObject> friends;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<FileObject> pictures;

    public FileObject getPicture() {
        return this.profilepic;
    }

    public List<Comment> getComments() {
        return this.profileComments;
    }

    public void addComment(Comment comment) {
        this.profileComments.add(comment);
    }

    public void addFriend(UserObject user) {
        this.friends.add(user);
    }

    public void addInvite(UserObject user) {
        this.invites.add(user);
    }

    public void deleteInvite(UserObject user) {
        this.invites.remove(user);
    }

    public void addPicture(FileObject fo) {
        this.pictures.add(fo);
    }

    public List<FileObject> getPictures() {
        return this.pictures;
    }

    public void deletePicture(FileObject fo) {
        this.pictures.remove(fo);
    }

    public int inviteSize() {
        return this.invites.size();
    }

    public void deleteProfilePicture() {
        this.profilepic = null;
    }
}
