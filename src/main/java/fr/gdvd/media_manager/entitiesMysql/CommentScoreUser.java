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
public class CommentScoreUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCommentScoreUser;
    @NotNull
    @Size(max = 1024)
    private String comment;
    @JsonIgnore
    @OneToOne(mappedBy = "commentScoreUser")
    private VideoUserScore videoUserScore;
}
