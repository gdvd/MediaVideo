package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.dao.MediaConfigRepository;

import java.util.List;

public interface MediaConfigService {

    public List<MediaConfigRepository> getAllList();
    public List<String> getOneList(String pathId);

}
