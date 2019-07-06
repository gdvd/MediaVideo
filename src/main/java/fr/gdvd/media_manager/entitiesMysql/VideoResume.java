package fr.gdvd.media_manager.entitiesMysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class VideoResume {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idresume;
    @Size(max = 2048)
    private String textResume;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModifResume;
    @ManyToOne
    @JoinColumn(name="fk_videoFilm")
    private VideoFilm videoFilm;

}
