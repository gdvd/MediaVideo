package fr.gdvd.media_manager.entitiesNoDb;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class OneFilmpgraphy {

    private String idNm;
    private String name;
    private String urlImg;

    private List<Filmlight> filmo = new ArrayList<>();


}
