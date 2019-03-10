package fr.gdvd.media_manager.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FindInPathImpl implements FindInPath {
    @Override
    public Map<String, Integer> findNbIdInAllPaths(List<Map<String, List<String>>> allPaths, String id) {

        Map<String, Integer> res = new HashMap<>();
        for(Map<String, List<String>> mp: allPaths){
            for(Map.Entry<String, List<String>> m : mp.entrySet()){
                String entry = m.getKey();
                List<String> values = m.getValue();
                res.put(entry, 0);
                for(String foundid: values){
                    if(foundid.equals(id)){
                        res.replace(entry, res.get(entry) + 1);
                    }
                }
            }
        }
        return res;
    }

}
