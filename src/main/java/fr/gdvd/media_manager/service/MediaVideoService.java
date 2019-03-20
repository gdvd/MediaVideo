package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.entities.MediaVideo;
import fr.gdvd.media_manager.entities.MediaVideoLight;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

public interface MediaVideoService {
    public String getOneNameByIdmd5(String id);
    public MediaVideo getById(String id);
    public Document getOneVideoPartialInfo(String id, List<String> info, List<String> video, List<String> audio, List<String> text/**/);
    public List<Document> getSeveralVideoPartialInfo(List<String> ids, List<String> info, List<String> video, List<String> audio, List<String> text);
    public MediaVideoLight videoByIdLight(String id);
    public Document savedataid(Document id);
    public Map<String, Map<String, List<String>>> searchtitlecontain(String request);
}
