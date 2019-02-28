package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.dao.MediaVideoRepository;
import fr.gdvd.media_manager.entities.MediaVideo;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MediaVideoServiceImpl implements MediaVideoService {

    @Autowired
    private MediaVideoRepository mediaVideoRepository;

    @Override
    public String getOneNameByIdmd5(String id) {
        MediaVideo mv = mediaVideoRepository.findById(id).orElse(null);
        if (mv == null) return "";
        String str =  (mv.getUrlFile().get(0).values().toArray()[0].toString());
        return str;
    }

    @Override
    public Document getAllInfo4OneNameByIdmd5(String id) {
        MediaVideo mv = mediaVideoRepository.findById(id).orElse(null);
        if (mv == null) return null;
        Document doc = new Document("id", id)
                .append("urlFile", mv.getUrlFile())
                .append("info", mv.getInfo())
                .append("video", mv.getVideo())
                .append("audio", mv.getAudio())
                .append("text", mv.getText());
        return doc;
    }

    @Override
    public Document getOneVideoPartialInfo(String id, List<String> info, List<String> video, List<String> audio, List<String> text/**/) {
        MediaVideo mv = mediaVideoRepository.findById(id).orElse(null);
        if (mv == null) return null;
        Document doc = new Document("id", id)
                .append("urlFile", mv.getUrlFile());
        doc.append("info", searchData(mv.getInfo(), info));
        doc.append("video", searchData(mv.getVideo(), video));
        doc.append("audio", searchData(mv.getAudio(), audio));
        doc.append("text", searchData(mv.getText(), text));
        return doc;
    }
    private Map<String, Object> searchData(Map<String, Object> objMV, List<String> dataNeeded){
        Map<String, Object> mp = new HashMap<>();
        if(dataNeeded == null)return mp;
        for (String strNeeded:dataNeeded) {
            for (Map.Entry<String, Object> objAvaible: objMV.entrySet()) {
                if(objAvaible.getKey().equals(strNeeded)){
                    mp.put(strNeeded, objAvaible.getValue());
                    break;
                }
            }
        }
        return mp;
    }
}
