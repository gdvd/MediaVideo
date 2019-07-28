package fr.gdvd.media_manager.entitiesMysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class VideoComment {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idComment;
    @NotNull @Size(max = 1024)
    private String comment;

    @JsonIgnore
    @OneToOne
    @JoinColumn(columnDefinition="varchar(16)")
    private VideoFilm videoFilm;

}
