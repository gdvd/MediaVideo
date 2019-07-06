package fr.gdvd.media_manager.entitiesMysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VideoSerie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idMyUser;
    @Size(max = 32)
    private String serieName;

    @OneToMany(mappedBy = "videoSerie")
    private List<VideoFilm> videoFilms = new ArrayList<>();

}
