package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.entities.MediaVideo;
import org.bson.Document;

import java.util.List;
import java.util.Map;

public interface MediaVideoService {
    public String getOneNameByIdmd5(String id);
    public Document getAllInfo4OneNameByIdmd5(String id);
    public Document getOneVideoPartialInfo(String id, List<String> info, List<String> video, List<String> audio, List<String> text/**/);
    //public Map<String, Object> searchData(Map<String, Object> objMV, List<String> dataNeeded);

}
