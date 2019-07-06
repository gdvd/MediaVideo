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
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity @Table(uniqueConstraints={@UniqueConstraint(columnNames={"keywordEn"}), @UniqueConstraint(columnNames={"keywordFr"})})
public class VideoKeyword {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idKeyword;
    @Size(max = 64)
    private String keywordEn;
    @Size(max = 64)
    private String keywordFr;

    @ManyToMany(mappedBy = "videoKeywordSet")
    private List<VideoFilm> videoFilms = new ArrayList<>();


}
