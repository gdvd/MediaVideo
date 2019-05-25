package fr.gdvd.media_manager.controller;

import fr.gdvd.media_manager.entities.FileVideo;
import fr.gdvd.media_manager.service.ManagmentFilesImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping(value = "managment")
public class ManagmentFilesController {

    @Autowired
    private ManagmentFilesImpl managmentFiles;

    @GetMapping("/selectFolder")
    public List<FileVideo> selectFolder(
            @RequestBody String urlFolder){
        return managmentFiles.scanFolder(urlFolder);
    }

}
