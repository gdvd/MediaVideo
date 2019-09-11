package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;

import java.util.List;

public interface ExportService {

//    List<VideoNameExport> getAllVideoNameExport();
    void toggleActivationExport(Long id);
    MyMediaInfo getOneMmi(String idmmi);
    List<String> getListUsers();
    void deleteExport(Long id);
}
