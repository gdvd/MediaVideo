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
public class OptionGetMmiWithIdVf implements Serializable {

    private List<String> lidvf;


    private Boolean withOptionL1;
    private int seasonMin;
    private int seasonMax;
    private int episodeMin;
    private int episodeMax;
    private String nameSerie;
    private int keywordSerie;


    private Boolean withOptionL2;
    private String importStr;

    private Boolean withOptionL3;
    private int durationMin;
    private int durationMax;
    private int widthMin;
    private int widthMax;
    private String languagesStr;
}
