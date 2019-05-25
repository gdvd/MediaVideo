package fr.gdvd.media_manager.controller;

import fr.gdvd.media_manager.entities.MediaVideo;
import fr.gdvd.media_manager.entities.MediaVideoLight;
import fr.gdvd.media_manager.service.MediaVideoServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Log4j2
@RestController
@RequestMapping(value = "video")
public class MediaVideoController {

    @Autowired
    private MediaVideoServiceImpl mediaVideoService;

    /**************************
    *  Request for one video
    ***************************/


    @GetMapping(value = {"/getVideoById/{id}"},
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MediaVideo getById(@PathVariable String id){
        MediaVideo mv = mediaVideoService.getById(id);
        return mv;
    }
    // getVideoByIdLight

    @GetMapping(value = {"/getVideoByIdLight/{id}"},
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MediaVideoLight getByIdLight(@PathVariable String id){
        MediaVideoLight mv = mediaVideoService.getByIdLight(id);
        return mv;
    }

/*    @GetMapping(value = "/getVideoByIdToInfo/{id}/default")
    public Document getByIdDefault(@PathVariable String id){
        Document doc = mediaVideoService.getOneVideoPartialInfo(
                id,
                Arrays.asList("Format", "Duration", "FileSize", "Format_Version", "File_Modified_Date"), // To info
                Arrays.asList("CodecID", "Width", "Height", "BitRate"), // To video
                Arrays.asList("CodecID", "Language", "Forced"), // To  audio
                Arrays.asList("Language")); // To text
        return doc;
    }*/

    @GetMapping(value = "/videoByIdLight/{id}",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MediaVideoLight getOneVideoPartialInfoLight(@PathVariable String id){
        return mediaVideoService.videoByIdLight(id);
    }

    @GetMapping(value = "/videoByIdLightToDoc/{id}")
    public Document videoByIdLightToDoc(@PathVariable String id){
        MediaVideoLight mvl = mediaVideoService.videoByIdLight(id);
        Document doc = new Document("id", mvl.getId())
                .append("info", Arrays.asList(mvl.getInfo()))
                .append("video", Arrays.asList(mvl.getVideo()))
                .append("audio", Arrays.asList(mvl.getAudio()))
                .append("text", Arrays.asList(mvl.getText()))
                .append("title", Arrays.asList(mvl.getTitle()));
        return doc;
    }

    @PostMapping(value = "/videoByIdLightToDocs",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public List<Document> videoByIdLightToDocs(@RequestBody List<String> ids){
        return mediaVideoService.videosByIdLight(ids);
    }

    @GetMapping(value = "/videoByIdToInfo/{id}/name")
    public Document getOneVideoPartialInfoName(@PathVariable String id){
        Document doc = mediaVideoService.getOneVideoPartialInfo(
                id,
                Arrays.asList(), // To info
                Arrays.asList(), // To video
                Arrays.asList(), // To  audio
                Arrays.asList()); // To text
        return doc;
    }

    @GetMapping(value = "/videoByIdToInfo/")
    public Document getOneVideoPartialInfo(
            @RequestBody @NonNull ObjectRequestBody objectRequestBody){
        Document doc = null;
        //TODO: test this
        String idStr = mediaVideoService.getOneNameByIdmd5(objectRequestBody.getId());
        if (idStr=="")return doc;
        doc = mediaVideoService.getOneVideoPartialInfo(
                objectRequestBody.getId(),
                objectRequestBody.getInfo(),
                objectRequestBody.getVideo(),
                objectRequestBody.getAudio(),
                objectRequestBody.getText()
        );
        return doc;
    }
    /*@GetMapping(value = "/searchtitlecontain/{req}")
    public Map<String, Map<String, List<String>>> searchtitlecontain(
            @PathVariable @NonNull String req){
        return mediaVideoService.searchtitlecontain(req);
    }*/
    /*@GetMapping(value = "/searchtitleregex")
    public List<Map<String, List<String>>> searchtitleregex(@RequestBody @NonNull String request){
        return mediaVideoService.searchtitleregex(request);
    }*/

    @GetMapping(value = "/findInTitle/{req}")
    public List<MediaVideoLight> findInTitle(
            @PathVariable String req){
        return mediaVideoService.getInTitle(req);
    }


    /********************************
     *  Request Save video
     *******************************/

    @PostMapping(value = "/saveVideoLight/{nameExport}")
    public ResponseEntity<Void> saveVideoLight(
            @PathVariable String nameExport,
            @RequestBody @NonNull MediaVideoLight mediaVideoLight,
            UriComponentsBuilder ucBuilder){
        mediaVideoService.saveVideoLight(mediaVideoLight, nameExport);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Save","OK");
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @PostMapping(value = "/saveVideo/{nameExport}")
    public void saveVideo(
            @PathVariable String nameExport,
            @RequestBody @NonNull MediaVideo mediaVideo){
        mediaVideoService.saveVideo(mediaVideo, nameExport);
    }


    /********************************
     *  Request for several video
     *******************************/


    /*@PostMapping(value="/saveid")
    public Document saveid(@RequestBody @NotNull Document dataid){

        return mediaVideoService.savedataid(dataid);
    }*/

}
