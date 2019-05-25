package fr.gdvd.media_manager.service;

import org.springframework.web.multipart.MultipartFile;


public interface VideoAdminService {

    public int saveData(String pahtdirids, MultipartFile[] file);

}
