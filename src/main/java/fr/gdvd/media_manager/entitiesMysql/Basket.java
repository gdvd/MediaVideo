package fr.gdvd.media_manager.entitiesMysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Basket {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "id.MyMediaInfo",
                    column = @Column(name = "id_my_media_info")),
            @AttributeOverride(name = "id.MyUser",
                    column = @Column(name = "id_my_user")),
            @AttributeOverride(name = "id.BasketName",
                    column = @Column(name = "id_basket_name"))
    })
    private EmbeddedKeyBasket id;


    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModif;

    @Column(name = "id_basket_name",insertable = false, updatable = false)
    private String id_basket_name;

    @ManyToOne
    @JoinColumn(name = "id_my_media_info",insertable = false, updatable = false)
    private MyMediaInfo myMediaInfo;

    @ManyToOne
    @JoinColumn(name = "id_my_user",insertable = false, updatable = false)
    private MyUser myUser;


    public Basket(MyMediaInfo myMediaInfo, MyUser myUser, Long id_basket_name) {
        this.myMediaInfo = myMediaInfo;
        this.myUser = myUser;
        this.myMediaInfo = myMediaInfo;
        this.id= new EmbeddedKeyBasket(myMediaInfo.getIdMyMediaInfo(),
                myUser.getIdMyUser(), id_basket_name);
    }


}
