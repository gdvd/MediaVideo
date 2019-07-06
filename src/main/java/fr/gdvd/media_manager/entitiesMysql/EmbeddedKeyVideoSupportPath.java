package fr.gdvd.media_manager.entitiesMysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddedKeyVideoSupportPath implements Serializable {

    @Column(name = "id_my_media_info", columnDefinition="varchar(32)")
    private String idMyMediainfo;

    @Column(name = "id_video_name_export")
    private Long idVideoNameExport;

    @Size(max = 255)
    @Column(name = "title")
    private String title;

    @Size(max = 255)
    @Column(name = "pathGeneral")
    private String pathGeneral;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmbeddedKeyVideoSupportPath that = (EmbeddedKeyVideoSupportPath) o;
        return Objects.equals(idMyMediainfo, that.idMyMediainfo) &&
                Objects.equals(idVideoNameExport, that.idVideoNameExport) &&
                Objects.equals(title, that.title) &&
                Objects.equals(pathGeneral, that.pathGeneral);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMyMediainfo, idVideoNameExport, title, pathGeneral);
    }
}
