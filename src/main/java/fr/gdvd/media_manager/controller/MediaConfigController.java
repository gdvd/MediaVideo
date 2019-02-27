package fr.gdvd.media_manager.controller;

import fr.gdvd.media_manager.service.MediaConfigServiceImpl;
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

    @GetMapping(value="/path/{id}")
    public List<String> getOnePathByPathVariable(@PathVariable String id) {
        return mediaConfigService.getEntryById(id);
    }



}
