package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.dao.MediaVideoRepository;
import fr.gdvd.media_manager.entities.MediaVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MediaVideoServiceImpl implements MediaVideoService {

    @Autowired
    private MediaVideoRepository mediaVideoRepository;

    @Override
    public List<String> listMediaVideoByPath(String path) {
        return null;
    }

    @Override
    public MediaVideo getListOfPath() {
        return null;
    }
}
