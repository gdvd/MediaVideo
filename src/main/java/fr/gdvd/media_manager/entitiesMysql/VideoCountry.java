package fr.gdvd.media_manager.entitiesMysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @ToString
@Entity @Table(uniqueConstraints={@UniqueConstraint(columnNames={"country"})})
public class VideoCountry {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCountry;
    @Size(max = 32)
    private String country;

    @ManyToMany(mappedBy = "videoCountries")
    private List<VideoFilm> videoFilms = new ArrayList<>();

    @OneToMany(mappedBy = "videoCountry")
    private List<VideoTitle> videoTitles = new ArrayList<>();

}
