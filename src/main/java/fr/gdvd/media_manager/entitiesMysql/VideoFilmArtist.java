package fr.gdvd.media_manager.entitiesMysql;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

//@AllArgsConstructor -> create new constructor with EmbeddedKeyVideoFil2Artist
@Data
@NoArgsConstructor
@ToString
@Entity
public class VideoFilmArtist implements Serializable {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "id.videoArtist",
                    column = @Column(name = "id_video_artist", columnDefinition="varchar(16)")),
            @AttributeOverride(name = "id.videoFilm",
                    column = @Column(name = "id_video_film", columnDefinition="varchar(16)"))
    })
    private EmbeddedKeyVideoFilmArtist id;

    @Column(nullable=false, columnDefinition="boolean default false")
    private boolean actor;
    @Column(columnDefinition = "int default 0")
    private int numberOrderActor;
    @Column(nullable=false, columnDefinition="boolean default false")
    private boolean director;
    @Column(nullable=false, columnDefinition="boolean default false")
    private boolean writer;
    @Column(nullable=false, columnDefinition="boolean default false")
    private boolean producer;
    @Column(nullable=false, columnDefinition="boolean default false")
    private boolean music;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_video_artist", insertable = false, updatable = false, columnDefinition="varchar(16)")
    private VideoArtist videoArtist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_video_film", insertable = false, updatable = false, columnDefinition="varchar(16)")
    private VideoFilm videoFilm;

    public VideoFilmArtist(VideoArtist videoArtist, VideoFilm videoFilm) {
        this.videoArtist = videoArtist;
        this.videoFilm = videoFilm;
        this.id = new EmbeddedKeyVideoFilmArtist(
                videoFilm.getIdVideo(), videoArtist.getIdVideoArtist());
    }
}
