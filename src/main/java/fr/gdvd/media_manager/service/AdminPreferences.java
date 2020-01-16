package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.entitiesMysql.Preferences;

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
}
