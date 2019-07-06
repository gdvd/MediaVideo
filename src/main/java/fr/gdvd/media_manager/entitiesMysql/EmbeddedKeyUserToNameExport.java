package fr.gdvd.media_manager.entitiesMysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddedKeyUserToNameExport implements Serializable {

    @Column(name = "id_my_user", columnDefinition="bigint(20)")
    private Long idMyUser;

    @Column(name = "id_video_name_export", columnDefinition="bigint(20)")
    private Long idVideoNameExport;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmbeddedKeyUserToNameExport that = (EmbeddedKeyUserToNameExport) o;
        return Objects.equals(idMyUser, that.idMyUser) &&
                Objects.equals(idVideoNameExport, that.idVideoNameExport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMyUser, idVideoNameExport);
    }
}
