package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.entities.MediaVideo;

import java.util.List;

public interface MediaVideoService {

    public List<String> listMediaVideoByPath(String path);
    public MediaVideo getListOfPath();

}
