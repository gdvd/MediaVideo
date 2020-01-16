package fr.gdvd.media_manager.tools;

import fr.gdvd.media_manager.daoMysql.MyMediaInfoRepository;
import fr.gdvd.media_manager.daoMysql.MyUserRepository;
import fr.gdvd.media_manager.daoMysql.PreferencesRepository;
import fr.gdvd.media_manager.daoMysql.VideoUserScoreRepository;
import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesMysql.MyUser;
import fr.gdvd.media_manager.entitiesMysql.Preferences;
import fr.gdvd.media_manager.entitiesMysql.VideoUserScore;
import fr.gdvd.media_manager.entitiesNoDb.BasicBackup;
import fr.gdvd.media_manager.entitiesNoDb.ScoreBackup;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.security.RolesAllowed;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RolesAllowed({"ROLE_ADMIN"})
@Log4j2
@Component
@EnableAsync
public class ScheduledTasks {

    @Autowired
    private PreferencesRepository preferencesRepository;
    @Autowired
    private MyMediaInfoRepository myMediaInfoRepository;
    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private VideoUserScoreRepository videoUserScoreRepository;

    @Async
    @Scheduled(fixedDelayString = "${fixed-delay4ScheduledTasks.in.milliseconds}")
    public void myTasks() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Preferences> lp = new ArrayList<>();
        Preferences p1 = preferencesRepository.findByIdPreferences("backupMo");
        lp.add(p1);
        Preferences p2 = preferencesRepository.findByIdPreferences("backupSc");
        lp.add(p2);

