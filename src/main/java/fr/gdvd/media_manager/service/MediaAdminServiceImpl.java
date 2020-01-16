package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.daoMysql.*;
import fr.gdvd.media_manager.entitiesMysql.*;
import fr.gdvd.media_manager.entitiesNoDb.*;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

@Log4j2
@Transactional
@Service
public class MediaAdminServiceImpl implements MediaAdminService {

    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private MyRoleRepository myRoleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private VideoFilmRepository videoFilmRepository;
    @Autowired
    private CommentScoreUserRepository commentScoreUserRepository;
    @Autowired
    private VideoUserScoreRepository videoUserScoreRepository;
    @Autowired
    private MyMediaInfoRepository myMediaInfoRepository;
    @Autowired
    private PreferencesRepository preferencesRepository;
    @Autowired
    private PreferencesSubscribeRepository preferencesSubscribeRepository;
    @Autowired
    private VideoTitleRepository videoTitleRepository;
    @Autowired
    private DateSubscribeRepository dateSubscribeRepository;
    @Autowired
    private VideoArtistRepository videoArtistRepository;

    @Override
    public List<MyUser> getAllUser() {
        return myUserRepository.findAllByLoginNotNull();
    }

    @Override
    public List<MyUser> getAllUserActive() {
        return myUserRepository.findAllByActiveIsTrue();
    }

    @Override
    public MyUser getOne(String login) {
        return myUserRepository.findByLogin(login);
    }

    @Override
    public MyUser getOneById(String id) {
        return myUserRepository.findByLogin(id);
    }

    @Override
    public MyUser updateUser(MyUser MyUser) {
        return myUserRepository.save(MyUser);
    }

    @Override
    public MyUser updateUserById(MyUser MyUser) {
        return myUserRepository.save(MyUser);
    }

    @Override
    public MyUser saveNewUser(MyUser MyUser, String[] roles) {
        if (roles == null) throw new UsernameNotFoundException("invalid role");
        List<MyRole> lmr = new ArrayList<>();
        for (String role : roles) {
            if (role == "") continue;
            MyRole mediaRole = myRoleRepository.findByRole(role);
            if (mediaRole == null) {
                mediaRole = new MyRole(null, role);
                MyRole nmr = myRoleRepository.save(mediaRole);
                lmr.add(nmr);
            } else {
                lmr.add(mediaRole);
            }
        }
        if (lmr.size() == 0) {
            MyRole mediaRole = myRoleRepository.save(new MyRole(null, "USER"));
            lmr.add(mediaRole);
        }
//        MyUser.setRoles(lmr);
        return myUserRepository.save(MyUser);
    }

    @Override
    public String info() {
        log.info("mediaadminservice.info OK");
        return "Tres bien recu";
    }

    @Override
    public VideoFilm addScoreToUser(ReqScore reqScore, String login) {
        VideoFilm vf = videoFilmRepository.findById(reqScore.getIdtt()).orElse(null);
        if (vf == null) throw new RuntimeException("This idVideoFilm doen't exist");
        CommentScoreUser csu = null;
        if (reqScore.getComment().length() != 0) {
            csu = new CommentScoreUser(null, reqScore.getComment(), null);
            csu = commentScoreUserRepository.save(csu);
        }
        MyUser mu = myUserRepository.findByLogin(login);
        VideoUserScore vus = new VideoUserScore(mu, vf);
        vus.setCommentScoreUser(csu);
        vus.setDateModifScoreUser(new Date());
        vus.setNoteOnHundred(reqScore.getScore());
        videoUserScoreRepository.save(vus);
        return videoFilmRepository.findById(reqScore.getIdtt()).orElse(null);
    }

    @Override
    public List<String> listuserstosub(String login, boolean vf, boolean name) {
        Preferences pref = preferencesRepository.findByIdPreferences("subscrib");
        List<String> lret = new ArrayList<>();
        if (vf) lret.add("videofilm");
        if (name) lret.add("name");
        if (pref == null) throw new RuntimeException("Preferences subscribe is invalid");
        List<ItemToSearch> litem = pref.getItemToSearches();
        for (ItemToSearch item : litem) {
            if (item.getItemImdb().equals("admin")) {
                List lLogin = myUserRepository.findAllLoginActive();
                lret.addAll(lLogin);
            } else {
                if (item.getItemImdb().equals(login)) {
                    for (String it : item.getKeyset()) {
                        //TODO:
                    }
                }
            }
        }
        return lret;
    }

