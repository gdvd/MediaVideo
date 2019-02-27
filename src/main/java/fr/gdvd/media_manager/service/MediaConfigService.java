package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.dao.MediaConfigRepository;

import java.util.List;

public interface MediaConfigService {

    public List<String> getAllPath();
    public List<String> getEntryById(String id);

}
