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
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class VideoSupportPath implements Serializable {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "id.myMediaInfo",
                    column = @Column(name = "id_my_media_info", columnDefinition="varchar(32)")),
            @AttributeOverride(name = "id.video_name_export",
                    column = @Column(name = "id_video_name_export")),
            @AttributeOverride(name = "id.title",
                    column = @Column(name = "title")),
            @AttributeOverride(name = "id.path_general",
                    column = @Column(name = "pathGeneral"))
    })
    private EmbeddedKeyVideoSupportPath id;

    @NotNull
    private boolean active;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModif;

    @Nullable
    @Size(max = 8)
    private String type;

    @Column(name = "id_video_name_export",insertable = false, updatable = false)
    private Long id_video_name_export;


    @Column(name = "id_my_media_info",insertable = false, updatable = false)
    private String id_my_media_info;

    @Column(name = "title",insertable = false, updatable = false,
            columnDefinition="varchar(255)")
    private String title;

    @Column(name = "pathGeneral",insertable = false, updatable = false,
            columnDefinition="varchar(255)")
    private String pathGeneral;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_my_media_info", insertable = false,
            updatable = false, columnDefinition="varchar(32)")
    private MyMediaInfo myMediaInfo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_video_name_export", insertable = false,
            updatable = false)
    private VideoNameExport videoNameExport;

    public VideoSupportPath(@Size(max = 255) String title,@Size(max = 255) String pathGeneral,
                            MyMediaInfo myMediaInfo,
                            VideoNameExport videoNameExport) {
        this.myMediaInfo = myMediaInfo;
        this.videoNameExport = videoNameExport;
        this.id = new EmbeddedKeyVideoSupportPath(myMediaInfo.getIdMyMediaInfo(),
                videoNameExport.getIdVideoNameExport(), title, pathGeneral);
    }
    public VideoSupportPath(@Size(max = 255) String title,@Size(max = 255) String pathGeneral,
                            String idMyMediaInfo, Long idVideoNameExport) {
        this.myMediaInfo = new MyMediaInfo(idMyMediaInfo);
        this.videoNameExport = new VideoNameExport(idVideoNameExport);
        this.id = new EmbeddedKeyVideoSupportPath(myMediaInfo.getIdMyMediaInfo(),
                videoNameExport.getIdVideoNameExport(), title, pathGeneral);
    }
}
