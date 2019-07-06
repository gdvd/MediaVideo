package fr.gdvd.media_manager.entitiesMysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data @NoArgsConstructor @AllArgsConstructor @ToString
@Entity
public class MyMediaText {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "id.MyMediaInfo",
                    column = @Column(name = "id_my_media_info")),
            @AttributeOverride(name = "id.MyMediaLanguage",
                    column = @Column(name = "id_my_media_language"))
    })
    private EmbeddedKeyMyMediaText id;

    @Size(max = 16)
    private String format;
    @Size(max = 16)
    private String codecId;
    private boolean forced;
    private boolean internal;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name ="id_my_media_info", insertable = false, updatable = false)
    private MyMediaInfo myMediaInfo;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name ="id_my_media_language", insertable = false, updatable = false)
    private MyMediaLanguage myMediaLanguage;

    public MyMediaText(MyMediaInfo myMediaInfo, MyMediaLanguage myMediaLanguage) {
        this.myMediaInfo = myMediaInfo;
        this.myMediaLanguage = myMediaLanguage;
        this.id = new EmbeddedKeyMyMediaText(myMediaInfo.getIdMyMediaInfo(),
                myMediaLanguage.getIdMyMediaLanguage());
    }
}
