package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.entities.MediaPath;

import java.util.List;

public interface MediaPathService {

    public MediaPath getOnePath(String path);
    public List<MediaPath> getAllPath();
}
