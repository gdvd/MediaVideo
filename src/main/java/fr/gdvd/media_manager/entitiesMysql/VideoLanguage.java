package fr.gdvd.media_manager.entitiesMysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class VideoLanguage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVideoLanguage;

    @Size(max = 32)
    private String language;

    @Size(max = 32)
    private String urlLanguage;

    @JsonIgnore
    @ManyToMany(mappedBy = "videoLanguages")
    private List<VideoFilm> videoFilms = new ArrayList<>();



}
