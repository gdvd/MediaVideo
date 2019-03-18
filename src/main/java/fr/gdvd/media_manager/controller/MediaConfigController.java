package fr.gdvd.media_manager.controller;

import fr.gdvd.media_manager.service.FindInPath;
import fr.gdvd.media_manager.service.MediaConfigService;
import fr.gdvd.media_manager.service.MediaConfigServiceImpl;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("path")
public class MediaConfigController {

/*    @Autowired
    MediaConfigService mediaConfigService;
    @Autowired
    FindInPath findInPath;

    @GetMapping(value="/allkeys")
    public List<String> getAllPath() {
        return mediaConfigService.getAllPath();
    }

    @GetMapping(value="/allPaths")
    public List<Document> getAllPaths() {
        return mediaConfigService.getAllPaths();
    }
    //getAllPathsToMap
    @GetMapping(value="/getAllPathsToMap")
    public List<Map<String, List<String>>> getAllPathsToMap() {
        return mediaConfigService.getAllPathsToMap();
    }

    @GetMapping(value="/{id}")
    public List<String> getIdByPathVariable(@PathVariable String id) {
        return mediaConfigService.getEntryById(id);
    }

    @PostMapping(value = "/findNbIdInAllPaths/{id}")
    public Map<String, Integer> findNbIdInAllPaths(@PathVariable String id, @RequestBody List<Map<String, List<String>>> list){
        return findInPath.findNbIdInAllPaths(list, id);
    }

    @PatchMapping(value = "/saveOnePath/{path}")
    public Document updateDocWithOnePath(@PathVariable String path,
                                @RequestBody List<String> list){ return mediaConfigService.updateDocWithOnePath(path, list);}*/


}