    @Override
    public PreferencesSubscribeWithScore subscribe(String login, PreferencesSubscribe psProvided) {
        List<String> lu = listuserstosub(login, false, false);
        MyUser mu = myUserRepository.findByLogin(login);

        if (psProvided.getIdToSub().equals("")) throw new RuntimeException("Invalid data idToSub");

        if (psProvided.getIdToSub().equals("videofilm")
                || lu.contains(psProvided.getIdToSub())
                || psProvided.getIdToSub().equals("name")) {

            PreferencesSubscribe ps = null;
            List<PreferencesSubscribe> lps = preferencesSubscribeRepository.findAllByMyUser_Login(login);

            boolean test = false;

            for (PreferencesSubscribe pslocal : lps) {

                if (psProvided.getIdToSub().equals("videofilm") && pslocal.getIdToSub().equals("videofilm")) {
                    ps = pslocal;
                    test = true;
                    break;
                } else {
                    if (psProvided.getIdToSub().equals("name") && pslocal.getIdToSub().equals("name") &&
                            psProvided.getName().equals(pslocal.getName())) {
                        ps = pslocal;
                        test = true;
                        break;
                    } else {
                        if (pslocal.getIdToSub().equals(psProvided.getIdToSub())) {
                            ps = pslocal;
                            test = true;
                            break;

                        }
                    }
                }

            }

            if (test) {
                List<DateSubscribe> lds = dateSubscribeRepository
                        .findAllByPreferencesSubscribe_Id(ps.getId());
                if (lds.size() != 0) {
                    Collections.reverse(lds);
                    dateSubscribeRepository.deleteById(lds.get(0).getId());
                }
            }

            if (ps == null) {
                ps = new PreferencesSubscribe();
            }

            Date d = new Date();
            DateSubscribe ds = new DateSubscribe(null, d, null);

            ps.setActive(true);
            ps.setValueMin(psProvided.getValueMin());
            ps.setValueMax(psProvided.getValueMax());
            ps.setNbOfresultMin(psProvided.getNbOfresultMin());
            ps.setNbOfresultMax(psProvided.getNbOfresultMax());
            setTheLimit(psProvided, ps);

            List<OneSimpleScore> loss = new ArrayList<>();
            ps.setMyUser(mu);

            if (psProvided.getIdToSub().equals("videofilm")) { // user ask videofilm
                ps.setName("");
                ps.setIdToSub("videofilm");
                ps = preferencesSubscribeRepository.save(ps);
                ds.setPreferencesSubscribe(ps);
                dateSubscribeRepository.save(ds);

                PreferencesSubscribeWithScore newpsws = new PreferencesSubscribeWithScore(ps
                        , 0, 0, null, null, loss);

                return getSubscribeTt(newpsws);

            } else {
                if (psProvided.getIdToSub().equals("name")) {
                    if (Pattern.matches("nm[\\d]{6,9}", psProvided.getName())) {
                        VideoArtist va = videoArtistRepository.findById(psProvided.getName()).orElse(null);
                        if (va == null) throw new RuntimeException("IdArtist not found");
                        PreferencesSubscribe pstmp =
                                preferencesSubscribeRepository
                                        .findByMyUser_LoginAndIdToSubAndIdname(mu.getLogin(),
                                                "name", psProvided.getName()).orElse(null);
                        if (pstmp != null) {
                            ps.setId(pstmp.getId());
                        } else {
                            ps.setId(null);
                        }
                        ps.setIdToSub("name");
                        ps.setName(va.getFirstLastName());
                        ps.setIdname(va.getIdVideoArtist());
                        ps = preferencesSubscribeRepository.save(ps);
                        ds.setPreferencesSubscribe(ps);
                        dateSubscribeRepository.save(ds);

                        PreferencesSubscribeWithScore newpsws = new PreferencesSubscribeWithScore(ps
                                , 0, 0, null, null, loss);

                        return getSubscribeName(newpsws);
                    }

                } else {
                    // Else : the name subscribe is in the list lu,
                    // and is not 'videofilm' and not 'name'
                    MyUser muToSub = myUserRepository.findByLogin(psProvided.getIdToSub());
                    ps.setName("");
                    ps.setIdToSub(psProvided.getIdToSub());
                    ps = preferencesSubscribeRepository.save(ps);
                    ds.setPreferencesSubscribe(ps);
                    dateSubscribeRepository.save(ds);
                    PreferencesSubscribeWithScore newpsws = new PreferencesSubscribeWithScore(ps
                            , 0, 0, null, null, loss);

                    return getSubscribeUser(newpsws, muToSub);
                }
            }
        } else {
            throw new RuntimeException("Not allowed");
        }
        return null;
    }

