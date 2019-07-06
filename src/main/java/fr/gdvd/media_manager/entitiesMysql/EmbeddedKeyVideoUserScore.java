package fr.gdvd.media_manager.entitiesMysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddedKeyVideoUserScore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id_video")
    private String idVideo;

    @Column(name = "id_my_user")
    private Long idMyUser;

}
