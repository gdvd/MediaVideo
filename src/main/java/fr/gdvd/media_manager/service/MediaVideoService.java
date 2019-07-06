package fr.gdvd.media_manager.service;

//import org.bson.Document;

import java.util.List;
import java.util.Map;

public interface MediaVideoService {
    /*public String getOneNameByIdmd5(String id);
    public MediaVideo getById(String id);
    public MediaVideoLight getByIdLight(String id);
    public Document getOneVideoPartialInfo(String id, List<String> info, List<String> video, List<String> audio, List<String> text*//**//*);
    public List<Document> getSeveralVideoPartialInfo(List<String> ids, List<String> info, List<String> video, List<String> audio, List<String> text);
    public MediaVideoLight videoByIdLight(String id);
    public Document savedataid(Document id);
    public Map<String, Map<String, List<String>>> searchtitlecontain(String request);
    public void saveVideoLight(MediaVideoLight mediaVideoLight, String nameExport);
    public void saveVideo(MediaVideo mediaVideo, String nameExport);
    public List<MediaVideoLight> getInTitle(String keyword);
    public List<Document> videosByIdLight(List<String> ids);*/
}
