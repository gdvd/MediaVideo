package fr.gdvd.media_manager.entitiesNoDb;

import fr.gdvd.media_manager.entitiesMysql.PreferencesSubscribe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PreferencesSubscribeWithScore {

    private PreferencesSubscribe preferencesSubscribe;
    private int dateask;
    private int total;
    private Date dateModif;
    private Date datePrevious;
    private List<OneSimpleScore> lsimplescores;

}
