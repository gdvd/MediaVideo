package fr.gdvd.media_manager.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data @NoArgsConstructor @AllArgsConstructor @ToString
@Document
public class MediaVideo {

    @Id
    private String id;
    private Map<String, Object> info;
    private List<Map<String, Object>> video;
    private List<Map<String, Object>> audio;
    private List<Map<String, Object>> text;
    private boolean active;
    @DBRef
    private List<MediaTitle> mediaTitles = new ArrayList<>();

    public void addMediaTitle(MediaTitle mt){
        mediaTitles.add(mt);
    }
}
