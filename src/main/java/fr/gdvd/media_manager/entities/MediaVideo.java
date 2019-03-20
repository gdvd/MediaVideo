package fr.gdvd.media_manager.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor @AllArgsConstructor @ToString
@Document
public class MediaVideo {

    @Id
    private String id;
    private List<Map<String, String>> title;
    private Map<String, Object> info;
    private List<Map<String, Object>> video;
    private List<Map<String, Object>> audio;
    private List<Map<String, Object>> text;

}