    private void setTheLimit(PreferencesSubscribe psProvided, PreferencesSubscribe ps) {
        if (!(psProvided.getValueMin() >= 0 &&
                psProvided.getValueMin() < 100)) {
            ps.setValueMin(0);
        } else {
            ps.setValueMin(psProvided.getValueMin());
        }
        if (!(psProvided.getValueMax() >= 0 &&
                psProvided.getValueMax() < 100)) {
            ps.setValueMax(99);
        } else {
            ps.setValueMax(psProvided.getValueMax());
        }
        if (!(psProvided.getNbOfresultMin() >= 1 &&
                psProvided.getNbOfresultMin() < 1000)) {
            ps.setNbOfresultMin(1);
        } else {
            ps.setNbOfresultMin(psProvided.getNbOfresultMin());
        }
        if (!(psProvided.getNbOfresultMax() > 1 &&
                psProvided.getNbOfresultMax() < 1000)) {
            ps.setNbOfresultMax(10);
        } else {
            ps.setNbOfresultMax(psProvided.getNbOfresultMax());
        }
    }

    private PreferencesSubscribeWithScore getSubscribeTt(PreferencesSubscribeWithScore newpsl) {
        if (newpsl.getDateask() < 0)
            throw new RuntimeException("GetdateSubscribe data is invalid");

        List<DateSubscribe> lds = dateSubscribeRepository
                .findAllByPreferencesSubscribe_Id(newpsl.getPreferencesSubscribe().getId());
        Collections.reverse(lds);
        List<VideoFilm> lvf = new ArrayList<>();

        if (lds.size() > newpsl.getDateask()) {
            if (newpsl.getDateask() == 0) {
                newpsl.setDatePrevious(lds.get(0).getDateModif());
                lvf = videoFilmRepository.findVideofilmDateAfter(
                        newpsl.getPreferencesSubscribe().getValueMin(),
                        newpsl.getPreferencesSubscribe().getValueMax(),
                        newpsl.getDatePrevious());
                convertVFtoOSS(lvf, newpsl);
            } else {
                newpsl.setDateModif(lds.get(newpsl.getDateask()).getDateModif());
                newpsl.setDatePrevious(lds.get(newpsl.getDateask() - 1).getDateModif());
                lvf = videoFilmRepository.findVideofilmDateBetween(
                        newpsl.getPreferencesSubscribe().getValueMin(),
                        newpsl.getPreferencesSubscribe().getValueMax(),
                        newpsl.getDateModif(), newpsl.getDatePrevious());
                convertVFtoOSS(lvf, newpsl);
            }
        } else {
            int deep = newpsl.getDateask() - lds.size();
            Pageable findScore = PageRequest.of(deep, newpsl.getPreferencesSubscribe().getNbOfresultMin());
            Page<VideoFilm> pvf = videoFilmRepository.findVideofilmSubscribeByResMin(findScore,
                    newpsl.getPreferencesSubscribe().getValueMin(),
                    lds.get(lds.size() - 1).getDateModif());

            if (pvf.getContent().size() != 0) {
                for (VideoFilm vf : pvf.getContent()) {
                    lvf.add(vf);
                }
                newpsl.setDateModif(lvf.get(0).getDateModifFilm());
                newpsl.setDatePrevious(lvf.get(lvf.size() - 1).getDateModifFilm());
                convertVFtoOSS(lvf, newpsl);
            } else {
                int da = newpsl.getDateask() - 1;
                if (da < 0) da = 0;
                newpsl.setDateask(da);
                if (pvf.getTotalPages() != 0) {
                    findScore = PageRequest.of(pvf.getTotalPages() - 1, newpsl.getPreferencesSubscribe().getNbOfresultMin());
                    pvf = videoFilmRepository.findVideofilmSubscribeByResMin(findScore,
                            newpsl.getPreferencesSubscribe().getValueMin(),
                            lds.get(lds.size() - 1).getDateModif());
                    for (VideoFilm vf : pvf.getContent()) {
                        lvf.add(vf);
                    }
                    newpsl.setDateModif(lvf.get(0).getDateModifFilm());
                    newpsl.setDatePrevious(lvf.get(lvf.size() - 1).getDateModifFilm());
                    convertVFtoOSS(lvf, newpsl);
                }
            }
        }
        return newpsl;
    }

