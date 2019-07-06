package fr.gdvd.media_manager.entitiesNoDb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Msg {

    private String state;
    private String name;
    private String tokenLimit;
    private String url;
    private String info;
    private String navigator;
}
