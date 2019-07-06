package fr.gdvd.media_manager.entitiesMysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Data
@ToString
@Entity
@NoArgsConstructor @AllArgsConstructor
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"role"})})
public class MyRole  {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idMyRole;

    @Size(min = 2, max = 16)
    private String role;

    /*@JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private List<MyUser> myUsers = new ArrayList<>();*/

}
