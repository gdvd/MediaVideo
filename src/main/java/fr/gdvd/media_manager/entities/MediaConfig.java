package fr.gdvd.media_manager.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data @ToString
@NoArgsConstructor @AllArgsConstructor
@Document
public class MediaConfig {

    @Id
    private String id;
    private Document path;
    //    private Map<String, Map<String, List<String>>> path = new HashMap<>();

}
