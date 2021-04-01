package fr.gdvd.media_manager.entitiesNoDb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestVideo implements Serializable {
    private String title;
    private int keyword;
    private int charError;

    private String nameSerie;
    private String country;
    private int keywordSerie;
    private int keywordCountryIsSel;

    private String keywordfilm;
    private String keywordsStr;
    private int keywordSel;

    private int pageSize;
    private int pageNumber;

    private int scoreMin;
    private int scoreMax;
    private Boolean scoreMayNull;
    private List<UserLightWithScore> userLightWithScore;

    private int yearMin;
    private int yearMax;
    private int durationMin;
    private int durationMax;
    private int widthMin;
    private int widthMax;
    private Boolean yearMayNull;
    private int seasonMin;
    private int seasonMax;
    private int episodeMin;
    private int episodeMax;
    private String importStr;
    private String kindStr;
    private String kindNotStr;
    private String languagesStr;

    private Roles roleList;
    private int keywordNameIsSel;
    private int charErrorNameIsSel;
    private String nameArtist;

    private List<Long> listType;
    private String sortBy;
    private Boolean ascending;
    private Boolean reqTitleScoreYear;
    private Boolean reqTypeEpisodeSeason;
    private Boolean reqImportSupportwidthSize;
    private Boolean reqNameandroles;
    private Boolean reqDurationWidthLanguage;
    private Boolean reqKeywordKindCountry;

}
