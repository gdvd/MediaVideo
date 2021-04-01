package fr.gdvd.media_manager.entitiesNoDb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SubmitSerie {

    private int season;
    private int episode;
    @NotNull
    private String nameSerieVo;
    @NotNull
    private String idMyMediaInfo;
    @NotNull
    private Long idTypemmi;

}
