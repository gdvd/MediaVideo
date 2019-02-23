package fr.gdvd.media_manager.entities;


import com.sun.javafx.beans.IDProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor @ToString
@Document
public class MediaVideo {

    @Id
    private String id;
    private String urlFile;
    private List<Object> info;
    private List<Object> video;
    private List<Object> audio;
    private List<Object> text;

}
