package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.dao.MediaConfigRepository;
import fr.gdvd.media_manager.dao.MediaVideoRepository;
import fr.gdvd.media_manager.entities.MediaConfig;
import fr.gdvd.media_manager.entities.MediaVideo;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class MediaConfigServiceImpl implements MediaConfigService {

    @Autowired
    private MediaConfigRepository mediaConfigRepository;
    @Autowired
    private MediaVideoRepository mediaVideoRepository;

    @Override
    public List<String> getAllPath() {
        List<MediaConfig> listMediaConfig = mediaConfigRepository.findAll();
        List<String> listKeyword = new ArrayList<>();

        for (MediaConfig res : listMediaConfig) {
            if (res.getId().equals("IDs_by_path")) {
                List<Map<String, List<String>>> map = res.getPath();
                for (Map<String, List<String>> lmc : map) {
                    listKeyword.add(lmc.keySet().toArray()[0].toString());
                }
            }
        }
        return listKeyword;
    }

    @Override
    public List<String> getEntryById(String id) {
        List<MediaConfig> listMediaConfig = mediaConfigRepository.findAll();
        List<String> listResult = new ArrayList<>();

        for (MediaConfig res : listMediaConfig) {
            if (res.getId().equals("IDs_by_path")) {
                for (Map<String, List<String>> lmc : res.getPath()) {
                    if (((lmc.keySet()).toArray()[0]).equals(id)) {
                        for (List<String> lstr : lmc.values()) {
                            listResult.addAll(lstr);
                        }
                    }
                }
            }
        }
        return listResult;
    }


}
