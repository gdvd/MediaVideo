package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.dao.MediaConfigRepository;
import fr.gdvd.media_manager.entities.MediaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MediaConfigServiceImpl implements MediaConfigService {

    @Autowired
    private MediaConfigRepository mediaConfigRepository;

    @Override
    public List<MediaConfigRepository> getAllList() {
        MediaConfig mc = (MediaConfig) mediaConfigRepository.findAll();
        return null;
    }

    @Override
    public List<String> getOneList(String pathId) {
        return null;
    }
}
