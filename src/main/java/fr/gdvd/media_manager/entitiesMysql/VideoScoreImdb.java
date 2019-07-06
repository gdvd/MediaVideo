package fr.gdvd.media_manager.entitiesMysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class VideoScoreImdb {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idScoreImdb;
    private int scoreOnHundred;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModifScoreImdb;
    private int nbOfVote;
    @ManyToOne
    private VideoFilm videoFilm;

}
