/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author O
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends AbstractPersistable<Long> {

    @Id
    private Long id;
    private String content;
    private String leftBy;

    @OneToOne
    private UserObject madeBy;

    private int likes;

    private Date postTime;

    @ManyToMany(cascade = {CascadeType.ALL})
    private List<UserObject> likedThis;

    public List<UserObject> getListUsers() {
        return this.likedThis;
    }

    public boolean inUserList(UserObject user) {
        return this.likedThis.contains(user);
    }

    public void increaseLikes(UserObject user) {
        this.likes = this.likes + 1;
        this.likedThis.add(user);

    }

    public String getContent() {
        return this.content;
    }

    public String getLeftBy() {
        return this.leftBy;
    }

    public UserObject getMadeBy() {
        return this.madeBy;
    }
}
