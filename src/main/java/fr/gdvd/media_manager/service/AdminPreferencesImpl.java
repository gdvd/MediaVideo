package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.daoMysql.PreferencesRepository;
import fr.gdvd.media_manager.entitiesMysql.Preferences;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminPreferencesImpl implements AdminPreferences {

    @Autowired
    private PreferencesRepository preferencesRepository;

    @Override
    public Preferences getpref() {
        return preferencesRepository.findByIdPreferences("01");
    }

    @Override
    public Preferences getpreftitle() {
        return preferencesRepository.findByIdPreferences("c2title");
    }
}
