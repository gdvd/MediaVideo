package fr.gdvd.media_manager.entitiesNoDb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BasketInfo {

    private String basketName;
    private Long userId;
    private String comment;
    private List<BasketInfoElement> basketInfoElements;

}
