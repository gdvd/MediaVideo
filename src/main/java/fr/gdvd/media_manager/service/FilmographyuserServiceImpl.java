package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.daoMysql.MyUserRepository;
import fr.gdvd.media_manager.daoMysql.PreferencesRepository;
import fr.gdvd.media_manager.daoMysql.VideoFilmRepository;
import fr.gdvd.media_manager.daoMysql.VideoUserScoreRepository;
import fr.gdvd.media_manager.entitiesMysql.*;
import fr.gdvd.media_manager.entitiesNoDb.Filmlight;
import fr.gdvd.media_manager.entitiesNoDb.OneActor;
import fr.gdvd.media_manager.entitiesNoDb.OneFilmpgraphy;
import fr.gdvd.media_manager.entitiesNoDb.VideoFilmLight;
import fr.gdvd.media_manager.tools.Download;
import fr.gdvd.media_manager.tools.DownloadListIdtt2;
import fr.gdvd.media_manager.tools.Parser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Log4j2
@Service
public class FilmographyuserServiceImpl implements FilmographyuserService {

    @Autowired
    private RequestWebImpl requestWeb;
    @Autowired
    private Download download;
    @Autowired
    private Parser parser;
    @Autowired
    private VideoFilmRepository videoFilmRepository;
    @Autowired
    private VideoUserScoreRepository videoUserScoreRepository;
    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private PreferencesRepository preferencesRepository;
    @Autowired
    private DownloadListIdtt2 downloadListIdtt2;
//    @Autowired
//    private TaskExecutor taskExecutor;
//    @Autowired
//    private ApplicationContext applicationContext;

    @Override
    public List<OneActor> searchname(String name, boolean exactName) {
//        log.info("Service search name : " + name + " , exactName : " + exactName);
        List<OneActor> loa = requestForName(name, exactName);
        return loa;
    }

