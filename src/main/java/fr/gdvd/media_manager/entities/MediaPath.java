package fr.gdvd.media_manager.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;


@Data @ToString
@NoArgsConstructor @AllArgsConstructor
@Document
public class MediaPath {

    @Id
    private ObjectId id;
    private boolean active;
    private String pathGeneral;
    private String nameExport;
    private List<String> videoids;
    private Date dateModif;
    private String type;
    private String owner;
    private List<Map<String, String>> users;
}