    private PreferencesSubscribeWithScore getSubscribeUser(PreferencesSubscribeWithScore newpsl, MyUser muToSub) {
        if (newpsl.getDateask() < 0)
            throw new RuntimeException("GetdateSubscribe data is invalid");

        List<DateSubscribe> lds = dateSubscribeRepository
                .findAllByPreferencesSubscribe_Id(newpsl.getPreferencesSubscribe().getId());
        Collections.reverse(lds);
        List<VideoFilm> lvf = new ArrayList<>();

        if (lds.size() > newpsl.getDateask()) {
            if (newpsl.getDateask() == 0) {
                newpsl.setDatePrevious(lds.get(0).getDateModif());
                lvf = videoFilmRepository.findVFwithUserResMinMaxAndDateAfter(
                        newpsl.getDatePrevious(),
                        newpsl.getPreferencesSubscribe().getValueMin(),
                        newpsl.getPreferencesSubscribe().getValueMax(),
                        muToSub.getIdMyUser());
                convertVFUsertoOSS(lvf, newpsl);
            } else {
                newpsl.setDateModif(lds.get(newpsl.getDateask()).getDateModif());
                newpsl.setDatePrevious(lds.get(newpsl.getDateask() - 1).getDateModif());
                lvf = videoFilmRepository.findVFwithUserResMinMaxAndDateBetween(
                        newpsl.getDateModif(), newpsl.getDatePrevious(),
                        newpsl.getPreferencesSubscribe().getValueMin(),
                        newpsl.getPreferencesSubscribe().getValueMax(),
                        muToSub.getIdMyUser());
                convertVFUsertoOSS(lvf, newpsl);
            }
        } else {
            int deep = newpsl.getDateask() - lds.size();
            Pageable findScore = PageRequest.of(deep,
                    newpsl.getPreferencesSubscribe().getNbOfresultMin());
            Page<VideoFilm> pvf = videoFilmRepository
                    .findVFwithUserResMinMaxByPage(findScore,
                            lds.get(lds.size() - 1).getDateModif(),
                            newpsl.getPreferencesSubscribe().getValueMin(),
                            newpsl.getPreferencesSubscribe().getValueMax(),
                            muToSub.getIdMyUser());

            if (pvf.getContent().size() != 0) {
                for (VideoFilm vf : pvf.getContent()) {
                    lvf.add(vf);
                }
                newpsl.setDateModif(lvf.get(0).getDateModifFilm());
                newpsl.setDatePrevious(lvf.get(lvf.size() - 1).getDateModifFilm());
                convertVFUsertoOSS(lvf, newpsl);
            } else {
                int da = newpsl.getDateask() - 1;
                if (da < 0) da = 0;
                newpsl.setDateask(da);
                if (pvf.getTotalPages() != 0) {
                    findScore = PageRequest.of(pvf.getTotalPages() - 1, newpsl.getPreferencesSubscribe().getNbOfresultMin());
                    pvf = videoFilmRepository
                            .findVFwithUserResMinMaxByPage(findScore,
                                    lds.get(lds.size() - 1).getDateModif(),
                                    newpsl.getPreferencesSubscribe().getValueMin(),
                                    newpsl.getPreferencesSubscribe().getValueMax(),
                                    muToSub.getIdMyUser());
                    for (VideoFilm vf : pvf.getContent()) {
                        lvf.add(vf);
                    }
                    newpsl.setDateModif(lvf.get(0).getDateModifFilm());
                    newpsl.setDatePrevious(lvf.get(lvf.size() - 1).getDateModifFilm());
                    convertVFUsertoOSS(lvf, newpsl);
                }
            }
        }
        return newpsl;
    }

