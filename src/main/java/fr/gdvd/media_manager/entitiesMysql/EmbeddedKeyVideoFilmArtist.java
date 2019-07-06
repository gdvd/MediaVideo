package fr.gdvd.media_manager.entitiesMysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable @ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddedKeyVideoFilmArtist implements Serializable {

    @Column(name = "id_video_film", columnDefinition="varchar(16)")
    private String idVideoFilm;

    @Column(name = "id_video_artist", columnDefinition="varchar(16)")
    private String idVideoArtist;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmbeddedKeyVideoFilmArtist that = (EmbeddedKeyVideoFilmArtist) o;
        return Objects.equals(idVideoFilm, that.idVideoFilm) &&
                Objects.equals(idVideoArtist, that.idVideoArtist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVideoFilm, idVideoArtist);
    }
}
