package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.entitiesMysql.Preferences;
import fr.gdvd.media_manager.entitiesNoDb.BasketNameUser;
import fr.gdvd.media_manager.entitiesNoDb.EleBasket;

import java.util.List;

public interface AdminPreferences {

    Preferences getpref();
    Preferences getpreftitle();
    Preferences getValuesScheduledTask();
    Preferences postnewfrequency(int frequency);
    Preferences getprefbackupMo();
    Preferences getprefbackupSc();
    Preferences postnewfrequencyscore(int frequency);
    List<String> getlisttoexport();
    int executeexport(String nameExport);

    List<BasketNameUser> getBasketsOfUserWithInfo(String user, String login);
}
