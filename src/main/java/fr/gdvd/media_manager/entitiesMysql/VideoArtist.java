package fr.gdvd.media_manager.entitiesMysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class VideoArtist implements Serializable {

    @Id @Size(max = 16)
    private String idVideoArtist;
    @Size(max = 128)
    private String firstLastName;

    @OneToMany(mappedBy = "videoArtist")
    private List<VideoFilmArtist> videoFilm2Artists =  new ArrayList<>();

}
