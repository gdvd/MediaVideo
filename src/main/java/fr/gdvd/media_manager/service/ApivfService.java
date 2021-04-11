package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.entitiesNoDb.Item;

import java.util.List;

public interface ApivfService {

    String getrss(String apikey, int quatity, String loginRequest);

}