    private PreferencesSubscribeWithScore getSubscribeName(PreferencesSubscribeWithScore newpsl) {
        if (newpsl.getDateask() < 0)
            throw new RuntimeException("Get date Subscribe data is invalid");

        List<DateSubscribe> lds = dateSubscribeRepository
                .findAllByPreferencesSubscribe_Id(newpsl.getPreferencesSubscribe().getId());
        Collections.reverse(lds);
        List<VideoFilm> lvf = new ArrayList<>();

        if (lds.size() > newpsl.getDateask()) {
            if (newpsl.getDateask() == 0) {
                newpsl.setDatePrevious(lds.get(0).getDateModif());
                lvf = videoFilmRepository.findVFwithNameResMinMaxAndDateAfter(
                        newpsl.getDatePrevious(),
                        newpsl.getPreferencesSubscribe().getValueMin(),
                        newpsl.getPreferencesSubscribe().getValueMax(),
                        newpsl.getPreferencesSubscribe().getIdname());
                convertVFNametoOSS(lvf, newpsl);
            } else {
                newpsl.setDateModif(lds.get(newpsl.getDateask()).getDateModif());
                newpsl.setDatePrevious(lds.get(newpsl.getDateask() - 1).getDateModif());
                lvf = videoFilmRepository.findVFwithNameResMinMaxAndDateBetween(
                        newpsl.getDateModif(), newpsl.getDatePrevious(),
                        newpsl.getPreferencesSubscribe().getValueMin(),
                        newpsl.getPreferencesSubscribe().getValueMax(),
                        newpsl.getPreferencesSubscribe().getIdname());
                convertVFNametoOSS(lvf, newpsl);
            }
        } else {
            int deep = newpsl.getDateask() - lds.size();
            Pageable findScore = PageRequest.of(deep,
                    newpsl.getPreferencesSubscribe().getNbOfresultMin());
            Page<VideoFilm> pvf = videoFilmRepository
                    .findVFwithNameResMinMaxByPage(findScore,
                            lds.get(lds.size() - 1).getDateModif(),
                            newpsl.getPreferencesSubscribe().getValueMin(),
                            newpsl.getPreferencesSubscribe().getValueMax(),
                            newpsl.getPreferencesSubscribe().getIdname());

            if (pvf.getContent().size() != 0) {
                for (VideoFilm vf : pvf.getContent()) {
                    lvf.add(vf);
                }
                newpsl.setDateModif(lvf.get(0).getDateModifFilm());
                newpsl.setDatePrevious(lvf.get(lvf.size() - 1).getDateModifFilm());
                convertVFNametoOSS(lvf, newpsl);
            } else {
                int da = newpsl.getDateask() - 1;
                if (da < 0) da = 0;
                newpsl.setDateask(da);
                if (pvf.getTotalPages() != 0) {
                    findScore = PageRequest.of(pvf.getTotalPages() - 1, newpsl.getPreferencesSubscribe().getNbOfresultMin());
                    pvf = videoFilmRepository
                            .findVFwithNameResMinMaxByPage(findScore,
                                    lds.get(lds.size() - 1).getDateModif(),
                                    newpsl.getPreferencesSubscribe().getValueMin(),
                                    newpsl.getPreferencesSubscribe().getValueMax(),
                                    newpsl.getPreferencesSubscribe().getIdname());
                    for (VideoFilm vf : pvf.getContent()) {
                        lvf.add(vf);
                    }
                    newpsl.setDateModif(lvf.get(0).getDateModifFilm());
                    newpsl.setDatePrevious(lvf.get(lvf.size() - 1).getDateModifFilm());
                    convertVFNametoOSS(lvf, newpsl);
                }

            }
        }
        return newpsl;
    }

