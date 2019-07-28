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
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class TypeName {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idTypeName;

    @Size(max = 16)
    private String typeName;

    @JsonIgnore
    @OneToMany(mappedBy = "typeName")
    List<TypeMmi> typeMmis = new ArrayList<>();


}
