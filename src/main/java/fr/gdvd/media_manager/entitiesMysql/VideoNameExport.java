package fr.gdvd.media_manager.entitiesMysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity @Table(uniqueConstraints={@UniqueConstraint(columnNames={"nameExport"})})
public class VideoNameExport implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idVideoNameExport;
    @Size(max=255)
    private String nameExport;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModifNameExport;
    private boolean active;
    private boolean complete;

//    @JsonIgnore
    @OneToMany(mappedBy = "videoNameExport", cascade = {CascadeType.ALL})
    private List<VideoSupportPath> videoSupportPaths = new ArrayList<>();

//    @JsonIgnore
    @OneToMany(mappedBy = "videoNameExport", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<UserToNameExport> userToNameExports = new ArrayList<>();

    public VideoNameExport(@Size(max = 255) Long idVideoNameExport)
    {
        this.idVideoNameExport = idVideoNameExport;
    }
}
