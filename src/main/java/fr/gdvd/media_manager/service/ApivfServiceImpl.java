package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.daoMysql.MyUserRepository;
import fr.gdvd.media_manager.daoMysql.VideoTitleRepository;
import fr.gdvd.media_manager.daoMysql.VideoUserScoreRepository;
import fr.gdvd.media_manager.entitiesMysql.MyUser;
import fr.gdvd.media_manager.entitiesMysql.VideoUserScore;
import fr.gdvd.media_manager.entitiesNoDb.EleBasket;
import fr.gdvd.media_manager.entitiesNoDb.ScoreUserLight;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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

    @Override
    public String getrss(String apikey, int quatity, String loginRequest) {

        String err = "<info>Error</info>";
        String res = "";

        // search apikey in MyUser, if exist
        MyUser mu = myUserRepository.findByApiKey(apikey);
        if (mu != null) {

            //TODO: tmp
            res = "GOOD";
            // search loginRequest
            if (loginRequest.length() > 0) {
//                String[] ids = loginRequest.split("-");
                if (quatity <= 0) {
                    // verify if there is a subscribe
                    // search for the last notes, in seconds ('quantity')
                } else {
                    // search for the last 'quatity' notes
                    // request on videoUserScore
                    List<ScoreUserLight> lsul = lastScoreBylogin(quatity,
                            loginRequest.split("-"));
                }
            } else {
                res = err;
            }
        } else {
            res = err;
        }

        return res;
    }

    private List<ScoreUserLight> lastScoreBylogin(int quatity, String[] ids) {
        for (String usr : ids) {
            Pageable findScore = PageRequest.of(0, quatity);
            Long idUsr = Long.parseLong(usr);

            List<Tuple> lt = videoUserScoreRepository.getLastScore(idUsr, findScore);
            List<ScoreUserLight> lsul = convertListTupleInListSUL(lt);

        }
        return null;
    }

    private void getFirstTitle(List<ScoreUserLight> lsul) {

    }

    private List<ScoreUserLight> convertListTupleInListSUL(List<Tuple> lt) {
        List<ScoreUserLight> lsul = new ArrayList<>();
        if (lt.size() > 0) {
            for (Tuple t : lt) {
                ScoreUserLight sul = new ScoreUserLight(
                        (String) t.toArray()[0],
                        "",
                        (int) t.toArray()[1],
                        (String) t.toArray()[2],
                        (Timestamp) t.toArray()[3]
                );
                sul.setTitle(videoTitleRepository.findTitlesByIdvideo(sul.getIdtt()).get(0));
                sul.setIdtt(URL_SITE + "/videoid?title=" +
                        Base64.getEncoder().encodeToString((sul.getIdtt()).getBytes()) +
                        "&sortValue=2&sortOrder=0&sortUser=0&charsreadySel=1&keywordTitleIsSel=1");
                lsul.add(sul);
            }
        }
        return lsul;
    }

}
