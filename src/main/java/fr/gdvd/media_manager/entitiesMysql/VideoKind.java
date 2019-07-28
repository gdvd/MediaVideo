package fr.gdvd.media_manager.entitiesMysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data @NoArgsConstructor @AllArgsConstructor @ToString
@Entity @Table(uniqueConstraints={@UniqueConstraint(columnNames={"kindEn"}), @UniqueConstraint(columnNames={"kindfr"})})
public class VideoKind {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idKind;
    @Size(max = 64)
    private String kindEn;
    @Size(max = 64)
    private String kindFr;

    @JsonIgnore
    @ManyToMany(mappedBy = "videoKinds")
    private List<VideoFilm> videoFilms = new ArrayList<>();

}
