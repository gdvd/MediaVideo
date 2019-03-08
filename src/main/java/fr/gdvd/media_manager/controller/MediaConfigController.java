package fr.gdvd.media_manager.controller;

import fr.gdvd.media_manager.service.MediaConfigServiceImpl;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MediaConfigController {

    @Autowired
    MediaConfigServiceImpl mediaConfigService;

    @GetMapping(value="/path/allkeys")
    public List<String> getAllPath() {
        return mediaConfigService.getAllPath();
    }

    @GetMapping(value="/path/allPaths")
    public List<Document> getAllPaths() {
        return mediaConfigService.getAllPaths();
    }

    @GetMapping(value="/path/{id}")
    public List<String> getIdByPathVariable(@PathVariable String id) {
        return mediaConfigService.getEntryById(id);
    }

    @PatchMapping(value = "/path/saveOnePath/{path}")
    public Document updateDocWithOnePath(@PathVariable String path,
                                @RequestBody List<String> list){ return mediaConfigService.updateDocWithOnePath(path, list);}


}
