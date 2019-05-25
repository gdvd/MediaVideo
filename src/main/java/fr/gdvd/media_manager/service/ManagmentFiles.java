package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.entities.FileVideo;

import java.util.List;

public interface ManagmentFiles {

    public List<FileVideo> scanFolder(String urlFolder);

}
