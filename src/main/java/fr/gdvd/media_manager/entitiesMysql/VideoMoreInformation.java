package fr.gdvd.media_manager.entitiesMysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class VideoMoreInformation {

//    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idVideoMoreInformation;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(length=1024)
    Map<String, String> informap = new HashMap<>();

    @JsonIgnore
    @OneToOne
    @JoinColumn(columnDefinition="varchar(16)")
    private VideoFilm videoFilm;


}
