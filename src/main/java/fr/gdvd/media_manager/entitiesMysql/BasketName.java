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
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BasketName {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idBasketName;

    @NotNull
    @Size(max = 32)
    private String basketName;

    @Nullable
    @Size(max = 1024)
    private String comment;

    @JsonIgnore
    @OneToMany(mappedBy = "id_basket_name", cascade = {CascadeType.ALL})
    private List<Basket> baskets = new ArrayList<>();


}
