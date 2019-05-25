package fr.gdvd.media_manager.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data @NoArgsConstructor @AllArgsConstructor
public class FileVideo implements Serializable {

    private String id;
    private String infoMediaReader;
    private String path;
    private boolean activate;

}
