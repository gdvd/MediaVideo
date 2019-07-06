package fr.gdvd.media_manager.entitiesMysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class VideoFilm implements Serializable {

    @Id @Size(max = 16) // imdb ref, or other
    private String idVideo;

    @Nullable
    @OneToMany(mappedBy = "videoFilm")
    private List<VideoPoster> videoPosters = new ArrayList<>();

    @Nullable
    @OneToOne(mappedBy = "videoFilm")
    private VideoTrailler videoTrailler;

    @Nullable
    @OneToOne(mappedBy = "videoFilm")
    private VideoMoreInformation videoMoreInformation;

    @Nullable
    @OneToOne(mappedBy = "videoFilm")
    @JoinColumn(columnDefinition="varchar(16)")
    private VideoComment videoComment;

    @NotNull
    private int year;
    @NotNull
    private long duration;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModifFilm;

    @Nullable
    @OneToMany(mappedBy = "videoFilm")
    private List<MyMediaInfo> myMediaInfos = new ArrayList<>();

    @Nullable
    @OneToMany(mappedBy = "videoFilm")
    private List<VideoResume> videoResumes = new ArrayList<>();

    @Nullable
    @ManyToOne
    @JoinColumn(name = "fk_VideoSerie")
    private VideoSerie videoSerie;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "fk_videoSourceInfo")
    private VideoSourceInfo videoSourceInfo;

    @NotNull
    @OneToMany(mappedBy = "videoFilm")
    private List<VideoScoreImdb> videoScoreImdbs = new ArrayList<>();

    @NotNull
    @OneToMany(mappedBy = "videoFilm")
    private List<VideoFilmArtist> videoFilmArtists = new ArrayList<>();

    @OneToMany(mappedBy = "videoFilm")
    private List<VideoTitle> videoTitles = new ArrayList<>();

    @OneToMany(mappedBy = "videoFilm")
    private List<VideoUserScore> videoUserScores = new ArrayList<>();

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "video_film2kind",
            joinColumns = {@JoinColumn(name = "idVideo")},
            inverseJoinColumns = { @JoinColumn(name = "idKind")}
    )
    List<VideoKind> videoKinds = new ArrayList<>();

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "video_film2country",
            joinColumns = {@JoinColumn(name = "idVideo",columnDefinition="varchar(16)")},
            inverseJoinColumns = { @JoinColumn(name = "idCountry")}
    )
    List<VideoCountry> videoCountries = new ArrayList<>();

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "video_film2keyword",
            joinColumns = { @JoinColumn(name = "idVideo",columnDefinition="varchar(16)") },
            inverseJoinColumns = { @JoinColumn(name = "idKeyword")}
    )
    List<VideoKeyword> videoKeywordSet = new ArrayList<>();

    /* Recursive link to define remake of a videoFilm on an other videoFilm */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "remake", referencedColumnName = "idVideo", columnDefinition="varchar(16)")
    private VideoFilm remake;

    @OneToMany(mappedBy = "remake", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<VideoFilm> children = new HashSet<>();

}
