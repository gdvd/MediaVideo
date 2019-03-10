package fr.gdvd.media_manager.controller;

import fr.gdvd.media_manager.service.MediaVideoServiceImpl;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
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

    @GetMapping(value = {"/video/videoByIdToInfo/{id}", "/videoByIdToInfo/{id}/all"})
    public Document getOneVideoAllInfo(@PathVariable String id){
        Document doc = mediaVideoService.getAllInfo4OneNameByIdmd5(id);
        return doc;
    }

    @GetMapping(value = "/video/videoByIdToInfo/{id}/default")
    public Document getOneVideoPartialInfoDefault(@PathVariable String id){
        Document doc = mediaVideoService.getOneVideoPartialInfo(
                id,
                Arrays.asList("Format", "Duration", "FileSize", "Format_Version", "File_Modified_Date"), // To info
                Arrays.asList("CodecID", "Width", "Height", "BitRate"), // To video
                Arrays.asList("CodecID", "Language", "Forced"), // To  audio
                Arrays.asList("Language")); // To text
        return doc;
    }

    @GetMapping(value = "/video/videoByIdToInfo/{id}/light")
    public Document getOneVideoPartialInfoLight(@PathVariable String id){
        Document doc = mediaVideoService.getOneVideoPartialInfo(
                id,
                Arrays.asList("Duration", "FileSize", "File_Modified_Date"), // To info
                Arrays.asList("Format", "CodecID", "BitRate_Nominal"), // To video
                Arrays.asList(), // To  audio
                Arrays.asList()); // To text
        return doc;
    }
    @GetMapping(value = "/video/videoByIdToInfo/{id}/name")
    public Document getOneVideoPartialInfoName(@PathVariable String id){
        Document doc = mediaVideoService.getOneVideoPartialInfo(
                id,
                Arrays.asList(), // To info
                Arrays.asList(), // To video
                Arrays.asList(), // To  audio
                Arrays.asList()); // To text
        return doc;
    }

    @GetMapping(value = "/video/videoByIdToInfo")
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
    @GetMapping(value = "/video/searchtitlecontain")
    public List<Map<String, List<String>>> searchtitlecontain(@RequestBody @NonNull String request){
        return mediaVideoService.searchtitlecontain(request);
    }
    @GetMapping(value = "/video/searchtitleregex")
    public List<Map<String, List<String>>> searchtitleregex(@RequestBody @NonNull String request){
        return mediaVideoService.searchtitleregex(request);
    }

    /********************************
     *  Request for several video
     *******************************/

   /* @GetMapping(value = "/video/videoByIdsToInfo")
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

    @PostMapping(value="/video/saveid")
    public Document saveid(@RequestBody @NotNull Document dataid){
        return mediaVideoService.savedataid(dataid);
    }

}
