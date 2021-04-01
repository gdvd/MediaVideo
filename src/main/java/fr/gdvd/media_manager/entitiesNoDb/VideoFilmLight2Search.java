package fr.gdvd.media_manager.entitiesNoDb;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class VideoFilmLight2Search {
    @NotNull
    private String idVideo;
    private String title;
    private int score;
    private int year;
    private int idType;
    private int episode;
    private int season;
    private int idImport;
    private int supportWidth;
    private int idLogin;
    private double size;
    private String idName;
    private int functionName;
    private String keyword;
    private String kind;
    private String language;
    private String country;

    public VideoFilmLight2Search(@NotNull String idVideo) {
        this.idVideo = idVideo;
    }
}
