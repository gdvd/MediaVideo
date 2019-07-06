package fr.gdvd.media_manager.entitiesMysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Entity @Table(uniqueConstraints={@UniqueConstraint(columnNames={"language"})})
public class MyMediaLanguage {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idMyMediaLanguage;
    @Size(max = 16)
    private String language;

    @JsonIgnore
    @OneToMany(mappedBy = "myMediaLanguage", cascade = {CascadeType.ALL})
    private List<MyMediaText> myMediaTexts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "myMediaInfo", cascade = {CascadeType.ALL})
    private List<MyMediaAudio> myMediaAudios = new ArrayList<>();

    public MyMediaLanguage(@Size(max = 16) String language) {
        this.language = language;
    }
}