    @Override
    public PreferencesSubscribeWithScore validationsubscribe(String login, Long id) {
        PreferencesSubscribe ps = preferencesSubscribeRepository
                .findByActiveAndMyUser_LoginAndId(true, login, id).orElse(null);
        if (ps == null) throw new RuntimeException("Validation subscribe is invalide");
        PreferencesSubscribeWithScore psws = new PreferencesSubscribeWithScore();
        psws.setPreferencesSubscribe(ps);

        Date d = new Date();
        DateSubscribe ds = new DateSubscribe(null, d, ps);
        dateSubscribeRepository.save(ds);

        List<DateSubscribe> lds = dateSubscribeRepository
                .findAllByPreferencesSubscribe_Id(ps.getId());
        if (lds.size() > 1) {
            Collections.reverse(lds);
            List<VideoFilm> lvf = videoFilmRepository.findVideofilmDateBetween(
                    ps.getValueMin(), ps.getValueMax(),
                    lds.get(1).getDateModif(), lds.get(0).getDateModif());
            if (lvf.size() == 0) {
                dateSubscribeRepository.deleteById(lds.get(1).getId());
            }
        }
        if (ps.getIdToSub().equals("videofilm")) {
            getSubscribeTt(psws);
        } else {
            if (ps.getIdToSub().equals("name")) {
                getSubscribeName(psws);
            } else {
                MyUser mu = myUserRepository.findByLogin(login);
                getSubscribeUser(psws, mu);
            }
        }

        return psws;
    }

    @Override
    public PreferencesSubscribeWithScore getOneSubscribe(String login,
                                                         int dateask, Long id) {
        PreferencesSubscribe ps = preferencesSubscribeRepository
                .findByActiveAndMyUser_LoginAndId(true, login, id)
                .orElse(null);
        if (ps == null) throw new RuntimeException("Data to get oneSubscribe is invalid");

        PreferencesSubscribeWithScore psws = new PreferencesSubscribeWithScore();
        psws.setPreferencesSubscribe(ps);
        psws.setDateask(dateask);

        if (ps.getIdToSub().equals("videofilm")) {
            getSubscribeTt(psws);
        } else {
            if (ps.getIdToSub().equals("name")) {
                getSubscribeName(psws);
            } else {
                MyUser mu2sub = myUserRepository.findByLogin(ps.getIdToSub());
                getSubscribeUser(psws, mu2sub);
            }
        }
        return psws;
    }


    private void convertVFtoOSS(@NotNull List<VideoFilm> lvf, PreferencesSubscribeWithScore newpsl) {
        List<OneSimpleScore> loss = new ArrayList<>();
        if (lvf.size() != 0) {
            for (VideoFilm vf : lvf) {
                OneSimpleScore oss = new OneSimpleScore();
                oss.setIdVideoFilm(vf.getIdVideo());
                oss.setTitle(vf.getVideoTitles().get(0).getTitle());
                oss.setDateModif(vf.getDateModifFilm());
                oss.setScore(vf.getScoreOnHundred());
                oss.setNbOfVote(vf.getNbOfVote());
                oss.setComment("");
                oss.setActorPos(0);
                loss.add(oss);
            }
        }
        newpsl.setLsimplescores(loss);
    }


    private void convertVFUsertoOSS(@NotNull List<VideoFilm> lvf, PreferencesSubscribeWithScore newpsl) {
        List<OneSimpleScore> loss = new ArrayList<>();
        if (lvf.size() != 0) {
            String login = newpsl.getPreferencesSubscribe().getIdToSub();
            for (VideoFilm vf : lvf) {
                OneSimpleScore oss = new OneSimpleScore();
                oss.setIdVideoFilm(vf.getIdVideo());
                oss.setTitle(vf.getVideoTitles().get(0).getTitle());
                oss.setDateModif(vf.getDateModifFilm());
                List<VideoUserScore> vuss = vf.getVideoUserScores();
                int res = 0;
                for (VideoUserScore vus : vuss) {
                    if (vus.getMyUser().getLogin().equals(login)) {
                        res = vus.getNoteOnHundred();
                    }
                }
                oss.setScore(res);
                oss.setNbOfVote(0);
                oss.setComment("");
                oss.setActorPos(0);
                loss.add(oss);
            }
        }
        newpsl.setLsimplescores(loss);
    }

