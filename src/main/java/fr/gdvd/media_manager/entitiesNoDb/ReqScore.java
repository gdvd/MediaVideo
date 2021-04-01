package fr.gdvd.media_manager.entitiesNoDb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReqScore implements Serializable {

    private String idtt;
    @Size(max=99)
    @Nullable
    private String usr;
    private int score;
    @Size(max = 1024)
    private String comment;

}
