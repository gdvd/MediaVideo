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
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class UserToNameExport {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "id.my_user",
                    column = @Column(name = "id_my_user")),
            @AttributeOverride(name = "id.video_name_export",
                    column = @Column(name = "id_video_name_export"))
    })
    private EmbeddedKeyUserToNameExport id;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_my_user", insertable = false,
            updatable = false)
    private MyUser myUser;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_video_name_export", insertable = false,
            updatable = false)
    private VideoNameExport videoNameExport;

    @Column(name = "id_video_name_export",insertable = false, updatable = false)
    private Long id_video_name_export;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModif;

    @NotNull
    private boolean active;

    @Nullable
    @Size(max = 32)
    private String permission;

    public UserToNameExport(MyUser myUser,
                            VideoNameExport videoNameExport) {
        this.myUser = myUser;
        this.videoNameExport = videoNameExport;
        this.id = new EmbeddedKeyUserToNameExport(
                myUser.getIdMyUser(),
                videoNameExport.getIdVideoNameExport());
    }

}
