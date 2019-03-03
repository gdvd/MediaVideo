package fr.gdvd.media_manager.service;

import org.bson.Document;

import java.util.List;

public interface MediaVideoService {
    public String getOneNameByIdmd5(String id);
    public Document getAllInfo4OneNameByIdmd5(String id);
    public Document getOneVideoPartialInfo(String id, List<String> info, List<String> video, List<String> audio, List<String> text/**/);
    public List<Document> getSeveralVideoPartialInfo(List<String> ids, List<String> info, List<String> video, List<String> audio, List<String> text);
    public Document getNameWithId(String id);

}
