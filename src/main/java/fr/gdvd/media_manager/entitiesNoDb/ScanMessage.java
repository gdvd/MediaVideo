package fr.gdvd.media_manager.entitiesNoDb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class ScanMessage {

    private String message;
    private String pathVideo;
    private int minSizeOfVideoFile;
    private List<String> extentionsNotRead;
    private List<String> extentionsRead;
    private List<String> filesRead;


}
