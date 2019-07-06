package fr.gdvd.media_manager.entitiesMysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;

@Entity @Data @ToString @NoArgsConstructor @AllArgsConstructor
public class Preferences {

    @Id @Size(max = 8)
    private String idPreferences;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModifPref;

//    @Column(length=64)
    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn(name="information")
    Map<String, String> prefmap = new HashMap<>();

    @ElementCollection
    @OrderColumn(name="order_extention")
    private Set<String> extset = new HashSet<>();

    @OneToMany(mappedBy = "preferences")
    private List<ItemToSearch> itemToSearches = new ArrayList<>();

}
