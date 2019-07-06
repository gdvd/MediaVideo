package fr.gdvd.media_manager.entitiesMysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity @Table(uniqueConstraints={@UniqueConstraint(columnNames={"sourceUrl"}), @UniqueConstraint(columnNames={"name"})})
public class VideoSourceInfo {

    @Id @Size(max = 8) @NotNull // maybe to abort this, initially to identify the information source, like imdb or others
    private String idSourceInfo;
    @Size(max = 255)
    private String sourceUrl;
    @Size(max = 16)
    private String name;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModifSource;

    @OneToMany(mappedBy = "videoSourceInfo")
    private List<VideoFilm> videoFilms = new ArrayList<>();

}
