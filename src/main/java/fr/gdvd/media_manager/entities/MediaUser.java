package fr.gdvd.media_manager.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document
public class MediaUser {

    @Id
    private ObjectId id;
    private String login;
    private String password;
    private boolean active;
    private Date dateModif;
    @DBRef
    private List<MediaRole> roles = new ArrayList<>();

}
