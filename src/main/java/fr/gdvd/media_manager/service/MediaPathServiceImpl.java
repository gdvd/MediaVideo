package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.dao.MediaPathRepository;
import fr.gdvd.media_manager.entities.MediaPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MediaPathServiceImpl implements MediaPathService {

    @Autowired
    private MediaPathRepository mediaPathRepository;

    @Override
    public MediaPath getOnePath(String path) {
        return mediaPathRepository.getByNameExportAndActive(path);
    }

    @Override
    public List<MediaPath> getAllPath() {
        List<MediaPath> col = new ArrayList<>();
        for (MediaPath t : mediaPathRepository.findAll())
            col.add(t);
        return col;
    }
}
