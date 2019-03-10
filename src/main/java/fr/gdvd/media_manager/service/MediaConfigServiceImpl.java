package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.dao.MediaConfigRepository;
import fr.gdvd.media_manager.dao.MediaVideoRepository;
import fr.gdvd.media_manager.entities.MediaConfig;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service
public class MediaConfigServiceImpl implements MediaConfigService {

    @Autowired
    private MediaConfigRepository mediaConfigRepository;
    @Autowired
    private MediaVideoRepository mediaVideoRepository;
    @Autowired
    private  FindInPath findInPath;

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
    public List<Map<String, List<String>>> getAllPathsToMap() {
        List<MediaConfig> listMediaConfig = mediaConfigRepository.findAll();
        List<Map<String, List<String>>> result = new ArrayList<>();

        for (MediaConfig resid : listMediaConfig) {
            if (resid.getId().equals("IDs_by_path")) {
                for (Map<String, List<String>> lmc : resid.getPath()) {
                    for (Map.Entry<String, List<String>> lstr: lmc.entrySet()){
                        Map<String, List<String>> ls = new HashMap<>();
                        ls.put(lstr.getKey(), lstr.getValue());
                        result.add(ls);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public List<Document> getAllPaths() {
        List<MediaConfig> listMediaConfig = mediaConfigRepository.findAll();
        List<Document> listDocuments = new ArrayList<>();

        for (MediaConfig res : listMediaConfig) {
            if (res.getId().equals("IDs_by_path")) {
                List<Map<String, List<String>>> map = res.getPath();
                for (Map<String, List<String>> lmc : map) {
                    for (Map.Entry<String, List<String>> lstr: lmc.entrySet()){
                        Document doc = new Document(lstr.getKey(),lstr.getValue());
                        listDocuments.add(doc);
                    }
                }
            }
        }
        return listDocuments;
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

    @Override
    public Document updateDocWithOnePath(String path, List<String> list) {
        Document doc = new Document();
        doc.append("_id", "IDs_by_path");
        List<MediaConfig> listMediaConfig = mediaConfigRepository.findAll();
        for (MediaConfig res : listMediaConfig) {
            if (res.getId().equals("IDs_by_path")) {
                List<Map<String, List<String>>> mres = res.getPath();
                //search if path exist -> make nothing and return
                for(Map<String, List<String>> mp: mres){
                    if(mp.containsKey(path)){
                        doc.append("path",res.getPath());
                        return doc;
                    }
                }
                Map<String, List<String>> mtoadd = new HashMap<String, List<String>>();
                mtoadd.put(path, list);
                mres.add(mtoadd);
                res.setPath(mres);
                // the doc "IDs_by_path" exist -> update
                mediaConfigRepository.save(res);
                doc.append("path",res.getPath());
                return doc;
            }
        }
        MediaConfig res = new MediaConfig();
        res.setId("IDs_by_path");
        Map<String, List<String>> objToSave = new HashMap<>();
        objToSave.put(path, list);
        List<Map<String, List<String>>> mres = new ArrayList<>();
        mres.add(objToSave);
        res.setPath(mres);
        // the doc "IDs_by_path" dosn't exist -> create a new doc wiht : "path"&"list"
        mediaConfigRepository.save(res);
        doc.append("path",res.getPath());
        return doc;
    }


}