        for (Preferences p : lp) {
            Map<String, String> mapPref = p.getPrefmap();
            String frequencyInString = mapPref.get("frequency");
            String lastInString = mapPref.get("last");
            String directory = mapPref.get("directory");
            int frequency = 24;
            try {
                frequency = Integer.parseInt(frequencyInString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SimpleDateFormat dateHeureFormat = new SimpleDateFormat("yyyy-MM-dd>HH:mm:ss");
            Date dateInDb = null;
            try {
                dateInDb = dateHeureFormat.parse(lastInString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (dateInDb == null) throw new RuntimeException("Date is null");
            Date dateNow = new Date();
            Long tdb = dateInDb.getTime();
            Long tnow = dateNow.getTime();

            if (tdb < tnow) {
                // Do the task
                String dateNowInString = dateHeureFormat.format(dateNow);
                System.out.println(dateNowInString + " =====> Do the task : " + p.getIdPreferences());
                if (p.getIdPreferences().equals("backupMo")) {
                    dothetaskBasicBackup(directory);
                } else {
                    if (p.getIdPreferences().equals("backupSc")) {
                        dothetaskScoreBackup(directory);
                    }
                }

                // Next time to validate the scheduledtask = Now + (1 hour * frequency)
                Date d = new Date(tnow + (1000 * 3600 * frequency));
                String next = dateHeureFormat.format(d);
                mapPref.replace("last", next);
                p.setPrefmap(mapPref);
                preferencesRepository.save(p);

            }
        }
    }

    private void dothetaskBasicBackup(String directory) {
        List<String> lIdMmi = myMediaInfoRepository.getAllIdMmi();

        Date dateNow = new Date();
        SimpleDateFormat dateHeureFormat = new SimpleDateFormat("yyyy-MM-dd+HH-mm-ss");
        String nameDate = dateHeureFormat.format(dateNow);

        File f = new File(System.getProperty("user.home") + directory + "/SaveBasicBackup-" + nameDate + ".xml");

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(f.getAbsolutePath()));
            for (String id : lIdMmi) {

                MyMediaInfo mmi = myMediaInfoRepository.findById(id).orElse(null);
                if (mmi == null) continue;
                BasicBackup bb = new BasicBackup(mmi.getIdMyMediaInfo(), mmi.getFileSize(), mmi.getBitrate(),
                        mmi.getDuration(), mmi.getCodecId());
                if (mmi.getTypeMmi() != null) {
                    bb.setEpisode(mmi.getTypeMmi().getEpisode());
                    bb.setSaison(mmi.getTypeMmi().getSeason());
                    bb.setNameSerieVo(mmi.getTypeMmi().getNameSerieVO());
                    bb.setTypeName(mmi.getTypeMmi().getTypeName().getTypeName());
                    if (mmi.getTypeMmi().getVideoFilm() != null) {
                        bb.setIdTt(mmi.getTypeMmi().getVideoFilm().getIdVideo());
                    } else {
                        bb.setIdTt("");
                    }
                } else {
                    bb.setEpisode(0);
                    bb.setSaison(0);
                    bb.setNameSerieVo("");
                    bb.setTypeName("");
                    bb.setIdTt("");
                }
                writer.write(bb.toString());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dothetaskScoreBackup(String directory) {
        List<MyUser> lmu = myUserRepository.findAll();

        Date dateNow = new Date();
        SimpleDateFormat dateHeureFormat = new SimpleDateFormat("yyyy-MM-dd+HH-mm-ss");
        String nameDate = dateHeureFormat.format(dateNow);

        for (MyUser mu : lmu) {
            List<VideoUserScore> lvus = videoUserScoreRepository.findAllByMyUser(mu);
            String login = mu.getLogin();
            if (lvus.size() != 0) {
                File f = new File(System.getProperty("user.home") +
                        directory + "/SaveScoreBackup-" + login + "-" + nameDate + ".xml");
                BufferedWriter writer = null;
                try {
                    writer = new BufferedWriter(new FileWriter(f.getAbsolutePath()));
                    for (VideoUserScore vus : lvus) {
                        String comment = "";
                        if (vus.getCommentScoreUser() != null) {
                            comment = vus.getCommentScoreUser().getComment();
                        }
                        String d = dateHeureFormat.format(vus.getDateModifScoreUser());
                        ScoreBackup sb = new ScoreBackup(vus.getId().getIdVideo(),
                                comment,
                                vus.getNoteOnHundred(),
                                d);
                        writer.write(sb.toString());
                    }
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
/*

class OneTask implements Runnable {

    @Autowired
    private PreferencesRepository preferencesRepository;
    @Autowired
    private MyMediaInfoRepository myMediaInfoRepository;
    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private VideoUserScoreRepository videoUserScoreRepository;


    @Override
    public void run() {

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Preferences> lp = new ArrayList<>();
        Preferences p1 = preferencesRepository.findByIdPreferences("backupMo");
        lp.add(p1);
        Preferences p2 = preferencesRepository.findByIdPreferences("backupSc");
        lp.add(p2);

        for (Preferences p : lp) {
            Map<String, String> mapPref = p.getPrefmap();
            String frequencyInString = mapPref.get("frequency");
            String lastInString = mapPref.get("last");
            String directory = mapPref.get("directory");
            int frequency = 24;
            try {
                frequency = Integer.parseInt(frequencyInString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SimpleDateFormat dateHeureFormat = new SimpleDateFormat("yyyy-MM-dd>HH:mm:ss");
            Date dateInDb = null;
            try {
                dateInDb = dateHeureFormat.parse(lastInString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (dateInDb == null) throw new RuntimeException("Date is null");
            Date dateNow = new Date();
            Long tdb = dateInDb.getTime();
            Long tnow = dateNow.getTime();

            if (tdb < tnow) {
                // Do the task
                String dateNowInString = dateHeureFormat.format(dateNow);
                System.out.println(dateNowInString + " =====> Do the task");
                if (p.getIdPreferences().equals("backupMo")){
                    dothetaskBasicBackup(directory);
                }else{
                    if(p.getIdPreferences().equals("backupSc")){
                        dothetaskScoreBackup(directory);
                    }
                }

                // Next time to validate the scheduledtask = Now + (1 hour * frequency)
                Date d = new Date(tnow + (1000 * 3600 * frequency));
                String next = dateHeureFormat.format(d);
                mapPref.replace("last", next);
                p.setPrefmap(mapPref);
                preferencesRepository.save(p);

            }
        }
    }
    private void dothetaskScoreBackup(String directory) {
        List<MyUser> lmu = myUserRepository.findAll();

        Date dateNow = new Date();
        SimpleDateFormat dateHeureFormat = new SimpleDateFormat("yyyy-MM-dd+HH-mm");
        String nameDate = dateHeureFormat.format(dateNow);

        for (MyUser mu : lmu) {
            List<VideoUserScore> lvus = videoUserScoreRepository.findAllByMyUser(mu);
            String login = mu.getLogin();
            if (lvus.size() != 0) {
                File f = new File(System.getProperty("user.home") +
                        directory + "/SaveScoreBackup - " + login + "-" + nameDate + ".xml");
                BufferedWriter writer = null;
                try {
                    writer = new BufferedWriter(new FileWriter(f.getAbsolutePath()));
                    for (VideoUserScore vus : lvus) {
                        String comment = "";
                        if (vus.getCommentScoreUser() != null) {
                            comment = vus.getCommentScoreUser().getComment();
                        }
                        String d = dateHeureFormat.format(vus.getDateModifScoreUser());
                        ScoreBackup sb = new ScoreBackup(vus.getId().getIdVideo(),
                                comment,
                                vus.getNoteOnHundred(),
                                d);
                        writer.write(sb.toString());
                    }
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void dothetaskBasicBackup(String directory) {
        List<String> lIdMmi = myMediaInfoRepository.getAllIdMmi();

        Date dateNow = new Date();
        SimpleDateFormat dateHeureFormat = new SimpleDateFormat("yyyy-MM-dd+HH-mm");
        String nameDate = dateHeureFormat.format(dateNow);

        File f = new File(System.getProperty("user.home") + directory + "/SaveBasicBackup - " + nameDate + ".xml");

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(f.getAbsolutePath()));
            for (String id : lIdMmi) {

                MyMediaInfo mmi = myMediaInfoRepository.findById(id).orElse(null);
                if(mmi==null)continue;
                BasicBackup bb = new BasicBackup(mmi.getIdMyMediaInfo(), mmi.getFileSize(), mmi.getBitrate(),
                        mmi.getDuration(), mmi.getCodecId());
                if(mmi.getTypeMmi()!= null){
                    bb.setEpisode(mmi.getTypeMmi().getEpisode());
                    bb.setSaison(mmi.getTypeMmi().getSeason());
                    bb.setNameSerieVo(mmi.getTypeMmi().getNameSerieVO());
                    bb.setTypeName(mmi.getTypeMmi().getTypeName().getTypeName());
                    if(mmi.getTypeMmi().getVideoFilm()!=null){
                        bb.setIdTt(mmi.getTypeMmi().getVideoFilm().getIdVideo());
                    }else{
                        bb.setIdTt("");
                    }
                }else{
                    bb.setEpisode(0);
                    bb.setSaison(0);
                    bb.setNameSerieVo("");
                    bb.setTypeName("");
                    bb.setIdTt("");
                }
                writer.write(bb.toString());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/
