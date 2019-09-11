package fr.gdvd.media_manager.entitiesMysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @ToString
@Entity
public class MyMediaInfo implements Serializable {


    @Id @Size(max = 32) @NotNull
    private String idMyMediaInfo;

    @Size(max = 16)
    private String format;
    private Double fileSize;
    private Double duration;


    @Nullable
    @OneToMany(mappedBy = "myMediaInfo", cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    private List<MyMediaAudio> myMediaAudios = new ArrayList<>();

    @Nullable
    @OneToMany(mappedBy = "myMediaInfo", cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    private List<MyMediaText> myMediaTexts = new ArrayList<>();

    /* From mediaVideo*/
    @Size(max=32)
    private String codecId;
    private Double bitrate;
    private int width;
    private int height;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_type_mmi")
    private TypeMmi typeMmi;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModif;

    @OneToMany (mappedBy = "myMediaInfo", cascade = {CascadeType.ALL})
    private List<VideoSupportPath> videoSupportPaths = new ArrayList<>();

    @Nullable
    @OneToMany(mappedBy = "myMediaInfo", cascade = {CascadeType.ALL})
    private List<MyMediaComment> myMediaComments = new ArrayList<>();

    @Nullable
    @JsonIgnore
    @OneToMany(mappedBy = "myMediaInfo", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Basket> baskets = new ArrayList<>();

    public MyMediaInfo(@Size(max = 32) @NotNull String idMyMediaInfo) {
        this.idMyMediaInfo = idMyMediaInfo;
    }
}