    //TODO:
    private void convertVFNametoOSS(@NotNull List<VideoFilm> lvf, PreferencesSubscribeWithScore newpsl) {
        List<OneSimpleScore> loss = new ArrayList<>();
        if (lvf.size() != 0) {
            String idname = newpsl.getPreferencesSubscribe().getIdname();
            for (VideoFilm vf : lvf) {
                OneSimpleScore oss = new OneSimpleScore();
                oss.setIdVideoFilm(vf.getIdVideo());
                oss.setTitle(vf.getVideoTitles().get(0).getTitle());
                oss.setDateModif(vf.getDateModifFilm());
                oss.setNbOfVote(vf.getNbOfVote());
                oss.setComment("");
                oss.setScore(vf.getScoreOnHundred());
//                String role = "";
                int pos = 0;
                for (VideoFilmArtist vfa : vf.getVideoFilmArtists()) {
                    if (vfa.getVideoArtist().getIdVideoArtist().equals(idname)) {
                        int nb = vfa.getNumberOrderActor();
                        if (nb != 0) {
                            pos = nb;
                            break;
                        }
                    }
                }
                oss.setActorPos(pos);
                loss.add(oss);
            }
        }
        newpsl.setLsimplescores(loss);
    }

    @Override
    public PreferencesSubscribeWithScore getListOfSub(String login) {
        List<PreferencesSubscribe> lps = preferencesSubscribeRepository.findAllByMyUser_Login(login);
        for (PreferencesSubscribe ps : lps) {
            ps.setMyUser(null);
        }
        return null;
    }

    @Override
    public List<PreferencesSubscribeWithScore> getallsubscribes(String login) {
        List<PreferencesSubscribeWithScore> lpsws = new ArrayList<>();
        List<PreferencesSubscribe> lps = preferencesSubscribeRepository.findAllByMyUser_Login(login);
        if (lps.size() == 0) return lpsws;

        for (PreferencesSubscribe ps : lps) {
            PreferencesSubscribeWithScore psws = new PreferencesSubscribeWithScore();
            psws.setPreferencesSubscribe(ps);
            psws.setDateask(0);

            if (ps.getIdToSub().equals("videofilm")) {
                getSubscribeTt(psws);
            } else {
                if (ps.getIdToSub().equals("name")) {
                    getSubscribeName(psws);
                } else {
                    // listuserstosub
                    List<String> lu = listuserstosub(login, false, false);
                    if (lu.contains(ps.getIdToSub())) {
                        MyUser mu2sub = myUserRepository.findByLogin(ps.getIdToSub());
                        getSubscribeUser(psws, mu2sub);
                    }
                }
            }

            lpsws.add(psws);
        }

        return lpsws;
    }

/*    private List<OneSimpleScore> getOneSubscribeOnLogin(String login, PreferencesSubscribe ps) {
        MyUser mu = myUserRepository.findByLogin(login);
        return null;
    }

    private List<OneSimpleScore> getOneSubscribeOnName(PreferencesSubscribe ps) {
        return null;
    }*/

    @Override
    public MyUser udateUserToActive(String login) {
        MyUser MyUser = myUserRepository.findByLogin(login);
        if (MyUser == null) throw new UsernameNotFoundException("invalid login");
        MyUser.setActive(true);
        return myUserRepository.save(MyUser);
    }

    @Override
    public MyUser udateUserToInactive(String login) {
        MyUser MyUser = myUserRepository.findByLogin(login);
        if (MyUser == null) throw new UsernameNotFoundException("invalid login");
        MyUser.setActive(false);
        return myUserRepository.save(MyUser);
    }

    @Override
    public List<MyRole> findAllRoles() {
        return myRoleRepository.findAll();
    }

    @Override
    public MyRole saveRole(MyRole mediaRole) {
        return myRoleRepository.save(mediaRole);
    }

    @Override
    public MyRole findOneRole(String role) {
        return myRoleRepository.findByRole(role);
    }

    @Override
    public MyUser saveNewUser(Usr user) {
        if (!policyUser(user)) throw new UsernameNotFoundException("invalid user");
        MyUser myUser = new MyUser();
        myUser.setLogin(user.getLogin());
        myUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        myUser.setActive(true);
        myUser.setDateModif(new Date());
        user.getRoles().forEach(r -> {
            MyRole role = myRoleRepository.findByRole(r);
            if (role == null) myRoleRepository.save(new MyRole(null, r));
            myUser.getRoles().add(role);
        });
        myUserRepository.save(myUser);
        return myUser;
    }

