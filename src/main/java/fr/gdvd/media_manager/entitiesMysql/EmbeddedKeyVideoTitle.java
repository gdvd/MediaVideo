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
public class EmbeddedKeyVideoTitle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id_video")
    private String idVideo;

    @Column(name = "id_country")
    private Long idCountry;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmbeddedKeyVideoTitle that = (EmbeddedKeyVideoTitle) o;
        return Objects.equals(idVideo, that.idVideo) &&
                Objects.equals(idCountry, that.idCountry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVideo, idCountry);
    }
}
