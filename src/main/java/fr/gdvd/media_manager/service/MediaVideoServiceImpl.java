package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.dao.MediaVideoLightRepository;
import fr.gdvd.media_manager.dao.MediaVideoRepository;
import fr.gdvd.media_manager.entities.MediaVideo;
import fr.gdvd.media_manager.entities.MediaVideoLight;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.text.Normalizer;
import java.util.*;

@Log4j2
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
        return mv;
    }
    @Override
    public MediaVideoLight getByIdLight(String id) {
        MediaVideoLight mv = mediaVideoLightRepository.getById(id);
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

    @Override
    public void saveVideoLight(MediaVideoLight mediaVideoLight, String nameExport) {

        List<Map<String, String>> oldTitles = mediaVideoLight.getTitle();

        if(oldTitles.size()>1){
            for(Map<String, String> m: oldTitles){
                if(m.containsKey(nameExport)){
                    List<Map<String, String>> lmpo = new ArrayList<>();
                    lmpo.add(m);
                    mediaVideoLight.setTitle(lmpo);
                    break;
                }
            }
        }

        MediaVideoLight mediaVideoDb =
                mediaVideoLightRepository.getById(mediaVideoLight.getId());
        if(mediaVideoDb!=null){
            List<Map<String, String>> dbTitles = mediaVideoDb.getTitle();
            List<Map<String, String>> newTitles = mediaVideoLight.getTitle();
            Map<String, String> mpn = new HashMap<>();
            for(Map<String, String> m: dbTitles){
                if(!m.containsKey(nameExport)){
                    mpn = m;
                    newTitles.add(mpn);
                }
            }
            mediaVideoLight.setTitle(oldTitles);
        }
        mediaVideoLightRepository.save(mediaVideoLight);
    }

    @Override
    public void saveVideo(MediaVideo mediaVideo, String nameExport) {

        /*List<String> oldTitles = mediaVideo.getTitle();

        if(oldTitles.size()>1){
            for(List<String> m: oldTitles){
                if(m.containsKey(nameExport)){
                    List<Map<String, String>> lmpo = new ArrayList<>();
                    lmpo.add(m);
                    mediaVideo.setTitle(lmpo);
                    break;
                }
            }
        }

        MediaVideo mediaVideoDb =
                mediaVideoRepository.getById(mediaVideo.getId());
        if(mediaVideoDb!=null){
            List<Map<String, String>> dbTitles = mediaVideoDb.getTitle();
            List<Map<String, String>> newTitles = mediaVideo.getTitle();
            Map<String, String> mpn = new HashMap<>();
            for(Map<String, String> m: dbTitles){
                if(!m.containsKey(nameExport)){
                    mpn = m;
                    newTitles.add(mpn);
                }
            }
            mediaVideo.setTitle(oldTitles);
        }

        mediaVideoRepository.save(mediaVideo);*/
    }

    @Override
    public List<MediaVideoLight> getInTitle(String keyword) {
        List<MediaVideoLight> mediaVideoLightInDB = mediaVideoLightRepository.findAll();
        List<MediaVideoLight> mediaVideoLightFound = new ArrayList<>();
        for(MediaVideoLight mvl: mediaVideoLightInDB){
            for (Map<String, String> m: mvl.getTitle()){
                if((m.values().toString()).toLowerCase().contains(keyword)){
                    mediaVideoLightFound.add(mvl);
                }
            }
        }
        return mediaVideoLightFound;
    }

    @Override
    public List<Document> videosByIdLight(List<String> ids) {
        List<Document> ldoc = new ArrayList<>();
        for(String id: ids){
            MediaVideoLight mvl =
                    mediaVideoLightRepository.getById(id);
            Document doc = new Document("id", mvl.getId())
                    .append("info", Arrays.asList(mvl.getInfo()))
                    .append("video", Arrays.asList(mvl.getVideo()))
                    .append("audio", Arrays.asList(mvl.getAudio()))
                    .append("text", Arrays.asList(mvl.getText()))
                    .append("title", Arrays.asList(mvl.getTitle()));
            ldoc.add(doc);
        }
        return ldoc;
    }

    private int hache(@NotNull String data){
        return data.hashCode();
    }

    public String sansAccents(String source) {
        String normalized = Normalizer.normalize(source, Normalizer.Form.NFD);
        return normalized.replaceAll("[\u0300-\u036F]", "");
    }

    /*public static String sansAccents(String s) { //With ICU4J on https://mvnrepository.com/artifact/com.ibm.icu/icu4j
        Transliterator accentsconverter = Transliterator.getInstance("NFD; [:M:] Remove; NFC; ");
        return accentsconverter.transliterate(s);
    }*/

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
