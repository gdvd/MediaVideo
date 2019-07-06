package fr.gdvd.media_manager.entitiesMysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemToSearch {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idItemToSearch;
    @Size(max=64)
    private String itemImdb;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_preferences")
    private Preferences preferences;

    @Column(length=2014)
    @ElementCollection
    @OrderColumn(name="keyset")
    private Set<String> keyset = new HashSet<>();

}
