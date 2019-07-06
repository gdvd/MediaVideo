package fr.gdvd.media_manager.service;


import java.util.List;
import java.util.Map;

public interface FindInPath {

    public Map<String, Integer> findNbIdInAllPaths(List<Map<String, List<String>>> allPaths, String id);


}
