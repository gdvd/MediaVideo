package fr.gdvd.media_manager.entitiesMysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PreferencesSubscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;//Id DB

    @NotNull
    @Size(max = 32)
    private String idToSub;//Id of what is being monitored

    private String name;//Name's actor/director...

    @Size(max = 16)
    private String idname;//Id NMxxx of personality

    private boolean active;

    @Min(value=0)
    @Max(value=99)
    private int valueMin;
    @Min(value=0)
    @Max(value=99)
    private int valueMax;

    @Min(value=0)
    @Max(value=1000)
    private int nbOfresultMin;
    @Min(value=0)
    @Max(value=1000)
    private int nbOfresultMax;

    /*@OneToMany(mappedBy = "preferencesSubscribe")
    private List<DateSubscribe> dateSubscribes = new ArrayList<>();*/

    @ManyToOne
    @JoinColumn(name="fk_my_user")
    private MyUser myUser;

}
