package fr.gdvd.media_manager.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
public class MediaVideoLight {

    @Id
    private String id;
    private List<Map<String, String>> title;
    private Map<String, Object> info;
    private Map<String, Object> video;
    private Map<String, Object> audio;
    private Map<String, Object> text;

}
