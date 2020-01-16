package fr.gdvd.media_manager.entitiesMysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class MyMediaComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMyMediaComment;
    @Size(max=1024)
    private String mediaComment;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "fk_myMediaInfo")
    private MyMediaInfo myMediaInfo;
}
