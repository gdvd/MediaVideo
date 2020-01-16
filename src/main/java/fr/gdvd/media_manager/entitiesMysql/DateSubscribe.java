package fr.gdvd.media_manager.entitiesMysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DateSubscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModif;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "fk_preferences_subscribe")
    private PreferencesSubscribe preferencesSubscribe;

}
