package fr.gdvd.media_manager.entitiesMysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"login"})})
public class MyUser implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idMyUser;

    @NotNull
    @Size(min = 2, max = 16)
    private String login;

    @NotNull
    @Size(max = 255)
    private String password;

    @NotNull
    private boolean active;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModif;

    @ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="my_users2roles",
            joinColumns = {@JoinColumn(name="fkUser")},
            inverseJoinColumns={@JoinColumn(name="fkRole")})
    private List<MyRole> roles = new ArrayList<>();

    @Nullable
    @JsonIgnore
    @OneToMany(mappedBy = "myUser", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<VideoUserScore> videoUserScores = new ArrayList<>();

    @Nullable
    @JsonIgnore
    @OneToMany(mappedBy = "myUser", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<UserToNameExport> userToNameExports = new ArrayList<>();

    @Nullable
    @JsonIgnore
    @OneToMany(mappedBy = "myUser", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Basket> baskets = new ArrayList<>();

/*    @Nullable
    @JsonIgnore
    @OneToMany(mappedBy = "myUser", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<VideoNameExport> videoNameExports = new ArrayList<>();*/


}

