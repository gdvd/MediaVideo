package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.dao.MediaVideoRepository;
import fr.gdvd.media_manager.entities.MediaVideo;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        doc.append("video", searchDataInList(mv.getVideo(), video));
        doc.append("audio", searchDataInList(mv.getAudio(), audio));
        doc.append("text", searchDataInList(mv.getText(), text));
        return doc;
    }

    @Override
    public List<Document> getSeveralVideoPartialInfo(List<String> ids, List<String> info, List<String> video, List<String> audio, List<String> text) {
        List<Document> ld = new ArrayList<>();
        for (String id:ids) {

        }
        return null;
    }

    @Override
    public Document getNameWithId(String id) {
        MediaVideo mv = mediaVideoRepository.findById(id).orElse(null);
        if (mv == null) return null;
        Document doc = new Document("id", id)
                .append("urlFile", mv.getUrlFile());
        return null;
    }

    @Override
    public Document savedataid(Document dataid) {
        MediaVideo mv = new MediaVideo();
        mv.setId((String) dataid.get("id"));
        mv.setUrlFile((List<Map<String, String>>) dataid.get("urlFile"));
        mv.setInfo((Map<String, Object>) dataid.get("info"));
        mv.setVideo((List<Map<String, Object>>) dataid.get("video"));
        mv.setAudio((List<Map<String, Object>>) dataid.get("audio"));
        mv.setText((List<Map<String, Object>>) dataid.get("text"));
        mediaVideoRepository.save(mv);
        return dataid;
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

    private List<Map<String, Object>> searchDataInList(Object objMV, List<String> dataNeeded){
        List<Map<String, Object>> res = new ArrayList<>();
        if(objMV instanceof Map){
            Map<String, Object> mp = searchData((Map<String, Object>) objMV, dataNeeded);
            res.add(mp);
        }
        if(objMV instanceof List){
            for (Object ob: (List)objMV) {
                if(ob instanceof Map){
                    Map<String, Object> mp = searchData((Map<String, Object>) ob, dataNeeded);
                    res.add(mp);
                }
            }
        }
        return res;
    }

}
