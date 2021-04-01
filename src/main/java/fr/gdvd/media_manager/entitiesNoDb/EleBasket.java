package fr.gdvd.media_manager.entitiesNoDb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EleBasket {

    private String basketName;
    private String idMmi;
    private Double fileSize;
    private String nameExport;
    private String pathGeneral;
    private String title;

}
