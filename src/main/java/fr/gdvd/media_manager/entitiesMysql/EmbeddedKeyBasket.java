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
public class EmbeddedKeyBasket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id_my_media_info")
    private String idMyMediaInfo;

    @Column(name = "id_my_user")
    private Long idMyUser;

    @Column(name = "id_basket_name")
    private Long idBasketName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmbeddedKeyBasket that = (EmbeddedKeyBasket) o;
        return Objects.equals(idMyMediaInfo, that.idMyMediaInfo) &&
                Objects.equals(idMyUser, that.idMyUser) &&
                Objects.equals(idBasketName, that.idBasketName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMyMediaInfo, idMyUser, idBasketName);
    }
}
