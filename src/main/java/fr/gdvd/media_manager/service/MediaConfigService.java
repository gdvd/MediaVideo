package fr.gdvd.media_manager.service;

import org.bson.Document;

import java.util.List;

public interface MediaConfigService {

    public List<String> getAllPath();
    public List<Document> getAllPaths();
    public List<String> getEntryById(String id);
    public Document updateDocWithOnePath(String path, List<String> list);
}