    private List<MyRole> checkRoles(List<String> lstr) {
        List<MyRole> lmr = new ArrayList<>();
        if (lstr.size() == 0) {
            MyRole mrguset = myRoleRepository.findByRole("GUEST");
            if (mrguset == null) myRoleRepository.save(new MyRole(null, "GUEST"));
            lmr.add(mrguset);
        } else {
            for (String r : lstr) {
                MyRole mr = myRoleRepository.findByRole(r);
                //If this role doesn't exist
                if (mr == null) {
                    MyRole mrguset = myRoleRepository.findByRole("GUEST");
                    if (mrguset == null) myRoleRepository.save(new MyRole(null, "GUEST"));
                    lmr.add(mrguset);
                } else {
                    lmr.add(mr);
                }
            }
        }
        return lmr;
    }

    private boolean policyUser(Usr user) {
        if (user.getLogin().length() < 1
                || user.getPassword().length() < 1
                || user.getRoles().size() == 0) return false;

        return true;
    }

    private boolean policyUserNewPassword(Usrnewpassword user) {
        if (user.getLogin().length() < 1
                || user.getPasswordnew().length() < 1
                || user.getRoles().size() == 0) return false;

        return true;
    }

    @Override
    public MyUser changestatus(String login) {
        MyUser MyUser = myUserRepository.findByLogin(login);
        if (MyUser == null) throw new UsernameNotFoundException("invalid login");

        MyUser.setActive(!MyUser.isActive());
        MyUser.setDateModif(new Date());
        return myUserRepository.save(MyUser);
    }

    @Override
    public MyUser updateuserandpassword(Usrnewpassword user) {
        MyUser myUser = myUserRepository.findByLogin(user.getLogin());

        if (myUser == null) throw new UsernameNotFoundException("invalid id");
        if (!policyUserNewPassword(user)) throw new UsernameNotFoundException("invalid user");
        if (!bCryptPasswordEncoder.matches(user.getPasswordold(), myUser.getPassword()))
            throw new UsernameNotFoundException("invalid password");

//        MyUser.setRoles(checkRoles(user.getRoles()));
        myUser.setRoles(new ArrayList<>());

        List<String> rolesclear = new ArrayList<>(
                new HashSet<>(user.getRoles()));

        rolesclear.forEach(r -> {
            MyRole role = myRoleRepository.findByRole(r);
            if (role == null) myRoleRepository.save(new MyRole(null, r));
            myUser.getRoles().add(role);
        });

        myUser.setLogin(user.getLogin());
        myUser.setPassword(bCryptPasswordEncoder.encode(user.getPasswordnew()));
        myUser.setDateModif(new Date());
        myUserRepository.save(myUser);
        return myUser;
    }

    @Override
    public MyUser updateuser(Usr user) {
        MyUser myUser = myUserRepository.findByLogin(user.getLogin());
        if (myUser == null) throw new UsernameNotFoundException("invalid id");
        if (!bCryptPasswordEncoder.matches(user.getPassword(), myUser.getPassword()))
            throw new UsernameNotFoundException("invalid password");

//        MyUser.setRoles(checkRoles(user.getRoles()));
        myUser.setRoles(new ArrayList<>());
        List<String> rolesclear = new ArrayList<>(
                new HashSet<>(user.getRoles()));

        rolesclear.forEach(r -> {
            MyRole role = myRoleRepository.findByRole(r);
            if (role == null) myRoleRepository.save(new MyRole(null, r));
            myUser.getRoles().add(role);
        });

        myUser.setLogin(user.getLogin());
        myUser.setDateModif(new Date());
        return myUserRepository.save(myUser);
    }

    @Override
    public MyRole addnewrole(String role) {
        if (role == "") throw new UsernameNotFoundException("roleName null");
        MyRole myRole = myRoleRepository.findByRole(role);
        if (myRole == null) {
            myRole = new MyRole(null, role);
            myRole = myRoleRepository.save(myRole);
        }
        return myRole;
    }

}
