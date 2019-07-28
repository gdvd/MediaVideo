package fr.gdvd.media_manager.entitiesMysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class TypeMmi implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idTypeMmi;

    @Nullable
    private int season;
    @Nullable
    private int episode;
    @Nullable
    @Size(max = 32)
    private String nameSerie;
    @Nullable
    @Size(max = 32)
    private String nameSerieVO;

    @Nullable
    private boolean active;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModif;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_type_name")
    private TypeName typeName;

    @Nullable
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_video_film")
    private VideoFilm videoFilm;

    @JsonIgnore
    @Nullable
    @OneToMany(mappedBy = "typeMmi")
    private List<MyMediaInfo> myMediaInfos = new ArrayList<>();

}
