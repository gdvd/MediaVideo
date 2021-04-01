package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.daoMysql.*;
import fr.gdvd.media_manager.entitiesMysql.*;
import fr.gdvd.media_manager.entitiesNoDb.*;
import fr.gdvd.media_manager.tools.Parser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Autowired
    private BasketRepository basketRepository;

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

                            /*VideoFilm vf = videoFilmRepository.findById(idtt).orElse(null);*/
                            VideoFilm vf = requestWeb.getOneVideoFilm(idtt, "");
                            if (vf == null) continue;
                            VideoUserScore v = videoUserScoreRepository.findByVideoFilmAndMyUser(vf, mu);
                            if (v == null) {
                                VideoUserScore vus = new VideoUserScore(mu, vf);
                                vus.setNoteOnHundred(score);
                                vus.setDateModifScoreUser(date);

                                if (!comment.equals("")) {
                                    CommentScoreUser csu = new CommentScoreUser(null, comment, null);
                                    csu = commentScoreUserRepository.save(csu);
                                    vus.setCommentScoreUser(csu);
                                }

                                System.out.println(vf.getIdVideo() + " -> OK");
                                videoUserScoreRepository.save(vus);
                            } else {
                                System.out.println(vf.getIdVideo() + " -> NOP");
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

    @Override
    public List<BasketNameUser> getBasketsOfUserWithInfo(
            String user, String login) {
        MyUser mu = myUserRepository.findByLogin(login);
        if (!mu.getRoles().stream()
                .anyMatch(r -> r.getRole().equals("ADMIN")))
            throw new RuntimeException("Not allowed to user (E412896)");
        mu = myUserRepository.findByLogin(user);
        if (mu == null) throw new RuntimeException("This user doesn't exist (E417406)");
        List<Tuple> lt = basketRepository.getBasketsAndMmiAndVne(user);
        List<EleBasket> leb = new ArrayList<>();
        if (lt.size() > 0) {
            for (Tuple t : lt) {
                leb.add(new EleBasket(
                        (String) t.toArray()[0],
                        (String) t.toArray()[1],
                        (Double) t.toArray()[2],
                        (String) t.toArray()[3],
                        (String) t.toArray()[4],
                        (String) t.toArray()[5]
                ));
            }
        }

        //Init Obj
        return reorderBaskets(leb);
    }

    private List<BasketNameUser> reorderBaskets(List<EleBasket> leb) {
        List<BasketNameUser> lbnu = new ArrayList<>();
        if (leb.size() != 0) {
            for (EleBasket e : leb) {
                if (lbnu.size() != 0) {
                    boolean t1 = false;
                    for (BasketNameUser bnu : lbnu) {
                        if (bnu.getBasketName().equals(e.getBasketName())) {
                            t1 = true; // This basket exist -> search VideoNameExport exist ?
                            boolean t2 = false;
                            for (ImportUser iu : bnu.getLimportuser()) {
                                if (iu.getNameVne().equals(e.getNameExport())) {
                                    t2 = true;
                                    //Add ele to List<MmiUser>
                                    addEle(iu, e);
                                }
                            }
                            if (!t2) {
                                // Add new Vne-Mmiuser to basket
                                addEleVne(bnu, e);
                            }
                        }
                    }
                    if (!t1) {
                        // Add new basket-Vne-Mmiuser to List<BasketNameUser>
                        addEleVneBasket(lbnu, e);
                    }
                } else {
                    // Create first element : basket-Vne-Mmiuser in List<BasketNameUser>
                    addEleVneBasket(lbnu, e);
                }
            }
        }
        updateSizeForEachBasket(lbnu);
        return lbnu;
    }

    private void updateSizeForEachBasket(List<BasketNameUser> lbnu) {
        if (lbnu.size() != 0) {
            for (BasketNameUser bnu : lbnu) {
                List<String> lid = new ArrayList<>();
                if (bnu.getLimportuser().size() != 0) {
                    for (ImportUser iu : bnu.getLimportuser()) {
                        if (iu.getLmmiuser().size() != 0) {
                            for (MmiUser mu : iu.getLmmiuser()) {
                                if (lid.size() != 0) {
                                    if(lid.stream().noneMatch(id->id.equals(mu.getIdmmi()))){
                                        //this id doesn't exist
                                        lid.add(mu.getIdmmi());
                                        mu.setActive(true);
                                        iu.setActive(true);
                                        iu.setSize(mu.getSize()+iu.getSize());
                                        bnu.setSize(mu.getSize()+bnu.getSize());
                                    }
                                } else {
                                    //New idMmi -> set to active and update basketSize
                                    lid.add(mu.getIdmmi());
                                    mu.setActive(true);
                                    iu.setActive(true);
                                    iu.setSize(mu.getSize());
                                    bnu.setSize(mu.getSize());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void addEle(ImportUser iu, EleBasket e) {
        MmiUser mu = new MmiUser(e.getPathGeneral(), e.getTitle(), e.getIdMmi(),
                e.getFileSize(), false);
        iu.getLmmiuser().add(mu);
    }

    private void addEleVne(BasketNameUser bnu, EleBasket e) {
        MmiUser mu = new MmiUser(e.getPathGeneral(), e.getTitle(), e.getIdMmi(),
                e.getFileSize(), false);
        ImportUser iu = new ImportUser(e.getNameExport(), 0D,
                false, Stream.of(mu).collect(Collectors.toList()));
        bnu.getLimportuser().add(iu);
    }

    private void addEleVneBasket(List<BasketNameUser> lbnu, EleBasket e) {
        MmiUser mu = new MmiUser(e.getPathGeneral(), e.getTitle(), e.getIdMmi(),
                e.getFileSize(), false);
        ImportUser iu = new ImportUser(e.getNameExport(), 0D,
                false, Stream.of(mu).collect(Collectors.toList()));
        BasketNameUser bu = new BasketNameUser(e.getBasketName(), 0D,
                false, Stream.of(iu).collect(Collectors.toList()));
        lbnu.add(bu);
    }

}
