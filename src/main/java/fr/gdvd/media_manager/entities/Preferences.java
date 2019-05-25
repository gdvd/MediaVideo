package fr.gdvd.media_manager.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data @ToString @NoArgsConstructor @AllArgsConstructor
@Document
public class Preferences {

    @Id
    private String id;
    private List<Date> dateStart;
    private List<String> extentionsVideo;
    private String pathIdVideo;
    private int minSizeOfVideoFile;

}
