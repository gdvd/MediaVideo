package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.dao.MediaVideoLightRepository;
import fr.gdvd.media_manager.dao.MediaVideoRepository;
import fr.gdvd.media_manager.entities.MediaVideo;
import fr.gdvd.media_manager.entities.MediaVideoLight;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MediaVideoServiceImpl implements MediaVideoService {

    @Autowired
    private MediaVideoRepository mediaVideoRepository;
    @Autowired
    private MediaVideoLightRepository mediaVideoLightRepository;

    @Override
    public String getOneNameByIdmd5(String id) {
        MediaVideo mv = mediaVideoRepository.findById(id).orElse(null);
        if (mv == null) return "";
//        String str = (mv.getUrlFile().get(0).values().toArray()[0].toString());
        return null /*str*/;
    }

    @Override
    public MediaVideo getById(String id) {
        MediaVideo mv = mediaVideoRepository.getById(id);
        /*if (mv == null) return null;
        Document doc = new Document("id", id)
//                .append("urlFile", mv.getUrlFile())
                .append("info", mv.getInfo())
                .append("video", mv.getVideo())
                .append("audio", mv.getAudio())
                .append("text", mv.getText());*/
        return mv;
    }

    @Override
    public Document getOneVideoPartialInfo(String id, List<String> info, List<String> video, List<String> audio, List<String> text/**/) {
        MediaVideo mv = mediaVideoRepository.findById(id).orElse(null);
        if (mv == null) return null;
        Document doc = new Document("id", id)
//                .append("urlFile", mv.getUrlFile())
                ;
//        doc.append("info", searchData(mv.getInfo(), info));
        doc.append("video", searchDataInList(mv.getVideo(), video));
        doc.append("audio", searchDataInList(mv.getAudio(), audio));
        doc.append("text", searchDataInList(mv.getText(), text));
        return doc;
    }

    @Override
    public List<Document> getSeveralVideoPartialInfo(List<String> ids, List<String> info, List<String> video, List<String> audio, List<String> text) {
        List<Document> ld = new ArrayList<>();
        for (String id : ids) {

        }
        return null;
    }

    @Override
    public MediaVideoLight videoByIdLight(String id) {
        return mediaVideoLightRepository.getById(id);
    }

    @Override
    public Document savedataid(Document dataid) {

        /*MediaVideo mv = new MediaVideo();
//        mv.setId((String) dataid.get("id"));
//        mv.setUrlFile((List<Map<String, String>>) dataid.get("urlFile"));
//        mv.setInfo((Map<String, Object>) dataid.get("info"));
        mv.setVideo((List<Map<String, Object>>) dataid.get("video"));
        mv.setAudio((List<Map<String, Object>>) dataid.get("audio"));
        mv.setText((List<Map<String, Object>>) dataid.get("text"));
        mediaVideoRepository.save(mv);*/
        return null;
    }

    private Map<String, Object> searchData(Map<String, Object> objMV, List<String> dataNeeded) {
        Map<String, Object> mp = new HashMap<>();
        if (dataNeeded == null) return mp;
        for (String strNeeded : dataNeeded) {
            for (Map.Entry<String, Object> objAvaible : objMV.entrySet()) {
                if (objAvaible.getKey().equals(strNeeded)) {
                    mp.put(strNeeded, objAvaible.getValue());
                    break;
                }
            }
        }
        return mp;
    }

    private List<Map<String, Object>> searchDataInList(Object objMV, List<String> dataNeeded) {
        List<Map<String, Object>> res = new ArrayList<>();
        if (objMV instanceof Map) {
            Map<String, Object> mp = searchData((Map<String, Object>) objMV, dataNeeded);
            res.add(mp);
        }
        if (objMV instanceof List) {
            for (Object ob : (List) objMV) {
                if (ob instanceof Map) {
                    Map<String, Object> mp = searchData((Map<String, Object>) ob, dataNeeded);
                    res.add(mp);
                }
            }
        }
        return res;
    }

    public Map<String, Map<String, List<String>>> searchtitlecontain(String req) {

        byte[] decodedBytes = Base64.getDecoder().decode(req);
        String request = new String(decodedBytes);

        /*Map<String, Map<String, List<String>>> res = new HashMap<>();
        List<MediaVideo> lmv = mediaVideoRepository.findAll();
        for (MediaVideo mv : lmv) {
            List<Map<String, String>> ls = mv.getUrlFile();
            for (Map<String, String> mp : ls) {
                for (Map.Entry<String, String> m: mp.entrySet()) {
                    if (m.getValue().toLowerCase().contains(request.toLowerCase())) {
                        //We found it, write it in res
                        res = addElementToMap(res, m.getValue(), mv.getId(), m.getKey());
                    }
                }
            }
        }*/

        return null /*res*/;
    }

    private Map<String, Map<String, List<String>>> addElementToMap(
            Map<String, Map<String, List<String>>> res, String titleFound, String idStr, String pathid) {
        if(res.size()==0){
            Map<String, List<String>> re = new HashMap<>();
            List<String> l = new ArrayList<>();
            l.add(titleFound);
            re.put(idStr, l);
            res.put(pathid, re);
        }else{
            for(Map.Entry<String, Map<String, List<String>>>r : res.entrySet()){
                if(r.getKey().equals(pathid)){
                    for(Map.Entry<String, List<String>> re: r.getValue().entrySet()){
                        if(re.getKey().equals(idStr)){
                            List<String> lf = re.getValue();
                            lf.add(titleFound);
                            Map<String, List<String>> ress = new HashMap<>();
                            ress.put(idStr, lf);
                            res.put(pathid, ress);
                            return res;
                        }
                    }

                    List<String> lf = new ArrayList<>();
                    lf.add(titleFound);
                    Map<String, List<String>> msl = new HashMap<>();
                    msl.putAll(res.get(pathid));
                    msl.put(idStr, lf);
                    res.replace(pathid, res.get(pathid), msl);
                    return res;
                }/*else{
                    Map<String, List<String>> re = new HashMap<>();
                    List<String> l = new ArrayList<>();
                    l.add(titleFound);
                    re.put(idStr, l);
                    res.put(pathid, re);
                    return res;
                }*/
            }
        }
        return res;
    }

    /*public List<MediaVideo> doSomething(String idPath) {
        mediaVideoRepository.findAll(eq("_id", "iDs_by_path"), addToSet("path", eq("", "")));
        //Sol3(idem) set("path", set(# , set(path, list)))
        return mediaVideoRepository.findAll();
    }*/
}
