package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.daoMysql.*;
import fr.gdvd.media_manager.entitiesMysql.*;
import fr.gdvd.media_manager.entitiesNoDb.ScoreBackup;
import fr.gdvd.media_manager.tools.Parser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Log4j2
@Service
public class AdminPreferencesImpl implements AdminPreferences {

    @Autowired
    private PreferencesRepository preferencesRepository;
    @Autowired
    private Parser parser;
    @Autowired
    private RequestWeb requestWeb;
    @Autowired
    private CommentScoreUserRepository commentScoreUserRepository;
    @Autowired
    private VideoFilmRepository videoFilmRepository;
    @Autowired
    private VideoUserScoreRepository videoUserScoreRepository;
    @Autowired
    private MyUserRepository myUserRepository;

    @Override
    public Preferences getpref() {
        return preferencesRepository.findByIdPreferences("01");
    }

    @Override
    public Preferences getpreftitle() {
        return preferencesRepository.findByIdPreferences("c2title");
    }

    @Override
    public Preferences getprefbackupMo() {
        return preferencesRepository.findByIdPreferences("backupMo");
    }

    @Override
    public Preferences getprefbackupSc() {
        return preferencesRepository.findByIdPreferences("backupSc");
    }

    @Override
    public Preferences postnewfrequencyscore(int frequency) {
        if (frequency < 1) frequency = 1;
        Preferences p = preferencesRepository.findByIdPreferences("backupSc");
        Map<String, String> mapPref = p.getPrefmap();
        mapPref.replace("frequency", frequency + "");
        p.setPrefmap(mapPref);
        return preferencesRepository.save(p);
    }

    @Override
    public List<String> getlisttoexport() {
        Preferences p = preferencesRepository.findByIdPreferences("backupSc");
        String dir = p.getPrefmap().get("directory");
        String extToScan = "xml";
        return parser.listDirectoryLight(dir, extToScan);
    }

    @Override
    public int executeexport(String nameExport) {
        int res = 0;
        List<String> lBackupScore = getlisttoexport();
        if (lBackupScore.contains(nameExport)) {
            // get the path of backupSc
            Preferences p = preferencesRepository.findByIdPreferences("backupSc");
            String dir = p.getPrefmap().get("directory");
            // get the file xml
            File f = new File(System.getProperty("user.home") + dir + "/" + nameExport);
            // export the file in string
            String toparse = requestWeb.fileToString(f);
            // parse the file xml to object
            List<String> lsb = parser.findAllTagsInString("<ScoreBackup>", "</ScoreBackup>", toparse, false);
            if (lsb.size() != 0) {

                String login = (nameExport.split("-"))[1];
                if (login.length() > 1) {
                    MyUser mu = myUserRepository.findByLogin(login);
                    if (mu != null) {
                        for (String sb : lsb) {
                            String idtt = parser.findTagInString("<idtt>", "</idtt>", sb, false);
                            String comment = parser.findTagInString("<comment>", "</comment>", sb, false);
                            String scoreStr = parser.findTagInString("<score>", "</score>", sb, false);
                            int score = 0;
                            if (Pattern.matches("[\\d]{1,2}", scoreStr)) {
                                score = Integer.parseInt(scoreStr);
                            } else {
                                continue;
                            }
                            String dateModif = parser.findTagInString("<dateModif>", "</dateModif>", sb, false);
                            ScoreBackup sbo = new ScoreBackup(idtt, comment, score, dateModif);
                            // insert into db
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd+HH-mm-ss");
                            Date date = new Date();
                            if (Pattern.matches("[\\d]{4}-[\\d]{2}-[\\d]{2}\\+[\\d]{2}-[\\d]{2}-[\\d]{2}", sbo.getDateModif())) {
                                try {
                                    date = formatter.parse(sbo.getDateModif());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                            VideoFilm vf = videoFilmRepository.findById(idtt).orElse(null);
                            if (vf == null) continue;
                            VideoUserScore v = videoUserScoreRepository.findByVideoFilmAndMyUser(vf, mu);
                            if(v==null){
                                VideoUserScore vus = new VideoUserScore(mu, vf);
                                vus.setNoteOnHundred(score);
                                vus.setDateModifScoreUser(date);

                                if (!comment.equals("")) {
                                    CommentScoreUser csu = new CommentScoreUser(null, comment, null);
                                    csu = commentScoreUserRepository.save(csu);
                                    vus.setCommentScoreUser(csu);
                                }

                                System.out.println(vf.getIdVideo()+" -> OK");
                                videoUserScoreRepository.save(vus);
                            }else{
                                System.out.println(vf.getIdVideo()+" -> NOP");
                                continue;
                            }
                            res++;
                        }
                    }
                }
            } else {
                log.info("List is empty");
            }
        }

        return res;
    }

    @Override
    public Preferences getValuesScheduledTask() {
        return preferencesRepository.findByIdPreferences("backupBa");
    }

    @Override
    public Preferences postnewfrequency(int frequency) {
        if (frequency < 1) frequency = 1;
        Preferences p = preferencesRepository.findByIdPreferences("backupMo");
        Map<String, String> mapPref = p.getPrefmap();
        mapPref.replace("frequency", frequency + "");
        p.setPrefmap(mapPref);
        return preferencesRepository.save(p);
    }


}
