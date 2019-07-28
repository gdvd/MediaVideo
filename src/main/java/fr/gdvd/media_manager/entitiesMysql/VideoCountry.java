package fr.gdvd.media_manager.entitiesMysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @ToString
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"country"})})
public class VideoCountry {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCountry;

    @Column(name = "country")
    @Size(max = 32)
    private String country;

    @Nullable
    @Size(max = 32)
    private String urlCountry;

    @Nullable
    @JsonIgnore
    @ManyToMany(mappedBy = "videoCountries")
    private List<VideoFilm> videoFilms = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "videoCountry")
    private List<VideoTitle> videoTitles = new ArrayList<>();

}
