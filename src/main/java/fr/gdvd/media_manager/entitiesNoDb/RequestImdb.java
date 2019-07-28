package fr.gdvd.media_manager.entitiesNoDb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestImdb implements Serializable {

    private String info;
    private boolean state;
    private String link;
    private String name;
    private String urlImg;
    private boolean isVideo;

}
