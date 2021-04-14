package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.daoMysql.MyUserRepository;
import fr.gdvd.media_manager.daoMysql.VideoFilmRepository;
import fr.gdvd.media_manager.daoMysql.VideoTitleRepository;
import fr.gdvd.media_manager.daoMysql.VideoUserScoreRepository;
import fr.gdvd.media_manager.entitiesMysql.MyUser;
import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesMysql.VideoUserScore;
import fr.gdvd.media_manager.entitiesNoDb.Item;
import fr.gdvd.media_manager.entitiesNoDb.ScoreUserLight;
import fr.gdvd.media_manager.entitiesNoDb.UserScoreLight;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static fr.gdvd.media_manager.sec.SecurityParams.URL_SITE;

@Log4j2
@Service
public class ApivfServiceImpl implements ApivfService {

    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private VideoUserScoreRepository videoUserScoreRepository;
    @Autowired
    private VideoTitleRepository videoTitleRepository;
    @Autowired
    private VideoFilmRepository videoFilmRepository;

    public static final String headRss = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rss version=\"2.0\"><channel>" +
            "<title>Score login</title><description>videogd</description><lastBuildDate></lastBuildDate>" +
            "<link>http://gdvd.ddns.net:8086/apivf/getrss/[apikey]/[]</link>";
    public static final String footerRss = "</channel></rss>";

    @Override
    public String getrss(String apikey, int quatity, String loginRequest) {

        String err = "<info>Error</info>";
        String res = "";

        // search apikey in MyUser, if exist
        MyUser mu = myUserRepository.findByApiKey(apikey);
        List<Item> li = new ArrayList<>();
        if (mu != null) {
            log.info("Request RSS : " + mu.getLogin() + " quantity : " + quatity + " on : " + loginRequest);
            // search loginRequest
            if (loginRequest.length() > 0) {
                Map<String, List<VideoFilm>> mlsul = new HashMap<>();
                if (quatity <= 0) {
                    // verify if there is a subscribe
                    // search for the last notes, in seconds ('quantity')
                } else {
                    // search for the last 'quatity' notes
                    // request on videoUserScore
                    mlsul = lastScoreBylogin(quatity,
                            loginRequest.split("-"));
                }
                // Convert Map with data -> List<Item>
                li = converdataMapToItemList(mlsul);
            } else {
                res = err;
            }
        } else {
            res = err;
        }
        String result = li.toString();
        result = result.replaceAll("</item>,[\\s]*<item>", "</item><item>");
        return headRss + result.substring(1, result.length() - 1) + footerRss;
    }

    private List<Item> converdataMapToItemList(Map<String, List<VideoFilm>> mlsul) {
        List<Item> li = new ArrayList<>();
        for (Map.Entry<String, List<VideoFilm>> e : mlsul.entrySet()) {
            String login = e.getKey();
            String db = "DB";
            for (VideoFilm vf : e.getValue()) {
                Item i = new Item();

                if (login.equals(db)) {
                    i.setTitle(vf.getVideoTitles().get(0).getTitle() + " : " +
                            ((float) vf.getScoreOnHundred() / 10) + "/10 (" + db + ")");
                    i.setPubDate(vf.getDateModifFilm().toString());
                    i.setComment("");
                } else {
                    List<VideoUserScore> lvu = vf.getVideoUserScores().stream()
                            .filter(vus -> vus.getMyUser().getLogin().equals(login))
                            .collect(Collectors.toList());
                    VideoUserScore vu = lvu.get(0);
                    i.setTitle(vf.getVideoTitles().get(0).getTitle() + " : " +
                            ((float) vu.getNoteOnHundred() / 10) + "/10 (" + login + ")");
                    i.setPubDate(vu.getDateModifScoreUser().toString());
                    i.setComment(vu.getCommentScoreUser() != null ? vu.getCommentScoreUser().getComment() : "");
                }


                i.setDescription(vf.getVideoTitles().get(0).getTitle());
                i.setLink(URL_SITE + "/videoid?title=" +
                        Base64.getUrlEncoder().encodeToString(vf.getIdVideo().getBytes()) +
                        "&sortValue=2&sortOrder=0&sortUser=0&charsreadySel=1&keywordTitleIsSel=1");
                li.add(i);
            }
        }
        return li;
    }

    private Map<String, List<VideoFilm>> lastScoreBylogin(int quatity, String[] ids) {
        Map<String, List<VideoFilm>> mlsul = new HashMap();
        for (String usr : ids) {
            Long idUsr = Long.parseLong(usr);
            if(idUsr==0){
                Pageable pageable = PageRequest.of(0, quatity);
                List<VideoFilm> l = videoFilmRepository.findVFByLast(pageable);
                mlsul.put("DB", l);
            }else{
                String username = getLogin(idUsr).orElse(null);
                if (username != null) {
                    Pageable pageable = PageRequest.of(0, quatity);
                    List<VideoFilm> l = videoFilmRepository.findVFwithUserOrderByDateModifScoreUser(
                            idUsr, new Date(), pageable);
                    mlsul.put(username, l);
                }
            }

        }
        return mlsul;
    }

    private Optional<String> getLogin(Long idUsr) {
        return myUserRepository.findLoginByIdUser(idUsr);
    }

}
