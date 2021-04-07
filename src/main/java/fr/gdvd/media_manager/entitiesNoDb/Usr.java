package fr.gdvd.media_manager.entitiesNoDb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class Usr {
    private Long id;
    private String login;
    private String password;
    private String apiKey;
    private String nickname;
    private List<String> roles;
}
