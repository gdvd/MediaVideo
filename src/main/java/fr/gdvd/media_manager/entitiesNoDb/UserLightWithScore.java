package fr.gdvd.media_manager.entitiesNoDb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserLightWithScore {

    private Long idMyUser;
    private String login;
    private Boolean used;
    private int scoreMin;
    private int scoreMax;
    private Boolean scoreMayNull;

}
