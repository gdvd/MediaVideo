package fr.gdvd.media_manager.entitiesMysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class VideoUserScore implements Serializable {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "id.video",
                    column = @Column(name = "id_video")),
            @AttributeOverride(name = "id.myUser",
                    column = @Column(name = "id_my_user"))
    })
    private EmbeddedKeyVideoUserScore id;

    private int noteOnHundred;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModifScoreUser;

    @ManyToOne
    @JoinColumn(name = "id_my_user", insertable = false, updatable = false)
    private MyUser myUser;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_video", insertable = false, updatable = false)
    private VideoFilm videoFilm;

    @OneToOne
    @JoinColumn(name = "fkCommentScoreUser")
    private CommentScoreUser commentScoreUser;

    public VideoUserScore(MyUser myUser, VideoFilm videoFilm) {
        this.myUser = myUser;
        this.videoFilm = videoFilm;
        this.id = new EmbeddedKeyVideoUserScore(videoFilm.getIdVideo(),
                myUser.getIdMyUser());
    }
}
