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
public class MyMediaAudio {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "id.MyMediaInfo",
                    column = @Column(name = "id_my_media_info")),
            @AttributeOverride(name = "id.MyMediaLanguage",
                    column = @Column(name = "id_my_media_language"))
    })
    private EmbeddedKeyMyMediaAudio id;

    @Size(max = 16)
    private String format;
    private Double duration;
    private Double bitrate;
    private int channels;
    private boolean forced;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name ="id_my_media_info", insertable = false,
            updatable = false)
    private MyMediaInfo myMediaInfo;

    @ManyToOne
    @JoinColumn(name ="id_my_media_language", insertable = false,
            updatable = false)
    private  MyMediaLanguage myMediaLanguage;

    public MyMediaAudio(MyMediaInfo myMediaInfo, MyMediaLanguage myMediaLanguage) {
        this.myMediaInfo = myMediaInfo;
        this.myMediaLanguage = myMediaLanguage;
        this.id = new EmbeddedKeyMyMediaAudio(myMediaInfo.getIdMyMediaInfo(),
                myMediaLanguage.getIdMyMediaLanguage());
    }
}
