package fr.gdvd.media_manager.entitiesMysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Remake {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idRemake;

    @ElementCollection
    @OrderColumn(name="information")
    private Set<String> remakes;

    @JsonIgnore
    @OneToMany(mappedBy = "remake")
    private List<VideoFilm> videoFilms = new ArrayList<>();
}
