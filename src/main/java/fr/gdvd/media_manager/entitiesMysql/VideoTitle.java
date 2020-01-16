package fr.gdvd.media_manager.entitiesMysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data @NoArgsConstructor
//@AllArgsConstructor -> create new constructor with EmbeddedKeyVideoTitle
@ToString
@Entity
public class VideoTitle {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "id.videoCountry",
                    column = @Column(name = "id_video")),
            @AttributeOverride(name = "id.videoFilm",
                    column = @Column(name = "id_video"))
    })
    private EmbeddedKeyVideoTitle id;

    @Size(max = 128)
    private String title;

    @ManyToOne
    @JoinColumn(name = "id_country", insertable = false, updatable = false)
    private VideoCountry videoCountry;

    /*Experimental
    private boolean select;*/

    @JsonIgnore
    @ManyToOne/*(fetch = FetchType.LAZY)*/
    @JoinColumn(name = "id_video", columnDefinition="varchar(16)", insertable = false, updatable = false)
    private VideoFilm videoFilm;

    public VideoTitle(VideoCountry videoCountry, VideoFilm videoFilm) {
        this.videoCountry = videoCountry;
        this.videoFilm = videoFilm;
        this.id = new EmbeddedKeyVideoTitle(
                videoFilm.getIdVideo(), videoCountry.getIdCountry());
    }
}
