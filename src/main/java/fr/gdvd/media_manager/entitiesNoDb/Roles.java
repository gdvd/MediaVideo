package fr.gdvd.media_manager.entitiesNoDb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Roles {

    private Boolean actor;
    private Boolean director;
    private Boolean music;
    private Boolean producer;
    private Boolean writer;

}
