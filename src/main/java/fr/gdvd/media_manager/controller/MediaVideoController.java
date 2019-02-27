package fr.gdvd.media_manager.controller;

import fr.gdvd.media_manager.entities.MediaVideo;
import fr.gdvd.media_manager.service.MediaVideoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MediaVideoController {

    @Autowired
    private MediaVideoServiceImpl mediaVideoService;

    @GetMapping(value = "/video/{id}")
    public String getOneVideo(@PathVariable String id){
        String str = mediaVideoService.getOne(id);
        return str;
    }

}
