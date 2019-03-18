package fr.gdvd.media_manager.controller;

import fr.gdvd.media_manager.service.MediaVideoServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping(value = "video")
public class MediaVideoController {

    @Autowired
    private MediaVideoServiceImpl mediaVideoService;

    /**************************
    *  Request for one video
    ***************************/

   /* @GetMapping(value = "/video/videoByIdToInfo/{id}/name")
    public String getOneVideoName(@PathVariable String id){
        String str = mediaVideoService.getOneNameByIdmd5(id);
        return str;
    }*/

    @GetMapping(value = {"/videoByIdToInfo/{id}", "/videoByIdToInfo/{id}/all"})
    public Document getOneVideoAllInfo(@PathVariable String id){
        Document doc = mediaVideoService.getAllInfo4OneNameByIdmd5(id);
        return doc;
    }

    @GetMapping(value = "/videoByIdToInfo/{id}/default")
    public Document getOneVideoPartialInfoDefault(@PathVariable String id){
        Document doc = mediaVideoService.getOneVideoPartialInfo(
                id,
                Arrays.asList("Format", "Duration", "FileSize", "Format_Version", "File_Modified_Date"), // To info
                Arrays.asList("CodecID", "Width", "Height", "BitRate"), // To video
                Arrays.asList("CodecID", "Language", "Forced"), // To  audio
                Arrays.asList("Language")); // To text
        return doc;
    }

    @GetMapping(value = "/videoByIdToInfo/{id}/light")
    public Document getOneVideoPartialInfoLight(@PathVariable String id){
        Document doc = mediaVideoService.getOneVideoPartialInfo(
                id,
                Arrays.asList("Duration", "FileSize", "File_Modified_Date"), // To inf0
                Arrays.asList("Format", "CodecID", "BitRate_Nominal"), // To video
                Arrays.asList(), // To  audio
                Arrays.asList()); // To text
        return doc;
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
    public Document getOneVideoPartialInfo(@RequestBody @NonNull ObjectRequestBody objectRequestBody){
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
    @GetMapping(value = "/searchtitlecontain/{req}")
    public Map<String, Map<String, List<String>>> searchtitlecontain(@PathVariable @NonNull String req){
        log.info("Je suis passe par la, request : "+req);
        return mediaVideoService.searchtitlecontain(req);
    }
    /*@GetMapping(value = "/searchtitleregex")
    public List<Map<String, List<String>>> searchtitleregex(@RequestBody @NonNull String request){
        return mediaVideoService.searchtitleregex(request);
    }*/

    /********************************
     *  Request for several video
     *******************************/

   /* @GetMapping(value = "/videoByIdsToInfo")
    public List<Document> getSeveralVideoPartialInfo(@RequestBody @NonNull ObjectRequestBody objectRequestBody){
        List<Document> doc = mediaVideoService.getSeveralVideoPartialInfo(
                objectRequestBody.getIds(),
                objectRequestBody.getInfo(),
                objectRequestBody.getVideo(),
                objectRequestBody.getAudio(),
                objectRequestBody.getText()
        );
        return doc;
    }*/

    @PostMapping(value="/saveid")
    public Document saveid(@RequestBody @NotNull Document dataid){
        return mediaVideoService.savedataid(dataid);
    }

}
