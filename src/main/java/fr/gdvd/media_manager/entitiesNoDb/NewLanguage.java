package fr.gdvd.media_manager.entitiesNoDb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewLanguage {

    @NotNull
    private String language;
    @NotNull
    private String idMmiInEdition;
    @NotNull
    @Size(min = 1)
    private Long idLanguageInEdition;


}