    @Override
    public OneFilmpgraphy filmographywithidnm(String idnm, String login) {
        OneFilmpgraphy of = new OneFilmpgraphy();
        if (Pattern.matches("nm[\\d]{6,9}", idnm)) {
            of.setIdNm(idnm);
            String request = "https://www.imdb.com/name/" + idnm;
            URL url = null;
            String res = "";
            try {
                res = download.download2String(new URL(request));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (res.length() > 1000) {
                String name = parser.findTagInString(
                        "<span class=\"itemprop\">",
                        "</span>", res, false);
                of.setName(name);

                String tableresult = parser.findTagInString(
                        "<div id=\"filmography*>",
                        "</div>", res, false);
                if (tableresult.length() > 30) {
                    MyUser mu = myUserRepository.findByLogin(login);
                    setOneFilmographyForOneList(of, tableresult, "id=\"actor-", mu);
                    setOneFilmographyForOneList(of, tableresult, "id=\"actress-", mu);
                    setOneFilmographyForOneList(of, tableresult, "id=\"writer-", mu);
                    setOneFilmographyForOneList(of, tableresult, "id=\"script_department-", mu);
                    setOneFilmographyForOneList(of, tableresult, "id=\"director-", mu);
                    setOneFilmographyForOneList(of, tableresult, "id=\"soundtrack-", mu);
                    setOneFilmographyForOneList(of, tableresult, "id=\"music_department-", mu);
                    setOneFilmographyForOneList(of, tableresult, "id=\"producer-", mu);
                    setOneFilmographyForOneList(of, tableresult, "id=\"thanks-", mu);
                    setOneFilmographyForOneList(of, tableresult, "id=\"self-", mu);
                    // ? voir nm1042874 -> Miscellaneous Crew
                }
            }
        }
        return of;
    }

    @Override
    public List<VideoFilmLight> loadFilmo(List<String> lfilmo, String idNm) {
        List<VideoFilmLight> lvf = new ArrayList<>();
        if (lfilmo.size() != 0) {
            System.out.println("threadsleep");
            Preferences pref = preferencesRepository.findByIdPreferences("idtt2dl");
            if (pref != null) {
//                pref.setDateModifPref(new Date());
//                Map<String, String> map = pref.getPrefmap();
                for (int i = lfilmo.size() - 1; i >= 0; i--) {
//                    System.out.println("Download idTT n# : " + i + " and idTt : " + lfilmo.get(i));
                    VideoFilm vf = requestWeb.getOneVideoFilm(lfilmo.get(i), "");
                    if (vf == null) continue;
                    int actpos = 0;
                    for (VideoFilmArtist vfa : vf.getVideoFilmArtists()) {
                        if (vfa.getId().getIdVideoArtist().equals(idNm)) {
                            actpos = vfa.getNumberOrderActor();
                        }
                    }
                    VideoFilmLight vfl = new VideoFilmLight(vf.getIdVideo(), actpos, vf.getScoreOnHundred());
                    lvf.add(vfl);
                    try {
                        Thread.sleep((3) * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                throw new RuntimeException("Preference idtt2dl doesn't exist");
            }
        }
        return lvf;
    }

    private OneFilmpgraphy setOneFilmographyForOneList(OneFilmpgraphy of, String tableresult, String type, MyUser mu) {
        List<String> lst = parser.findAllTagsInString(type, "</div>", tableresult, true);
//        int onepos = 1;
        for (String s : lst) {
            String idTT = parser.findTagInString(type, "\"", s, false);
            boolean todo = false;

            Filmlight fl = new Filmlight();

            if (of.getFilmo().size() != 0) {
                for (Filmlight afl : of.getFilmo()) {
                    if (idTT.equals(afl.getIdTt())) {
                        fl = afl;
                        todo = false;
                        break;
                    } else {
                        todo = true;
                    }
                }
            } else {
                todo = true;
            }

            switch (type) {
                case "id=\"actor-":
                    fl.setActor(true);
                    break;
                case "id=\"actress-":
                    fl.setActor(true);
                    break;
                case "id=\"writer-":
                    fl.setWriter(true);
                    break;
                case "id=\"script_department-":
                    fl.setWriter(true);
                    break;
                case "id=\"director-":
                    fl.setDirector(true);
                    break;
                case "id=\"soundtrack-":
                    fl.setSoundtrack(true);
                    break;
                case "id=\"music_department-":
                    fl.setSoundtrack(true);
                    break;
                case "id=\"producer-":
                    fl.setProducer(true);
                    break;
                case "id=\"thanks-":
                    fl.setThanks(true);
                    break;
                case "id=\"self-":
                    fl.setSelf(true);
                    break;
            }

            if (todo) {
                fl.setIdTt(idTT);
//                fl.setPos(onepos++);
                String infoTitle = parser.findTagInString("</b>", "<br/>", s, false);
                infoTitle = parser.findTagInStringToDelete("<a href*>", infoTitle, false);
                infoTitle = parser.findTagInStringToDelete("</a>", infoTitle, false);
                // (TV Series)
                if (infoTitle.contains("TV Series") && fl.isSelf()) continue;
                fl.setInfoTitle(infoTitle);
                fl.setTitle(parser.findTagInString("<a href=\"/title/*>", "</a>", s, false));
                String oneYearStr = parser.findTagInString("year_column*>", "</span>", s, false);
                oneYearStr = oneYearStr.replace("&nbsp;", "");
                int oneYearInt = 0;
                if (Pattern.matches("[0-9]{4}", oneYearStr)) {
                    oneYearInt = Integer.parseInt(oneYearStr);
                } else {
                    if (Pattern.matches("[0-9]{4}-[0-9]{4}", oneYearStr)) {
                        oneYearInt = Integer.parseInt(oneYearStr.substring(0, 4));
                    } else {
                        if (Pattern.matches("[0-9]{4}/[\\w]{1,3}", oneYearStr)) {
                            oneYearInt = Integer.parseInt(oneYearStr.substring(0, 4));
                        }
                    }
                }
                fl.setYear(oneYearInt);
                boolean appeared = true;
                if (infoTitle.contains("pre-production")
                        || infoTitle.contains("post-production")
                        || infoTitle.contains("completed")
                        || infoTitle.contains("announced")
                        || infoTitle.contains("filming")) {
                    appeared = false;
                }
                fl.setAppeared(appeared);
                // Find idTT, if exist get score
                int score = 0;
                String idtt = fl.getIdTt();
                if (idtt.length() != 0) {
                    VideoFilm vf = videoFilmRepository.findById(idtt).orElse(null);
                    if (vf != null) {
                        fl.setLoaded(true);
                        if (type.equals("id=\"actor-") || type.equals("id=\"actress-")) {
                            for (VideoFilmArtist vfa : vf.getVideoFilmArtists()) {
                                if (vfa.getId().getIdVideoArtist().equals(of.getIdNm())) {
                                    fl.setActorPos(vfa.getNumberOrderActor());
                                }
                            }
                        }
                        VideoUserScore vus = videoUserScoreRepository.findByVideoFilmAndMyUser(vf, mu);
                        if (vus != null) {
                            fl.setVus(vus);
                        }
                        score = vf.getScoreOnHundred();
                        if (vf.getTypeMmis() != null && vf.getTypeMmis().size() > 0) {
                            for (TypeMmi tm : vf.getTypeMmis()) {
                                if (tm.getMyMediaInfos() != null && tm.getMyMediaInfos().size() > 0) {
                                    if (fl.getLmmi() == null) fl.setLmmi(new ArrayList<>());
                                    fl.getLmmi().addAll(tm.getMyMediaInfos());
                                }
                            }
                        }
                    }
                }
                fl.setScore(score);
                of.getFilmo().add(fl);
            }
        }
        int pos = 1;
        for (Filmlight fl : of.getFilmo()) {
            fl.setPos(pos++);
        }
        return of;
    }

    private List<OneActor> requestForName(String name, boolean exactName) {
        List<OneActor> loa = new ArrayList<>();
        name = name.replaceAll("[^\\w\\s\\é\\'\\è\\ç\\à\\ë\\ê\\û\\ù\\ô\\ï]+", " ");
        name = name.replace("^[\\s]*", "").trim();
        name = name.replaceAll("[\\s]+", " ");

        String completeUrl = "https://www.imdb.com/find?";
        if (exactName) {
            completeUrl = completeUrl + "q=" + requestWeb.encodeValue(name) + "&s=nm&exact=true&ref_=fn_nm_ex";
        } else {
            name = name.replaceAll("[\\s]+", "+");
            completeUrl = completeUrl + "ref_=nv_sr_fn&q=" + requestWeb.encodeValue(name) + "&s=nm";
        }

        URL url = null;
        String res = "";
        try {
            url = new URL(completeUrl);
            res = download.download2String(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (res.length() > 1000) {
            String tableresult = parser.findTagInString(
                    "<table class=\"findList\">",
                    "</table>", res, false);
            if (tableresult.length() > 30) {
                List<String> lres = parser.findAllTagsInString("<tr class=\"findResult*>",
                        "</tr>", tableresult, false);
                if (lres.size() != 0) {
                    for (String strOa : lres) {
                        OneActor oa = new OneActor();

                        String resulttext1 = parser.findTagInString("<td class=\"primary_photo*>",
                                "</td>", strOa, false);
                        oa.setUrlImg(parser.findTagInString("<img src=\"", "\"", resulttext1, false));
                        oa.setIdNm(parser.findTagInString("href=\"/name/", "/", resulttext1, false));

                        String resulttext2 = parser.findTagInString("<td class=\"result_text*>",
                                "</td>", strOa, false);
                        oa.setName(parser.findTagInString("<a href=\"/name*>", "</a>", resulttext2, false));
                        String resulttext3 = parser.findTagInString("<small>", "</small>", resulttext2, false);
                        resulttext3 = parser.findTagInStringToDelete("<a href=\"/title/*>", resulttext3, false);
                        resulttext3 = parser.findTagInStringToDelete("</a>", resulttext3, false);
                        oa.setInfo(resulttext3);
                        loa.add(oa);
                    }
                }
            }
        }
        return loa;
    }
}
