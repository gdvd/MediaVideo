package fr.gdvd.media_manager.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Usrnewpassword {

    private String id;
    private String login;
    private String passwordold;
    private String passwordnew;
    private List<String> roles;

}
