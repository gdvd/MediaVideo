package fr.gdvd.media_manager.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data @NoArgsConstructor @AllArgsConstructor @ToString
@Document
public class MediaTitle {

    @Id
    private String id;
    private String title;
    private String absolutePath;
    private boolean active;
    @JsonIgnore
    @DBRef
    private MediaPath mediaPath;
    @JsonIgnore
    @DBRef
    private MediaVideo mediaVideo;

}
