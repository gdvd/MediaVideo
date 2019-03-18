package fr.gdvd.media_manager.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class ObjectRequestBody {

    @NotNull
    public String id;
    @Nullable
    public List<String > ids;
    @Nullable
    public List<String> info;
    @Nullable
    public List<String> video;
    @Nullable
    public List<String> audio;
    @Nullable
    public List<String> text;


}
