package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.daoMysql.*;
import fr.gdvd.media_manager.entitiesMysql.*;
import fr.gdvd.media_manager.entitiesNoDb.*;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Log4j2
@Service
public class RequestVideoServiceImpl implements RequestVideoService {

    @Autowired
    private VideoFilmRepository videoFilmRepository;
    @Autowired
    private VideoTitleRepository videoTitleRepository;
    @Autowired
    private TypeNameRepository typeNameRepository;
    @Autowired
    private TypeMmiRepository typeMmiRepository;
    @Autowired
    private VideoArtistRepository videoArtistRepository;
    @Autowired
    private VideoFilmArtistRepository videoFilmArtistRepository;
    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private VideoUserScoreRepository videoUserScoreRepository;
    @Autowired
    private MyMediaInfoRepository myMediaInfoRepository;
    @Autowired
    private VideoNameExportRepository videoNameExportRepository;
    @Autowired
    private VideoKindRepository videoKindRepository;
    @Autowired
    private MyMediaAudioRepository myMediaAudioRepository;
    @Autowired
    private MyMediaTextRepository myMediaTextRepository;
    @Autowired
    private MyMediaLanguageRepository myMediaLanguageRepository;
    @Autowired
    private VideoKeywordRepository videoKeywordRepository;
    @Autowired
    private VideoPosterRepository videoPosterRepository;
    @Autowired
    private VideoCommentRepository videoCommentRepository;
    @Autowired
    private VideoCountryRepository videoCountryRepository;

    @Override
    public Page<VideoFilm> getRequestVideo(RequestVideo req, String login) {

        //TODO if search ALL ...

        //Init
        Instant previous, current;
        long gap = 0;
        previous = Instant.now();
        MySorter sorter = getSorter(req);
        int states = getStates(req, sorter);
        /* Info: States -> switchCase
        reqTitleScoreYear                   2^0 ->  1
        reqTitleScoreYearEmpty              2^1 ->  2
        reqTypeEpisodeSeason                2^2 ->  4
        reqImportSupportwidthOnloginSize    2^3 ->  8
        reqNameandroles                     2^4 -> 16
        reqKeywordKindCountry               2^5 -> 32
        reqDurationWidthLanguage            2^6 -> 64
        */

        Page<VideoFilm> lvf = null;

//        log.info("==== request -> state : " + states);
        switch (states) {
            case (1):
                lvf = getVideoFilmsByDefault(req, login, states, sorter);
                break;
            default:
                lvf = getVideoFilmsByDefault(req, login, states, sorter);
        }

        current = Instant.now();
        if (previous != null) {
            gap = ChronoUnit.MILLIS.between(previous, current);
        }
        log.info("Request of : " + login + " Keyword : " + req.getTitle()
                + " | duration : " + ((float) gap / 1000) + "sec, with state : " + states);
        return lvf;
    }

    @NotNull
    Page<VideoFilm> getVideoFilmsByDefault(RequestVideo req, String login, int states, MySorter sorter) {
        List<VideoFilmLight2Search> lvfl = new ArrayList<>();
        List<String> lidtt = new ArrayList<>();
        verifySearchLevel(req);


        //SEARCH : all idVideo (Don't change the order)
        if ((states & 1) == 1) lidtt = searchOnTitleScoreYear(lidtt, req, sorter);
        if ((states & 4) == 4) lidtt = searchOnTypeEpisodeSeason(lidtt, req, states);
        if ((states & 8) == 8) lidtt = searchOnImportSupportwidthSize(lidtt, req, login, states);
        if ((states & 16) == 16) lidtt = searchOnNameandroles(lidtt, req, states);
        if ((states & 32) == 32) lidtt = searchOnKeywordKindCountry(lidtt, req, states);
        if ((states & 64) == 64) lidtt = searchOnDurationWidthLanguage(lidtt, req, states);

        //eraseDuplicate
        lidtt = eraseDuplicateInLStr(lidtt);
        Pageable p = getPageable(req);

        if (states == 2) {
            return findAllVFWithState2(p, req, sorter);
        } else {
            Page<VideoFilm> lvf = getPageWithListVideofilmlightV2(lidtt, p, req, sorter);
            return lvf;
        }
    }

    private Page<VideoFilm> findAllVFWithState2(Pageable p, RequestVideo req, MySorter sorter) {
        switch (sorter.getSortBy()) {
            case ("idVideo"):
                if (sorter.getAsc()) {
                    return getPageVideoFilmsSortByIddb(p, true);
                } else {
                    return getPageVideoFilmsSortByIddb(p, false);
                }
            case ("year"):
                if (sorter.getAsc()) {
                    return getPageVideoFilmsSortByYear(p, true);
                } else {
                    return getPageVideoFilmsSortByYear(p, false);
                }
            case ("scoreOnHundred"):
                MyUser mu = myUserRepository.findByIdMyUser(sorter.getIdPers());
                if (mu == null) sorter.setIdPers(0L);
                if (sorter.getIdPers() == 0) {
                    if (sorter.getAsc()) {
                        return getPageVideoFilmsSortByScore(p, true);
                    } else {
                        return getPageVideoFilmsSortByScore(p, false);
                    }
                } else {
                    if (sorter.getAsc()) {
                        return getPageVideoFilmsSortByScoreUser(p, sorter.getIdPers(), true);
                    } else {
                        return getPageVideoFilmsSortByScoreUser(p, sorter.getIdPers(), false);
                    }
                }
            default:
                return getPageVideoFilmsSortByScore(p, false);
        }
    }

    @NotNull
    private Page<VideoFilm> getPageVideoFilmsSortByScoreUser(Pageable p, Long idUser, boolean asc) {
        List<Tuple> lids = asc ?
                videoFilmRepository.findAllSortByUserScoreAsc(idUser) :
                videoFilmRepository.findAllSortByUserScoreDesc(idUser);
        List<String> lids1 = lids.stream()
                .map(i -> (String) i.toArray()[0])
                .collect(Collectors.toList());
        int sizeMax = lids.size();
        int min = getMinOfPage(p.getPageSize(), sizeMax, p.getPageNumber());
        int max = getMaxOfPage(p.getPageSize(), sizeMax, p.getPageNumber() + 1);
        List<String> lids2 = lids1.subList(min, max);
        List<VideoFilm> lvf3 = asc ?
                videoFilmRepository.findwithIdAndUserSortByScoreAsc(lids2, idUser) :
                videoFilmRepository.findwithIdAndUserSortByScoreDesc(lids2, idUser);
        orderListVfByscoreuser(lvf3, idUser, asc);
        Page<VideoFilm> pvf = new PageImpl<VideoFilm>(lvf3, p, lids1.size());
        return pvf;
    }

    private void orderListVfByscoreuser(List<VideoFilm> lvf,
                                        Long idUser, boolean asc) {
        for (int i = 0; i < lvf.size() - 1; i++) {
            VideoFilm a = lvf.get(i);
            int i1 = 0;
            for (int k = 0; k < a.getVideoUserScores().size(); k++) {
                if (a.getVideoUserScores().get(k).getMyUser().getIdMyUser() == idUser) i1 = k;
            }
            for (int j = i; j < lvf.size(); j++) {
                VideoFilm b = lvf.get(j);
                int j1 = 0;
                for (int l = 0; l < b.getVideoUserScores().size(); l++) {
                    if (b.getVideoUserScores().get(l).getMyUser().getIdMyUser() == idUser) j1 = l;
                }
                boolean invert = false;
                if (asc) {
                    if (a.getVideoUserScores().get(i1).getNoteOnHundred() >
                            b.getVideoUserScores().get(j1).getNoteOnHundred()) invert = true;
                } else {
                    if (a.getVideoUserScores().get(i1).getNoteOnHundred() <
                            b.getVideoUserScores().get(j1).getNoteOnHundred()) invert = true;
                }
                if (invert) {
                    lvf.set(i, b);
                    lvf.set(j, a);
                    a = lvf.get(i);
                }
            }
        }
    }

    @NotNull
    private Page<VideoFilm> getPageVideoFilmsSortByIddb(Pageable p, boolean asc) {
        List<Tuple> lids = asc ?
                videoFilmRepository.findAllSortByIddbAsc() :
                videoFilmRepository.findAllSortByIddbDesc();
        List<String> lids1 = lids.stream()
                .map(i -> (String) i.toArray()[0])
                .collect(Collectors.toList());
        int sizeMax = lids.size();
        int min = getMinOfPage(p.getPageSize(), sizeMax, p.getPageNumber());
        int max = getMaxOfPage(p.getPageSize(), sizeMax, p.getPageNumber() + 1);
        List<String> lids2 = lids1.subList(min, max);
        List<VideoFilm> lvf3 = asc ?
                videoFilmRepository.findwithIdAndSortByIddbAsc(lids2) :
                videoFilmRepository.findwithIdAndSortByIddbDesc(lids2);
        Page<VideoFilm> pvf = new PageImpl<VideoFilm>(lvf3, p, lids1.size());
        return pvf;
    }

    @NotNull
    private Page<VideoFilm> getPageVideoFilmsSortByYear(Pageable p, boolean asc) {
        List<Tuple> lids = asc ?
                videoFilmRepository.findAllSortByYearAsc() :
                videoFilmRepository.findAllSortByYearDesc();
        List<String> lids1 = lids.stream()
                .map(i -> (String) i.toArray()[0])
                .collect(Collectors.toList());
        int sizeMax = lids.size();
        int min = getMinOfPage(p.getPageSize(), sizeMax, p.getPageNumber());
        int max = getMaxOfPage(p.getPageSize(), sizeMax, p.getPageNumber() + 1);
        List<String> lids2 = lids1.subList(min, max);
        List<VideoFilm> lvf3 = asc ?
                videoFilmRepository.findwithIdAndSortByYearAsc(lids2) :
                videoFilmRepository.findwithIdAndSortByYearDesc(lids2);
        Page<VideoFilm> pvf = new PageImpl<VideoFilm>(lvf3, p, lids1.size());
        return pvf;
    }

    @NotNull
    private Page<VideoFilm> getPageVideoFilmsSortByScore(Pageable p, boolean asc) {
        List<Tuple> lids = asc ?
                videoFilmRepository.findAllSortByScoreAsc3() :
                videoFilmRepository.findAllSortByScoreDesc3();
        List<String> lids1 = lids.stream()
                .map(i -> (String) i.toArray()[0])
                .collect(Collectors.toList());
        int sizeMax = lids.size();
        int min = getMinOfPage(p.getPageSize(), sizeMax, p.getPageNumber());
        int max = getMaxOfPage(p.getPageSize(), sizeMax, p.getPageNumber() + 1);
        List<String> lids2 = lids1.subList(min, max);
        List<VideoFilm> lvf3 = asc ?
                videoFilmRepository.findwithIdAndSortByScoreAsc(lids2) :
                videoFilmRepository.findwithIdAndSortByScoreDesc(lids2);
        Page<VideoFilm> pvf = new PageImpl<VideoFilm>(lvf3, p, lids1.size());
        return pvf;
    }

    private int getMaxOfPage(int pageSize, int sizeMax, int i2) {
        int max = (i2) * pageSize;
        max = Math.min(max, sizeMax);
        return max;
    }

    private int getMinOfPage(int pageSize, int sizeMax, int pageNumber) {
        int min = pageNumber * pageSize;
        min = Math.min(min, sizeMax);
        return min;
    }

    private MySorter getSorter(RequestVideo req) {
        MySorter s;
        Long idPers = 0L;
        if (req.getSortBy().length() != 0) {
            String sortTbl[] = req.getSortBy().split("-");
            int sortValue = Integer.parseInt(sortTbl[0]);
            boolean sortOrder = Integer.parseInt(sortTbl[1]) == 1;
            String sortStr = "";
            switch (sortValue) {
                case (0):
                    sortStr = "idVideo";
                    break;
                case (1):
                    sortStr = "scoreOnHundred";
                    break;
                case (2):
                    sortStr = "year";
                    break;
                default:
                    sortStr = "scoreOnHundred";
            }

            if (sortTbl.length == 3) {
                idPers = Long.parseLong(sortTbl[2]);
            }
            s = new MySorter(sortStr, sortOrder, idPers);
        } else {
            s = new MySorter("scoreOnHundred", false, idPers);
        }
        return s;
    }

    /*********************************************************************
     ******************* getReqDurationWidthLanguage *********************
     *********************************************************************/

    private List<String> searchOnDurationWidthLanguage(
            List<String> lvfl, RequestVideo req, int states) {

        List<String> lvflNew = new ArrayList<>();
        List<String> lvflLanguage = new ArrayList<>();
        List<String> lvfl2sWidthDuration = new ArrayList<>();

        //Init
        int test = 0;
        if (req.getDurationMin() > 0 || req.getDurationMax() < 1000
                || req.getWidthMin() > 0 || req.getWidthMax() < 10000) test = 1;
        if (!req.getLanguagesStr().equals("")) test = test + 2;

        //Search
        if ((test & 1) == 1) lvfl2sWidthDuration = getAllWithDurationMinAndMax(req);
        if ((test & 2) == 2) lvflLanguage = getAllWithLanguages(req);

        //Reduce
        if ((test & 3) == 3) lvflNew = mergeTwoListIdVideoFilm(
                lvfl2sWidthDuration, lvflLanguage);
        if ((test & 3) == 1) lvflNew = lvfl2sWidthDuration;
        if ((test & 3) == 2) lvflNew = lvflLanguage;

        // Merge if reqTitle or reqType or reqImport or reqName or ReqKeyword is activated
        if ((states & 61) > 0) {
            return mergeTwoListIdVideoFilm(lvfl, lvflNew);
        } else {
            return lvflNew;
        }
    }

    private List<String> getAllWithLanguages(RequestVideo req) {
        String[] lstr = req.getLanguagesStr().split("-");
        List<Long> lIdLanguages = Arrays.stream(lstr).map(Long::parseLong).collect(Collectors.toList());
        List<String> lt = videoFilmRepository.findAllIdsWithLanguageV2(lIdLanguages);
        return lt;
    }

    private List<String> getAllWithDurationMinAndMax(RequestVideo req) {
        List<String> lt = videoFilmRepository.findAllIdsWithDurationAndWidthMmiV2(
                Double.valueOf(req.getDurationMin()) * 60,
                Double.valueOf(req.getDurationMax()) * 60,
                req.getWidthMin(), req.getWidthMax());
        return lt;
    }

    /*********************************************************************
     ******************* getReqKeywordKindCountry ********************
     *********************************************************************/

    private List<String> searchOnKeywordKindCountry(
            List<String> lvflPreviousSearch, RequestVideo req,
            int states) {
        List<String> lvflNew = new ArrayList<>();
        String kindStr = req.getKindStr();
        String kindNotStr = req.getKindNotStr();
        List<Long> lidKind = new ArrayList<>();
        List<Long> lidKindNot = new ArrayList<>();
        List<String> lvfl2sWithKind = new ArrayList<>();
        List<String> lvfl2sWithKeyword = new ArrayList<>();
        List<String> lvfl2sWithKeywords = new ArrayList<>();
        List<String> lvfl2sWithCountry = new ArrayList<>();

        //get list idsKind what works
        if (kindStr.length() != 0) {
            lidKind = getAllIdKindAndCompareWithList(kindStr);
            lidKindNot = getAllIdKindAndCompareWithList(kindNotStr);
            lvfl2sWithKind = findIdsVfWithIdKindV2(lidKind, lidKindNot);
        }
        if (req.getKeywordfilm().length() != 0) {
            lvfl2sWithKeyword = findIdsVfWithKeywordenV2(req);
        }
        if (req.getKeywordsStr().length() != 0) {
            lvfl2sWithKeywords = findIdsVfWithKeywordsV2(req);
        }
        if (req.getCountry().length() != 0) {
            lvfl2sWithCountry = findIdsVFWithCountryV2(req);
        }

        int test = 0;
        if (kindStr.length() != 0) test = 1;
        if (req.getKeywordfilm().length() != 0) test += 2;
        if (req.getKeywordsStr().length() != 0) test += 4;
        if (req.getCountry().length() != 0) test += 8;
        switch (test) {
            case (0):
                return lvflNew;
            case (1):
                lvflNew = lvfl2sWithKind;
                break;
            case (2):
                lvflNew = lvfl2sWithKeyword;
                break;
            case (3):
                lvflNew = merge2ListVfl2sV2(lvfl2sWithKind, lvfl2sWithKeyword);
                break;
            case (4):
                lvflNew = lvfl2sWithKeywords;
                break;
            case (5):
                lvflNew = merge2ListVfl2sV2(lvfl2sWithKind, lvfl2sWithKeywords);
                break;
            case (6):
                lvflNew = merge2ListVfl2sV2(lvfl2sWithKeyword, lvfl2sWithKeywords);
                break;
            case (7):
                lvflNew = merge2ListVfl2sV2(lvfl2sWithKind,
                        merge2ListVfl2sV2(lvfl2sWithKeyword, lvfl2sWithKeywords));
                break;
            case (8):
                lvflNew = lvfl2sWithCountry;
                break;
            case (9):
                lvflNew = merge2ListVfl2sV2(lvfl2sWithCountry, lvfl2sWithKind);
                break;
            case (10):
                lvflNew = merge2ListVfl2sV2(lvfl2sWithCountry, lvfl2sWithKeyword);
                break;
            case (11):
                lvflNew = merge2ListVfl2sV2(lvfl2sWithCountry,
                        merge2ListVfl2sV2(lvfl2sWithKind, lvfl2sWithKeyword));
                break;
            case (12):
                lvflNew = merge2ListVfl2sV2(lvfl2sWithCountry, lvfl2sWithKeywords);
                break;
            case (13):
                lvflNew = merge2ListVfl2sV2(lvfl2sWithCountry,
                        merge2ListVfl2sV2(lvfl2sWithKind, lvfl2sWithKeywords));
                break;
            case (14):
                lvflNew = merge2ListVfl2sV2(lvfl2sWithCountry,
                        merge2ListVfl2sV2(lvfl2sWithKeywords, lvfl2sWithKeyword));
                break;
            case (15):
                lvflNew = merge2ListVfl2sV2(lvfl2sWithCountry,
                        merge2ListVfl2sV2(lvfl2sWithKeywords,
                                merge2ListVfl2sV2(lvfl2sWithKind, lvfl2sWithKeyword)));
                break;
        }
        // Merge if reqTitle or reqType or reqImport or reqName is activeted
        if ((states & 29) > 0) {
            return mergeTwoListIdVideoFilm(lvflPreviousSearch, lvflNew);
        } else {
            return lvflNew;
        }
    }

    private List<String> findIdsVFWithCountryV2(RequestVideo req) {
        String requestCountry = getRequestTitleWithWildcardIfContainBeginEnd(
                req.getKeywordCountryIsSel(), req.getCountry());
        List<String> lt = videoFilmRepository.getIdsVFWithCountryV2(requestCountry);
        return lt;
    }

    private List<String> findIdsVfWithKeywordsV2(RequestVideo req) {
        String[] lstr = req.getKeywordsStr().split("-");
        List<Long> lIdKeywords = Arrays.stream(lstr).map(Long::parseLong).collect(Collectors.toList());
        List<String> lt = videoFilmRepository.findAllidvfWithListKeywordV2(lIdKeywords);
        return lt;
    }

    private List<String> findIdsVfWithKeywordenV2(RequestVideo req) {
        String keyword = getRequestTitleWithWildcardIfContainBeginEnd(
                req.getKeywordSel(), req.getKeywordfilm());
        List<String> lt = videoFilmRepository.findAllidvfWithKeywordV2(keyword);
        return lt;
    }

    private List<String> findIdsVfWithIdKindV2(List<Long> lidKind, List<Long> lidKindNot) {
        List<String> lvfl2sNot = new ArrayList<>();
        List<String> lvfl2s = videoFilmRepository.findAllidvfWithLidkindV2(lidKind);
        List<String> ltNot = new ArrayList<>();
        ;
        if (lidKindNot.size() != 0) {
            ltNot = videoFilmRepository.findAllidvfWithLidkindV2(lidKindNot);
            getListVFL2SWithListTupleV2(ltNot, lvfl2sNot);
            if (lvfl2sNot.size() != 0) {
                lvfl2s = reduceListVFL2SWithListAMinusListBV2(lvfl2s, lvfl2sNot);
            }
        }
        return lvfl2s;
    }

    private List<String> reduceListVFL2SWithListAMinusListBV2(
            List<String> lvfl2s,
            List<String> lvfl2sNot) {
        if (lvfl2sNot.size() == 0) return lvfl2s;
        List<String> lvfl2sNew = new ArrayList<>();

        lvfl2s.forEach(v -> {
            boolean test = false;
            for (String f : lvfl2sNot) {
                if (v.equals(f)) {
                    test = true;
                    break;
                }
            }
            if (!test) {
                lvfl2sNew.add(v);
            }
        });
        return lvfl2sNew;
    }

    private List<Long> getAllIdKindAndCompareWithList(String kindStr) {
        String[] importTbl = kindStr.split("-");
        List<Long> lidkindDb = getAllIdKind();
        List<Long> lidkindNew = new ArrayList<>();
        lidkindDb.forEach(id -> {
            for (String kind : importTbl) {
                if (kind.equals(id.toString())) lidkindNew.add(id);
            }
        });
        return lidkindNew;
    }

    private List<Long> getAllIdKind() {
        return videoKindRepository.findAllIdVideoKind();
    }

    /*********************************************************************
     ******************* getReqImportSupportwidthSize ********************
     *********************************************************************/

    private List<String> searchOnImportSupportwidthSize(
            List<String> lvflPreviousSearch,
            RequestVideo req, String login, int states) {

        List<String> lvflNew = new ArrayList<>();
        String imp = req.getImportStr();
        List<Long> lidVNE;

        //get list idsSupportPath what works
        if (imp.length() == 0) {
            lidVNE = getAllIdSupport(login);
        } else {
            lidVNE = getAllIdSupportAndCompareWithList(login, imp);
        }
        if (lidVNE.size() == 0) return lvflNew;

        //get all typeMmi who works with supportPath & transform in List<VideoFilmLight2Search>
        List<String> lvfl2sWithImport = findIdsVfWithIdsVNEV2(lidVNE, login);

        // Merge if reqTitle or reqType is activated
        if ((states & 5) > 0) {
            return mergeTwoListIdVideoFilm(lvflPreviousSearch, lvfl2sWithImport);
        } else {
            return lvfl2sWithImport;
        }
    }

    private List<String> merge2ListVfl2sV2(
            List<String> lvflPreviousSearch,
            List<String> lvfl2sWithImport) {
        List<String> lvfl2sNew = new ArrayList<>();
        lvfl2sWithImport.forEach(vfl -> {
            for (String old : lvflPreviousSearch) {
                if (vfl.equals(old)) {
                    lvfl2sNew.add(vfl);
                    break;
                }
            }
            ;
        });
        return lvfl2sNew;
    }

    private List<String> findIdsVfWithIdsVNEV2(List<Long> lidVNE, String login) {
        List<String> lt = videoFilmRepository.findAllIdsWithIdsVNEV2(lidVNE, login);
        return lt;
    }

    private List<Long> getAllIdSupportAndCompareWithList(String login, String imp) {
        String[] importTbl = imp.split("-");
        List<Long> lIdSupportDb = videoNameExportRepository.findAllIdsVNE(login);
        List<Long> lIdSupport = new ArrayList<>();
        lIdSupportDb.forEach(id -> {
            for (String importStr : importTbl) {
                if (importStr.equals(id.toString())) lIdSupport.add(id);
            }
        });
        return lIdSupport;
    }

    private List<Long> getAllIdSupport(String login) {
        return videoNameExportRepository.findAllIdsVNE(login);
    }


    /*********************************************************************
     *********************** getReqNamesAndRoles *************************
     *********************************************************************/

    private List<String> searchOnNameandroles
    (List<String> lvfl, RequestVideo req, int states) {
        String requestName = getRequestTitleWithWildcardIfContainBeginEnd(
                req.getKeywordNameIsSel(), req.getNameArtist());
        List<FilmArtistLight> lfal = new ArrayList<>();
        switch (req.getCharErrorNameIsSel()) {
            case (0):
                lfal = getNameLightWithOutLevenshtein(lfal, req, requestName);
                break;
            case (1):
                requestName = getRequestTitleWithWildcardOnPonctuation(requestName);
                lfal = getNameLightWithOutLevenshtein(lfal, req, requestName);
                break;
            case (2):
            case (3):
            case (4):
                String test = requestName.replaceAll("[%_]", "");
                if (test.length() <= req.getCharError()) break;
                requestName = getRequestTitleWithWildcardOnPonctuation(requestName);
                lfal = getNameLightWithLevenshtein(lfal, req, requestName);
                break;
            default:
                break;
        }
        List<String> lvfl2sNew = new ArrayList<>();
        lfal.forEach((fal) -> {
            lvfl2sNew.add(fal.getIdVideoFilm());
        });
        // Merge if reqTitle or reqType or reqImport is activated
        if ((states & 13) > 0) {
            return mergeTwoListIdVideoFilm(lvfl, lvfl2sNew);
        } else {
            return lvfl2sNew;
        }
    }

    private List<FilmArtistLight> getNameLightWithOutLevenshtein(
            List<FilmArtistLight> lfal, RequestVideo req, String requestName) {
        List<Tuple> lt = new ArrayList<>();
        List<FilmArtistLight> lfalTmp = new ArrayList<>();
        if (isAllRolesIsTrue(req.getRoleList())) {
            // All roles
            lt = videoFilmArtistRepository.findNameContentRequest(
                    requestName, req.getRoleList().getActor(),
                    req.getRoleList().getDirector(), req.getRoleList().getMusic(),
                    req.getRoleList().getProducer(), req.getRoleList().getWriter());
            lfalTmp = tupleToListFilmArtistLight(lt);
        } else {
            // severals roles or juste one...
            List<FilmArtistLight> lfalTmp2 = new ArrayList<>();
            //Search writer
            if (req.getRoleList().getWriter()) {
                lt = videoFilmArtistRepository.findNameContentRequestWithWriter(
                        requestName, req.getRoleList().getWriter());
                lfalTmp = tupleToListFilmArtistLight(lt);
            }
            // search producer
            if (req.getRoleList().getProducer()) {
                lt = videoFilmArtistRepository.findNameContentRequestWithProducer(
                        requestName, req.getRoleList().getProducer());
                lfalTmp2 = tupleToListFilmArtistLight(lt);
                lfalTmp = addAllWithoutDuplicateIdVFilmInListFilmArtistLight(lfalTmp, lfalTmp2);
            }
            // search music
            if (req.getRoleList().getMusic()) {
                lt = videoFilmArtistRepository.findNameContentRequestWithMusic(
                        requestName, req.getRoleList().getMusic());
                lfalTmp2 = tupleToListFilmArtistLight(lt);
                lfalTmp = addAllWithoutDuplicateIdVFilmInListFilmArtistLight(lfalTmp, lfalTmp2);
            }
            // search director
            if (req.getRoleList().getDirector()) {
                lt = videoFilmArtistRepository.findNameContentRequestWithDirector(
                        requestName, req.getRoleList().getDirector());
                lfalTmp2 = tupleToListFilmArtistLight(lt);
                lfalTmp = addAllWithoutDuplicateIdVFilmInListFilmArtistLight(lfalTmp, lfalTmp2);
            }
            // search actor
            if (req.getRoleList().getActor()) {
                lt = videoFilmArtistRepository.findNameContentRequestWithActor(
                        requestName, req.getRoleList().getActor());
                lfalTmp2 = tupleToListFilmArtistLight(lt);
                lfalTmp = addAllWithoutDuplicateIdVFilmInListFilmArtistLight(lfalTmp, lfalTmp2);
            }
        }
        lfal.addAll(lfalTmp);
        return lfal;
    }

    private List<FilmArtistLight> addAllWithoutDuplicateIdVFilmInListFilmArtistLight(
            List<FilmArtistLight> lfalTmp, List<FilmArtistLight> lfalTmp2) {
        List<FilmArtistLight> lfalFinal = new ArrayList<>();
        lfalTmp2.addAll(lfalTmp);
        lfalTmp2.forEach(v -> {
            boolean test = false;
            for (FilmArtistLight vfFinal : lfalFinal) {
                if (v.getIdVideoFilm().equals(vfFinal.getIdVideoFilm())) {
                    test = true;
                    break;
                }
            }
            if (!test) lfalFinal.add(v);
        });
        return lfalFinal;
    }

    private List<FilmArtistLight> tupleToListFilmArtistLight(List<Tuple> lt) {
        List<FilmArtistLight> lfalTmp = new ArrayList<>();
        if (lt.size() > 0) {
            for (Tuple t : lt) {
                lfalTmp.add(new FilmArtistLight(
                        (String) t.toArray()[0],
                        (int) t.toArray()[1],
                        (int) t.toArray()[2]
                ));
            }
        }
        return lfalTmp;
    }

    private boolean isAllRolesIsTrue(Roles r) {
        return r.getActor() && r.getDirector() && r.getMusic()
                && r.getProducer() && r.getWriter();
    }

    private List<FilmArtistLight> getNameLightWithLevenshtein(
            List<FilmArtistLight> lfal, RequestVideo req, String requestName) {
        List<VideoArtist> lva = findAllVideoArtistWithLevenshtein(req, requestName);
        for (VideoArtist va : lva) {
            getNameLightWithOutLevenshtein(lfal, req, va.getFirstLastName());
        }
        lfal = eraseDuplicateOfListFilmArtistLight(lfal);
        return lfal;
    }

    private List<FilmArtistLight> eraseDuplicateOfListFilmArtistLight(
            List<FilmArtistLight> lfal) {
        List<FilmArtistLight> lfalNew = new ArrayList<>();
        lfal.forEach(va -> {
            boolean test = false;
            for (FilmArtistLight fal : lfalNew) {
                if (va.getIdVideoFilm().equals(fal.getIdVideoFilm())) {
                    test = true;
                    break;
                }
            }
            if (!test) lfalNew.add(va);
        });
        return lfalNew;
    }

    private List<VideoArtist> findAllVideoArtistWithLevenshtein(
            RequestVideo req, String requestName) {
        List<VideoArtist> lva = new ArrayList<>();
        lva = findListVideoArtistWithLevenshteinLevelN(lva, "", requestName,
                req.getCharErrorNameIsSel() - 1);
        return lva;
    }

    private List<VideoArtist> findListVideoArtistWithLevenshteinLevelN(List<VideoArtist> lva
            , String begin, String end, int level) {
        int requestNameLenght = end.length();
        level--;
        for (int i = 0; i < requestNameLenght; i++) {
            char chartest = end.charAt(i);
            if (chartest == ('%') || chartest == '_') continue;
            String end1 = end.substring(0, i);
            String end1b = end.substring(0, i + 1);
            String end2 = end.substring(i + 1, requestNameLenght);
            String end2b = end.substring(i + 1, requestNameLenght);

            //search by deletion of a character
            List<VideoArtist> lvaTmp = videoArtistRepository.findAllLikeFirstLastName(begin + end1 + end2);
            concatWithoutDuplicatesLva(lva, lvaTmp);
            if (level > 0 && end2.length() > 1) {
                findListVideoArtistWithLevenshteinLevelN(
                        lva, begin + end1, end2, level);
            }
            //search by substitution of a character
            lvaTmp = videoArtistRepository.findAllLikeFirstLastName(begin + end1 + "_" + end2);
            concatWithoutDuplicatesLva(lva, lvaTmp);
            if (level > 0 && end2.length() > 1) {
                findListVideoArtistWithLevenshteinLevelN(
                        lva, begin + end1 + "_", end2, level);
            }

            //search by adding a character
            /*lvaTmp =  videoArtistRepository.findAllLikeFirstLastName(begin + end1b + "_" + end2b);
            concatWithoutDuplicatesLva(lva, lvaTmp);
            if (level > 0 && end2.length() > 1) {
                findListVideoArtistWithLevenshteinLevelN(
                        lva, begin + end1b + "_", end2b, level);
            }*/
        }
        return lva;
    }

    private void concatWithoutDuplicatesLva(
            List<VideoArtist> lva, List<VideoArtist> lvtlTmp) {
        if (lvtlTmp.size() != 0) {
            for (VideoArtist vaTmp : lvtlTmp) {
                boolean test = false;
                for (VideoArtist va : lva) {
                    if (va.getFirstLastName().equals(vaTmp.getFirstLastName())) {
                        test = true;
                        break;
                    }
                }
                if (!test) lva.add(vaTmp);
            }
        }
    }

    /*********************************************************************
     *********************** getReqTypeEpisodeSeason *********************
     *********************************************************************/

    private List<String> searchOnTypeEpisodeSeason(List<String> lvfl,
                                                   RequestVideo req, int states) {
        String reqNameSerie = getRequestTitleWithWildcardIfContainBeginEnd(
                req.getKeywordSerie(), req.getNameSerie());

        List<String> ltml = getTypeMmiLightsV2(req, reqNameSerie);
        List<String> lvtlNew = new ArrayList<>();
        if (ltml.size() != 0) {
            for (String id : ltml) {
                lvtlNew.add(id);
            }
        } else return lvtlNew;
        if ((states & 1) == 1) {
            lvtlNew = mergeTwoListIdVideoFilm(lvfl, lvtlNew);
        }
        return lvtlNew;
    }

    private boolean ifFilterTitlescoreyearNotModify(RequestVideo req) {
        return /*req.getReqTitleScoreYear();*/req.getTitle().equals("") &&
                req.getScoreMin() == 0 && req.getScoreMax() >= 99 &&
                req.getYearMin() <= 1880 && req.getYearMax() >= 2050 &&
                req.getYearMayNull() && req.getUserLightWithScore().size() == 0;
    }

    @NotNull
    private List<String> getTypeMmiLightsV2(RequestVideo req, String reqNameSerie) {
        List<String> lid = new ArrayList<>();
        if (req.getListType().size() == 0 || req.getListType().size() >= 12) {
            if (reqNameSerie.equals("") || reqNameSerie.equals("%")) {
                // search with nullable
                lid = typeMmiRepository.getTypeMmiLightAllTypeWithNameserieNullV2(
                        req.getSeasonMin(), req.getSeasonMax(),
                        req.getEpisodeMin(), req.getEpisodeMax()
                );
            } else {
                //Without nullable
                lid = typeMmiRepository.getTypeMmiLightAllTypeV2(
                        reqNameSerie,
                        req.getSeasonMin(), req.getSeasonMax(),
                        req.getEpisodeMin(), req.getEpisodeMax()
                );
            }

        } else {
            if (reqNameSerie.equals("") || reqNameSerie.equals("%")) {
                // search with nullable
                lid = typeMmiRepository.getTypeMmiLightWithNameserieNullV2(
                        req.getListType(),
                        req.getSeasonMin(), req.getSeasonMax(),
                        req.getEpisodeMin(), req.getEpisodeMax()
                );
            } else {
                //Without nullable
                lid = typeMmiRepository.getTypeMmiLightV2(
                        req.getListType(), reqNameSerie,
                        req.getSeasonMin(), req.getSeasonMax(),
                        req.getEpisodeMin(), req.getEpisodeMax()
                );
            }

        }
        while (lid.remove(null)) ;
        return lid;
    }

    /*********************************************************************
     *********************** getReqTitleScoreYear ************************
     *********************************************************************/
    @Override
    public List<TypeName> getAllTypeName(String login) {
        return typeNameRepository.findAll();
    }

    @Override
    public List<UserLight> getListForScores(String login) {
        List<UserLight> lul = new ArrayList<>();
        List<Tuple> lt = myUserRepository.lUserWithId();

        if (lt.size() != 0) {
            lt.forEach(t -> {
                if((Boolean) t.toArray()[2]){
                    UserLight ul = new UserLight(
                            (Long) t.toArray()[0],
                            (String) t.toArray()[1]
                    );
                    if (!ul.getLogin().equals("admin")) lul.add(ul);
                }
            });
        }
        return lul;
    }

    @Override
    public List<LinkIdvfMmi> getMmiWithIdVideo(OptionGetMmiWithIdVf lIdVideowithoption, String login, String idsVne) {
        List<LinkIdvfMmi> linksIdvfMmi = new ArrayList<>();
        List<String> lIdVideo = lIdVideowithoption.getLidvf();
        if (lIdVideo.size() == 0) return linksIdvfMmi;
        lIdVideo.forEach(id -> {
            List<Long> idstmmi = null;
            //Search mmi in fct of season,episode&nameSerie
            if (lIdVideowithoption.getWithOptionL1()) {
                String nameSerie = getRequestTitleWithWildcardIfContainBeginEnd(
                        lIdVideowithoption.getKeywordSerie(),
                        lIdVideowithoption.getNameSerie());
                if (nameSerie.equals("%") || nameSerie.equals("%%")) {
                    idstmmi = typeMmiRepository.findIdsTmmiByIdVideoFilmWithSeasonEpisodeAndNameserieMaybeNull(
                            id, lIdVideowithoption.getSeasonMin(), lIdVideowithoption.getSeasonMax(),
                            lIdVideowithoption.getEpisodeMin(), lIdVideowithoption.getEpisodeMax());
                } else {
                    idstmmi = typeMmiRepository.findIdsTmmiByIdVideoFilmWithSeasonEpisodeNameserie(
                            id, lIdVideowithoption.getSeasonMin(), lIdVideowithoption.getSeasonMax(),
                            lIdVideowithoption.getEpisodeMin(), lIdVideowithoption.getEpisodeMax()
                            , nameSerie
                    );
                }
            } else {
                idstmmi = typeMmiRepository.findIdsTmmiByIdVideoFilm(id);
            }
            if (idstmmi.size() != 0) {

                //Search in fct of import
                List<Long> idsVideoNameExport;
                if (lIdVideowithoption.getWithOptionL2()
                        && !lIdVideowithoption.getImportStr().equals("")) {

                    idsVideoNameExport = getAllIdSupportAndCompareWithList(login,
                            lIdVideowithoption.getImportStr());
                } else {
                    idsVideoNameExport = videoNameExportRepository.findAllidsVne();
                }
                //Get mmi list for one idVf
                List<MyMediaInfo> lmmi = new ArrayList<>();
                if (lIdVideowithoption.getWithOptionL3()) {
                    //Init
                    List<MyMediaInfo> lmmi1 = new ArrayList<>();
                    List<MyMediaInfo> lmmi2 = new ArrayList<>();
                    int test = 0;
                    if (!(lIdVideowithoption.getDurationMin() == 0 &&
                            lIdVideowithoption.getDurationMax() == 1000 &&
                            lIdVideowithoption.getWidthMin() == 0 &&
                            lIdVideowithoption.getWidthMax() == 10000)) test = 1;
                    if (lIdVideowithoption.getLanguagesStr().length() != 0) test = test + 2;

                    //Search
                    if ((test & 1) == 1) lmmi1 = myMediaInfoRepository
                            .findAllByAllTypeMyMediaInfoAndUserLight6AndDurationWidth(
                                    idstmmi, idsVideoNameExport,
                                    Double.valueOf(lIdVideowithoption.getDurationMin()) * 60,
                                    Double.valueOf(lIdVideowithoption.getDurationMax()) * 60,
                                    lIdVideowithoption.getWidthMin(), lIdVideowithoption.getWidthMax());
                    if ((test & 2) == 2) {
                        String[] lstr = lIdVideowithoption.getLanguagesStr().split("-");
                        List<Long> lIdLanguages = Arrays.stream(lstr).map(Long::parseLong).collect(Collectors.toList());
                        lmmi2 = myMediaInfoRepository
                                .findAllByAllTypeMyMediaInfoAndUserLight6AndDurationWidthAndLanguage(
                                        idstmmi, idsVideoNameExport, lIdLanguages);
                    }
                    //Merge
                    if ((test & 3) == 1) lmmi = lmmi1;
                    if ((test & 3) == 2) lmmi = lmmi2;
                    if ((test & 3) == 3) { //merge lmmi1&lmmi2 -> lmmi
                        lmmi = mergeToListMmi(lmmi1, lmmi2);
                    }

                } else {
                    lmmi = myMediaInfoRepository
                            .findAllByAllTypeMyMediaInfoAndUserLight6(idstmmi, idsVideoNameExport);
                }
                //Add id & lmmi -> linsIdvfMmi
                linksIdvfMmi.add(new LinkIdvfMmi(id, lmmi));
            }
        });
        return linksIdvfMmi;
    }

    private List<MyMediaInfo> mergeToListMmi(List<MyMediaInfo> lmmi1, List<MyMediaInfo> lmmi2) {
        List<MyMediaInfo> lmmi = new ArrayList<>();
        lmmi1.forEach(m1 -> {
            for (MyMediaInfo m2 : lmmi2) {
                if (m1.getIdMyMediaInfo().equals(m2.getIdMyMediaInfo())) {
                    lmmi.add(m1);
                    break;
                }
            }
        });
        return lmmi;
    }

    private Page<VideoFilm> getPageWithListVideofilmlightV2(
            List<String> lvfl, Pageable p, RequestVideo req, MySorter sorter) {
        while (lvfl.remove(null)) ;
        //Result is empty
        int resultSize = lvfl.size();
        if (((req.getPageNumber()) * req.getPageSize()) >= resultSize || resultSize == 0)
            return newPage(req, new ArrayList<>(), resultSize, p);
        //If it's not empty -> create VideoFilm list
        List<VideoFilm> lvf = new ArrayList<>();
        boolean toSort = !req.getSortBy().equals("");
        // Call Sort by
        if (toSort) lvfl = sortListIdvf(lvfl, sorter);
        resultSize = lvfl.size();
        int valInit = req.getPageSize() * req.getPageNumber();
        for (int i = 0; i < req.getPageSize(); i++) {
            int pointer = i + valInit;
            // Stop, if the page is finish
            if (pointer >= resultSize) break;
            String vtl2s = lvfl.get(pointer);
            VideoFilm vf = videoFilmRepository.findById(vtl2s).orElse(null);
            if (vf != null) {
                lvf.add(vf);
            }
        }
        if (toSort && sorter.getSortBy().equals("scoreOnHundred") && sorter.getIdPers() != 0) {
        }
        return newPage(req, lvf, resultSize, p);
    }

    private List<String> sortListIdvf(List<String> lvf, MySorter sorter) {
        switch (sorter.getSortBy()) {
            case ("idVideo"):
                if (sorter.getAsc()) {
                    return getListVideoFilmsSortByIddb(lvf, sorter, true);
                } else {
                    return getListVideoFilmsSortByIddb(lvf, sorter, false);
                }

            case ("year"):
                if (sorter.getAsc()) {
                    return getListVideoFilmsSortByYear(lvf, sorter, true);
                } else {
                    return getListVideoFilmsSortByYear(lvf, sorter, false);
                }
            case ("scoreOnHundred"):
                MyUser mu = myUserRepository.findByIdMyUser(sorter.getIdPers());
                if (mu == null) sorter.setIdPers(0L);
                if (sorter.getIdPers() == 0) {
                    if (sorter.getAsc()) {
                        return getListVideoFilmsSortByScore(lvf, sorter, true);
                    } else {
                        return getListVideoFilmsSortByScore(lvf, sorter, false);
                    }
                } else {
                    if (sorter.getAsc()) {
                        return getPageVideoFilmsSortByScoreUser(lvf, true, sorter.getIdPers());
                    } else {
                        return getPageVideoFilmsSortByScoreUser(lvf, false, sorter.getIdPers());
                    }
                }
            default:
                return getListVideoFilmsSortByYear(lvf, sorter, false);
        }
    }

    /*@NotNull
    private Page<VideoFilm> getPageVideoFilmsSortByScoreUser(Pageable p, Long idUser, boolean asc) {
        List<Tuple> lids = asc ?
                videoFilmRepository.findAllSortByUserScoreAsc(idUser) :
                videoFilmRepository.findAllSortByUserScoreDesc(idUser);
        List<String> lids1 = lids.stream()
                .map(i -> (String) i.toArray()[0])
                .collect(Collectors.toList());
        int sizeMax = lids.size();
        int min = getMinOfPage(p.getPageSize(), sizeMax, p.getPageNumber());
        int max = getMaxOfPage(p.getPageSize(), sizeMax, p.getPageNumber() + 1);
        List<String> lids2 = lids1.subList( min, max);
        List<VideoFilm> lvf3 = asc ?
                videoFilmRepository.findwithIdAndUserSortByScoreAsc(lids2, idUser) :
                videoFilmRepository.findwithIdAndUserSortByScoreDesc(lids2, idUser);
        orderListVfByscoreuser(lvf3, idUser, asc);
        Page<VideoFilm> pvf = new PageImpl<VideoFilm>(lvf3, p, lids1.size());
        return pvf;
    }

    private void orderListVfByscoreuser(List<VideoFilm> lvf,
                                                     Long idUser, boolean asc) {
        for(int i = 0; i < lvf.size()-1; i++){
            VideoFilm a = lvf.get(i);
            int i1 = 0;
            for(int k = 0; k < a.getVideoUserScores().size(); k++){
                if(a.getVideoUserScores().get(k).getMyUser().getIdMyUser()==idUser)i1=k;
            }
            for(int j = i; j < lvf.size(); j++){
                VideoFilm b = lvf.get(j);
                int j1=0;
                for(int l = 0; l < b.getVideoUserScores().size(); l++){
                    if(b.getVideoUserScores().get(l).getMyUser().getIdMyUser()==idUser)j1=l;
                }
                boolean invert = false;
                if(asc){
                    if(a.getVideoUserScores().get(i1).getNoteOnHundred()>
                            b.getVideoUserScores().get(j1).getNoteOnHundred()) invert = true;
                }else{
                    if(a.getVideoUserScores().get(i1).getNoteOnHundred()<
                            b.getVideoUserScores().get(j1).getNoteOnHundred()) invert = true;
                }
                if(invert){
                    lvf.set(i, b);
                    lvf.set(j, a);
                    a=lvf.get(i);
                }
            }
        }
    }*/


    private List<String> getPageVideoFilmsSortByScoreUser(List<String> lvf,
                                                          boolean asc, Long idUser) {
        List<Tuple> lt = asc ?
                videoFilmRepository.findAllSortByUserScoreAscWithIds(idUser, lvf) :
                videoFilmRepository.findAllSortByUserScoreDescWithIds(idUser, lvf);
        return lt.stream().map(i -> (String) i.toArray()[0]).collect(Collectors.toList());
    }

    private List<String> getListVideoFilmsSortByScore(List<String> lvf, MySorter sorter, boolean asc) {
        List<Tuple> lt = asc ?
                videoFilmRepository.findAllSortByScoreAscWithList(lvf) :
                videoFilmRepository.findAllSortByScoreDescWithList(lvf);
        return lt.stream().map(i -> (String) i.toArray()[0]).collect(Collectors.toList());
    }

    private List<String> getListVideoFilmsSortByYear(List<String> lvf, MySorter sorter, boolean asc) {
        List<Tuple> lt = asc ?
                videoFilmRepository.findAllSortByYearAscWithList(lvf) :
                videoFilmRepository.findAllSortByYearDescWithList(lvf);
        return lt.stream().map(i -> (String) i.toArray()[0]).collect(Collectors.toList());
    }

    private List<String> getListVideoFilmsSortByIddb(List<String> lvf, MySorter sorter, boolean asc) {
        List<Tuple> lt = asc ?
                videoFilmRepository.findAllSortByIddbAscWithList(lvf) :
                videoFilmRepository.findAllSortByIddbDescWithList(lvf);
        return lt.stream().map(i -> (String) i.toArray()[0]).collect(Collectors.toList());
    }

    /*********************************************************************
     *********************** getReqTitleScoreYear ************************
     *********************************************************************/

    private List<String> searchOnTitleScoreYear(
            List<String> lidtt, RequestVideo req, MySorter sorter) {

        if (ifRequestTitleIsAnIdtt(req)) {
            switch (req.getCharError()) {
                case (2):
                case (3):
                case (4):
                    lidtt = getListIdttWithLevenshteinV2(lidtt, req, sorter);
                    break;
                default:
                    List<String> lidttnew =
                            getListIdttV2(req.getTitle(), sorter);
                    if (lidttnew.size() > 0) lidtt.addAll(lidttnew);
                    lidtt = eraseDuplicateInLidtt(lidtt);
                    break;
            }
        } else {
            List<String> lidttTmp = new ArrayList<>();
            String requestTitle = getRequestTitleWithWildcardIfContainBeginEnd(
                    req.getKeyword(), req.getTitle());

            switch (req.getCharError()) {
                case (0):
                    lidttTmp = getTitleLightWithOutLevenshteinV2(lidttTmp, req, requestTitle, sorter);
                    break;
                case (1):
                    requestTitle = getRequestTitleWithWildcardOnPonctuation(requestTitle);
                    lidttTmp = getTitleLightWithOutLevenshteinV2(lidttTmp, req, requestTitle, sorter);
                    break;
                case (2):
                case (3):
                case (4):
                    String test = requestTitle.replaceAll("[%_]", "");
                    if (test.length() <= req.getCharError()) break;
                    requestTitle = getRequestTitleWithWildcardOnPonctuation(requestTitle);
                    lidttTmp = getTitleLightWithLevenshtein(lidttTmp, req, requestTitle, sorter);
                    break;
                default:
                    break;
            }
            mergeTwoListOfString(lidtt, lidttTmp);

            if (req.getUserLightWithScore().size() > 0)
                lidttTmp = lidtt = filterOnUsersScoreV3(lidtt, req);
            lidtt = compareVideoFilmLight2SearchV2(lidtt, lidttTmp);
        }
        return lidtt;
    }

    private List<String> compareVideoFilmLight2SearchV2(
            List<String> lvfl, List<String> lvflTmp) {
        List<String> lvflNew = new ArrayList<>();
        if (lvfl.size() < lvflTmp.size()) {
            lvfl.forEach(vfl -> {
                for (String vflTmp : lvflTmp) {
                    if (vfl.equals(vflTmp)) {
                        lvflNew.add(vflTmp);
                        break;
                    }
                }
            });
        } else {
            lvflTmp.forEach(vflTmp -> {
                for (String vfl : lvfl) {
                    if (vfl.equals(vflTmp)) {
                        lvflNew.add(vflTmp);
                        break;
                    }
                }
            });
        }
        return lvflNew;
    }

    private List<String> filterOnUsersScoreV3(List<String> lidOld, RequestVideo req) {
        List<String> lidttTmp = lidOld;
        if (lidttTmp.size() == 0) return lidttTmp;
        for (UserLightWithScore ul : req.getUserLightWithScore()) {
            if (lidttTmp.size() != 0) {
                if (ul.getScoreMayNull()) {
                    List<Tuple> ltnotinrange = videoFilmRepository.findAlltByUserScoreNotNullAndNotInRange(
                            ul.getIdMyUser(), lidttTmp, ul.getScoreMin(), ul.getScoreMax());
                    List<String> lidnotinrange = ltnotinrange.stream()
                            .map(i -> (String) i.toArray()[0])
                            .collect(Collectors.toList());
                    lidttTmp.removeAll(lidnotinrange);
                } else {
                    List<Tuple> ltnotnull = videoFilmRepository.findAlltByUserScoreNotNull(
                            ul.getIdMyUser(), lidttTmp, ul.getScoreMin(), ul.getScoreMax());
                    lidttTmp = ltnotnull.stream()
                            .map(i -> (String) i.toArray()[0])
                            .collect(Collectors.toList());
                }
            }
        }
        return lidttTmp;
    }

    private List<String> filterOnUsersScoreV2(List<String> lvfl,
                                              RequestVideo req, MySorter sorter) {
        List<String> lidttTmp = new ArrayList<>();

        boolean isFirstRound = true;
        for (UserLightWithScore ul : req.getUserLightWithScore()) {
            //search with score not null
            List<String> lstrOfIdVideo =
                    sorter.getAsc()
                            ?
                            videoUserScoreRepository
                                    .getAllIdVideoWithScoreAndLoginWithoutNullableAscV2(
                                            ul.getIdMyUser(), ul.getScoreMin(), ul.getScoreMax(),
                                            sorter.getSortBy())
                            :
                            videoUserScoreRepository
                                    .getAllIdVideoWithScoreAndLoginWithoutNullableDescV2(
                                            ul.getIdMyUser(), ul.getScoreMin(), ul.getScoreMax(),
                                            sorter.getSortBy());
            if (lstrOfIdVideo.size() == 0 && !ul.getScoreMayNull()) return new ArrayList<>();
//            List<String> lvsl = convertTupleToListVideoUserLightV2(lstrOfIdVideo);
            List<String> lvsl = lstrOfIdVideo;
            if (ul.getScoreMayNull()) {
                List<String> lvflTmp2 = new ArrayList<>();
                if (isFirstRound) {
                    for (String id : lvfl) {
                        if (videoUserScoreRepository
                                .findVusNumberWithIdvideoAndIdLogin(
                                        ul.getIdMyUser(), id) == 0) {
                            lidttTmp.add(id);
                        }
                    }
                    lidttTmp = concatListVFL2SWithListVUSLV2(lidttTmp, lvfl);
                    lidttTmp = mergeTwoListIdVideoFilm(lvfl, lidttTmp);
                } else {
                    for (String id : lidttTmp) {
                        if (videoUserScoreRepository
                                .findVusNumberWithIdvideoAndIdLogin(
                                        ul.getIdMyUser(), id) == 0) {
                            lvflTmp2.add(id);
                        }
                    }
                    lvflTmp2 = concatListVFL2SWithListVUSLV2(lvflTmp2, lvsl);
                    lidttTmp = mergeTwoListIdVideoFilm(lidttTmp, lvflTmp2);
                }
            } else {
                if (isFirstRound) {
                    lidttTmp = mergeListVFL2SWithListVUSLV2(lvfl, lvsl);
                } else {
                    lidttTmp = mergeListVFL2SWithListVUSLV2(lidttTmp, lvsl);
                }
            }
            isFirstRound = false;
        }
        return lidttTmp;
    }

    private List<String> concatListVFL2SWithListVUSLV2(
            List<String> lvflTmp, List<String> lvsl) {
        lvsl.forEach(vsl -> {
            lvflTmp.add(vsl);
        });
        return lvflTmp;
    }

    private List<String> mergeTwoListIdVideoFilm(
            List<String> lidvf,
            List<String> lidvfTmp) {
        List<String> lvflNew = new ArrayList<>();
        if (lidvfTmp.size() != 0 && lidvf.size() != 0) {
            lidvf.forEach(vfl -> {
                for (String vflTmp : lidvfTmp) {
                    if (vfl.equals(vflTmp)) {
                        lvflNew.add(vfl);
                        break;
                    }
                }
            });
        }
        return lvflNew;
    }

    private List<String> mergeTwoListVideoFilmLight2SearchV2(
            List<String> lvflTmp,
            List<String> lvfl) {
        List<String> lvflNew = new ArrayList<>();
        if (lvfl.size() < lvflTmp.size()) {
            lvfl.forEach(vfl -> {
                for (String vflTmp : lvflTmp) {
                    if (vfl.equals(vflTmp)) {
                        lvflNew.add(vfl);
                        break;
                    }
                }
            });
        } else {
            lvflTmp.forEach(vflTmp -> {
                for (String vfl : lvfl) {
                    if (vfl.equals(vflTmp)) {
                        lvflNew.add(vfl);
                        break;
                    }
                }
            });
        }
        return lvflNew;
    }

    @NotNull
    private List<String> mergeListVFL2SWithListVUSLV2
            (List<String> lvfl, List<String> lvsl) {
        if (lvsl.size() <= lvfl.size()) {
            lvfl = mergeListVFL2SWithLitleListVUSLV2(lvfl, lvsl);
        } else {
            lvfl = mergeListVFL2SWithLitleListVUSLV2(lvfl, lvsl);
        }
        return lvfl;
    }

    private List<String> mergeListVFL2SWithLitleListVUSLV2(
            List<String> lvflTmp,
            List<String> lvsl) {
        List<String> lvflNew = new ArrayList<>();
        if (lvsl.size() != 0) {
            lvsl.forEach(vsl -> {
                for (String vflTmp : lvflTmp) {
                    if (vflTmp.equals(vsl)) {
                        lvflNew.add(vflTmp);
                        break;
                    }
                }
            });
        }
        return lvflNew;
    }

    @NotNull
    private List<String> convertTupleToListVideoUserLightV2(
            List<String> lstrOfIdVideo) {
        List<String> l = new ArrayList<>();
        if (lstrOfIdVideo.size() != 0) {
            for (String t : lstrOfIdVideo) {
                l.add(t);
            }
        }
        return l;
    }

    private List<String> getListIdttWithLevenshteinV2(
            List<String> lidtt, RequestVideo req, MySorter sorter) {
        getListIdttWithLevenshteinLevelNV2(
                lidtt, req.getTitle().substring(0, 2),
                req.getTitle().substring(2),
                req.getCharError() - 1, sorter);
        return eraseDuplicateInLidtt(lidtt);
    }

    private List<String> eraseDuplicateInLStr(
            List<String> lvfl) {
        List<String> newLvfl = new ArrayList<>();

        for (String old : lvfl) {
            boolean test = false;
            for (String newVfl : newLvfl) {
                if (old.equals(newVfl)) {
                    test = true;
                    break;
                }
            }
            if (!test) newLvfl.add(old);
        }
        return newLvfl;
    }

    private List<String> eraseDuplicateInLidtt(List<String> lidtt) {
        List<String> lidttNew = new ArrayList<>();
        for (String idold : lidtt) {
            boolean test = false;
            for (String idnew : lidttNew) {
                if (idold.equals(idnew)) {
                    test = true;
                    break;
                }
            }
            if (!test) lidttNew.add(idold);
        }
        return lidttNew;
    }

    private void getListIdttWithLevenshteinLevelNV2(
            List<String> lidtt, String begin,
            String end, int level, MySorter sorter) {
        level--;
        int requestTitleLenght = end.length();
        for (int i = 0; i < requestTitleLenght; i++) {
            String end1 = end.substring(0, i);
            String end2 = end.substring(i + 1, requestTitleLenght);
            //search by substitution of a character
            List<String> lidttNew =
                    getListIdttV2(begin + end1 + "_" + end2, sorter);
            if (lidttNew != null && lidttNew.size() > 0) lidtt.addAll(lidttNew);
            if (level > 0 && end2.length() > 1) {
                getListIdttWithLevenshteinLevelNV2(
                        lidtt,
                        begin + end1 + "_",
                        end2,
                        level, sorter);
            }
        }
    }

    private List<String> getListIdttV2(String request, MySorter sorter) {
        List<String> lid = sorter.getAsc() ?
                videoFilmRepository.getListIdWithSYAscV2(request,
                        sorter.getSortBy()) :
                videoFilmRepository.getListIdWithSYDescV2(request,
                        sorter.getSortBy());
        return lid;
    }

    private void getListVFL2SWithListTupleV2(List<String> lt, List<String> lvfl) {
        if (lt != null && lt.size() > 0) {
            lvfl.addAll(lt);
        }
    }

    private boolean ifRequestTitleIsAnIdtt(RequestVideo req) {
        return Pattern.matches("tt[\\d]{6,10}", req.getTitle());
    }

    private void mergeTwoListOfString(List<String> lvfl,
                                      List<String> lvflTmp) {
        for (String vflTmp : lvflTmp) {
            boolean test = false;
            for (String vfl : lvfl) {
                if (vfl.equals(vflTmp)) {
                    test = true;
                    break;
                }
            }
            if (!test) {
                lvfl.add(vflTmp);
            }
        }
    }

    private List<String> getTitleLightWithOutLevenshtein(List<String> lidttTmp,
                                                         RequestVideo req, String requestTitle, MySorter sorter) {
        // requestTitle, scoreMin, scoreMax, yearMin, yearMax, and idVideo
        if (sorter.getAsc()) {
            lidttTmp = videoFilmRepository
                    .findIdVideoByOneTitleWithYearAndScoreAsc(
                            requestTitle,
                            req.getScoreMin(),
                            req.getScoreMax(),
                            req.getYearMin(),
                            req.getYearMax(),
                            req.getScoreMayNull() ? 0 : 1,
                            req.getYearMayNull() ? 0 : 1,
                            "vt." + sorter.getSortBy());
        } else {
            lidttTmp = videoFilmRepository
                    .findIdVideoByOneTitleWithYearAndScoreDesc(
                            requestTitle,
                            req.getScoreMin(),
                            req.getScoreMax(),
                            req.getYearMin(),
                            req.getYearMax(),
                            req.getScoreMayNull() ? 0 : 1,
                            req.getYearMayNull() ? 0 : 1,
                            "vt." + sorter.getSortBy());
        }
        return lidttTmp;
    }

    private List<String> getTitleLightWithOutLevenshteinV2(List<String> lidttTmp,
                                                           RequestVideo req, String requestTitle, MySorter sorter) {
        // requestTitle, scoreMin, scoreMax, yearMin, yearMax, and idVideo
        switch (sorter.getSortBy()) {
            case ("year"):
                if (sorter.getAsc()) {
                    lidttTmp = videoFilmRepository
                            .findIdVideoByOneTitleWithYearAndScoreOrderYearAsc(
                                    requestTitle,
                                    req.getScoreMin(),
                                    req.getScoreMax(),
                                    req.getYearMin(),
                                    req.getYearMax(),
                                    req.getScoreMayNull() ? 0 : 1,
                                    req.getYearMayNull() ? 0 : 1);
                } else {
                    lidttTmp = videoFilmRepository
                            .findIdVideoByOneTitleWithYearAndScoreOrderYearDesc(
                                    requestTitle,
                                    req.getScoreMin(),
                                    req.getScoreMax(),
                                    req.getYearMin(),
                                    req.getYearMax(),
                                    req.getScoreMayNull() ? 0 : 1,
                                    req.getYearMayNull() ? 0 : 1);
                }
                break;
            case ("scoreOnHundred"):
                //TODO: add if sortOnIdUser
                if (sorter.getAsc()) {
                    lidttTmp = videoFilmRepository
                            .findIdVideoByOneTitleWithYearAndScoreOrderScoreAsc(
                                    requestTitle,
                                    req.getScoreMin(),
                                    req.getScoreMax(),
                                    req.getYearMin(),
                                    req.getYearMax(),
                                    req.getScoreMayNull() ? 0 : 1,
                                    req.getYearMayNull() ? 0 : 1);
                } else {
                    lidttTmp = videoFilmRepository
                            .findIdVideoByOneTitleWithYearAndScoreOrderScoreDesc(
                                    requestTitle,
                                    req.getScoreMin(),
                                    req.getScoreMax(),
                                    req.getYearMin(),
                                    req.getYearMax(),
                                    req.getScoreMayNull() ? 0 : 1,
                                    req.getYearMayNull() ? 0 : 1);
                }

                break;
            case ("idVideo"):
                if (sorter.getAsc()) {
                    lidttTmp = videoFilmRepository
                            .findIdVideoByOneTitleWithYearAndScoreOrderIdAsc(
                                    requestTitle,
                                    req.getScoreMin(),
                                    req.getScoreMax(),
                                    req.getYearMin(),
                                    req.getYearMax(),
                                    req.getScoreMayNull() ? 0 : 1,
                                    req.getYearMayNull() ? 0 : 1);
                } else {
                    lidttTmp = videoFilmRepository
                            .findIdVideoByOneTitleWithYearAndScoreOrderIdDesc(
                                    requestTitle,
                                    req.getScoreMin(),
                                    req.getScoreMax(),
                                    req.getYearMin(),
                                    req.getYearMax(),
                                    req.getScoreMayNull() ? 0 : 1,
                                    req.getYearMayNull() ? 0 : 1);
                }
                break;
            default:
                lidttTmp = videoFilmRepository
                        .findIdVideoByOneTitleWithYearAndScoreOrderScoreDesc(
                                requestTitle,
                                req.getScoreMin(),
                                req.getScoreMax(),
                                req.getYearMin(),
                                req.getYearMax(),
                                req.getScoreMayNull() ? 0 : 1,
                                req.getYearMayNull() ? 0 : 1);
        }
        return lidttTmp;
    }

    private List<String> searchTitleLightV2(String requestToUseP1,
                                            String requestToUseP2, RequestVideo req1, MySorter sorter) {
        List<String> lstrTmp = new ArrayList<>();
        String req = requestToUseP1;
        req = req + requestToUseP2;

        lstrTmp = getLidStrings(req1, sorter, req);

        return lstrTmp;
    }

    private List<String> getLidStrings(RequestVideo req,
                                       MySorter sorter, String requestTitle) {
        List<String> lidttTmp = new ArrayList();
        switch (sorter.getSortBy()) {
            case ("year"):
                if (sorter.getAsc()) {
                    lidttTmp = videoFilmRepository
                            .findIdVideoByOneTitleWithYearAndScoreOrderYearAsc(
                                    requestTitle,
                                    req.getScoreMin(),
                                    req.getScoreMax(),
                                    req.getYearMin(),
                                    req.getYearMax(),
                                    req.getScoreMayNull() ? 0 : 1,
                                    req.getYearMayNull() ? 0 : 1);
                } else {
                    lidttTmp = videoFilmRepository
                            .findIdVideoByOneTitleWithYearAndScoreOrderYearDesc(
                                    requestTitle,
                                    req.getScoreMin(),
                                    req.getScoreMax(),
                                    req.getYearMin(),
                                    req.getYearMax(),
                                    req.getScoreMayNull() ? 0 : 1,
                                    req.getYearMayNull() ? 0 : 1);
                }
                break;
            case ("scoreOnHundred"):
                if (sorter.getAsc()) {
                    lidttTmp = videoFilmRepository
                            .findIdVideoByOneTitleWithYearAndScoreOrderScoreAsc(
                                    requestTitle,
                                    req.getScoreMin(),
                                    req.getScoreMax(),
                                    req.getYearMin(),
                                    req.getYearMax(),
                                    req.getScoreMayNull() ? 0 : 1,
                                    req.getYearMayNull() ? 0 : 1);
                } else {
                    lidttTmp = videoFilmRepository
                            .findIdVideoByOneTitleWithYearAndScoreOrderScoreDesc(
                                    requestTitle,
                                    req.getScoreMin(),
                                    req.getScoreMax(),
                                    req.getYearMin(),
                                    req.getYearMax(),
                                    req.getScoreMayNull() ? 0 : 1,
                                    req.getYearMayNull() ? 0 : 1);
                }

                break;
            case ("idVideo"):
                if (sorter.getAsc()) {
                    lidttTmp = videoFilmRepository
                            .findIdVideoByOneTitleWithYearAndScoreOrderIdAsc(
                                    requestTitle,
                                    req.getScoreMin(),
                                    req.getScoreMax(),
                                    req.getYearMin(),
                                    req.getYearMax(),
                                    req.getScoreMayNull() ? 0 : 1,
                                    req.getYearMayNull() ? 0 : 1);
                } else {
                    lidttTmp = videoFilmRepository
                            .findIdVideoByOneTitleWithYearAndScoreOrderIdDesc(
                                    requestTitle,
                                    req.getScoreMin(),
                                    req.getScoreMax(),
                                    req.getYearMin(),
                                    req.getYearMax(),
                                    req.getScoreMayNull() ? 0 : 1,
                                    req.getYearMayNull() ? 0 : 1);
                }
                break;
            default:
                lidttTmp = videoFilmRepository
                        .findIdVideoByOneTitleWithYearAndScoreOrderScoreDesc(
                                requestTitle,
                                req.getScoreMin(),
                                req.getScoreMax(),
                                req.getYearMin(),
                                req.getYearMax(),
                                req.getScoreMayNull() ? 0 : 1,
                                req.getYearMayNull() ? 0 : 1);
        }
        return lidttTmp;
    }

    private List<String> searchTitleLight(String requestToUseP1,
                                          String requestToUseP2, RequestVideo req1, MySorter sorter) {
        List<String> lstrTmp = new ArrayList<>();
        String req = requestToUseP1;
        req = req + requestToUseP2;

        lstrTmp =
                sorter.getAsc() ?
                        videoFilmRepository.findIdVideoByOneTitleWithYearAndScoreAsc(
                                req, req1.getScoreMin(), req1.getScoreMax(),
                                req1.getYearMin(), req1.getYearMax(),
                                req1.getScoreMayNull() ? 0 : 1, req1.getYearMayNull() ? 0 : 1
                                , sorter.getSortBy()) :
                        videoFilmRepository.findIdVideoByOneTitleWithYearAndScoreDesc(
                                req, req1.getScoreMin(), req1.getScoreMax(),
                                req1.getYearMin(), req1.getYearMax(),
                                req1.getScoreMayNull() ? 0 : 1, req1.getYearMayNull() ? 0 : 1
                                , sorter.getSortBy());

        return lstrTmp;
    }

    @NotNull
    private Pageable getPageable(RequestVideo req) {
        int page = req.getPageNumber();
        int size = req.getPageSize() > 0 ? req.getPageSize() : 5;
        Sort s;
        if (req.getSortBy().length() != 0) {
            String sortTbl[] = req.getSortBy().split("-");
            int sortValue = Integer.parseInt(sortTbl[0]);
            boolean sortOrder = Integer.parseInt(sortTbl[1]) == 1;
            String sortStr = "";
            switch (sortValue) {
                case (0):
                    sortStr = "idVideo";
                    break;
                case (1):
                    sortStr = "scoreOnHundred";
                    break;
                default:
                    sortStr = "year";
            }
            if (sortOrder) {
                s = Sort.by(sortStr).ascending();
            } else {
                s = Sort.by(sortStr).descending();
            }
        } else {
            s = Sort.by("scoreOnHundred").descending();
        }
        return PageRequest.of(page, size, s);
    }

    private List<String> getTitleLightWithLevenshtein(List<String> lidttTmp,
                                                      RequestVideo req,
                                                      String requestTitle, MySorter sorter) {
        // get in DB List<Titlelight> with :
        // requestTitle, scoreMin, scoreMax, yearMin, yearMax, and idVideo
        List<String> tmp = getTitlesLightWithLevenshteinLevelN(lidttTmp, "",
                requestTitle, req.getCharError() - 1, req, sorter);
//        eraseDuplicate(lidttTmp);
        concatWithoutDuplicates(lidttTmp, tmp);
        return lidttTmp;
    }

    private List<String> getTitlesLightWithLevenshteinLevelN(List<String> lidttTmp,
                                                             String begin, String end, int level, RequestVideo req1, MySorter sorter) {
        int requestTitleLenght = end.length();
        level--;
        for (int i = 0; i < requestTitleLenght; i++) {
            char chartest = end.charAt(i);
            if (chartest == ('%') || chartest == '_') continue;
            String end1 = end.substring(0, i);
            String end1b = end.substring(0, i + 1);
            String end2 = end.substring(i + 1, requestTitleLenght);
            String end2b = end.substring(i + 1, requestTitleLenght);
            //search by deletion of a character
            concatWithoutDuplicates(lidttTmp, searchTitleLightV2(begin,
                    end1 + end2, req1, sorter));
            if (level > 0 && end2.length() > 1) {
                List<String> tTmp = getTitlesLightWithLevenshteinLevelN(
                        lidttTmp,
                        begin + end1,
                        end2,
                        level,
                        req1, sorter);
                concatWithoutDuplicates(lidttTmp, tTmp);
            }
            //search by substitution of a character
            concatWithoutDuplicates(lidttTmp,
                    searchTitleLightV2(begin, end1 + "_" + end2, req1, sorter));
            if (level > 0 && end2.length() > 1) {
                List<String> tTmp = getTitlesLightWithLevenshteinLevelN(
                        lidttTmp,
                        begin + end1 + "_",
                        end2,
                        level,
                        req1, sorter);
                concatWithoutDuplicates(lidttTmp, tTmp);
            }
            //search by adding a character
            concatWithoutDuplicates(searchTitleLightV2(begin,
                    end1b + "_" + end2b, req1, sorter), lidttTmp);
            if (level > 0 && end2b.length() > 1) {
                List<String> tTmp = getTitlesLightWithLevenshteinLevelN(
                        lidttTmp,
                        begin + end1b + "_",
                        end2b,
                        level,
                        req1, sorter);
                concatWithoutDuplicates(lidttTmp, tTmp);
            }
        }
        return lidttTmp;
    }

    private void eraseDuplicate(List<String> ltl) {
        List<String> ltlNew = new ArrayList<>();
        for (String tl1 : ltl) {
            boolean test = false;
            for (String tln : ltlNew) {
                if (tl1.equals(tln)) {
                    test = true;
                    break;
                }
            }
            if (!test) ltlNew.add(tl1);
        }
    }

    private Page<VideoFilm> newPage(RequestVideo req, List<VideoFilm> lvf,
                                    int resultSize, Pageable p) {
        return new PageImpl<VideoFilm>(lvf, p, resultSize);
    }

    private void concatWithoutDuplicates(
            List<String> lvtlFinal, List<String> lvtlTmp) {
        List<String> lvtToAdd = new ArrayList<>();
        for (String tl : lvtlTmp) {
            boolean test = false;
            for (String tlf : lvtlFinal) {
                if (tl.equals(tlf)) {
                    test = true;
                    break;
                }
            }
            if (!test) lvtToAdd.add(tl);
        }
        lvtlFinal.addAll(lvtToAdd);
    }

    private String getRequestTitleWithWildcardOnPonctuation(String requestTitle) {
        return requestTitle.replaceAll("[ ,;.…:/'\"\\[\\]*?&+=`$€—_()!<>-]+", "%");
    }

    private String getRequestTitleWithWildcardIfContainBeginEnd(
            int level, String requestTitle) {

        switch (level) {
            case (1):
                if (requestTitle.length() != 0) {
                    requestTitle = "%" + requestTitle + "%";
                } else {
                    requestTitle = "%";
                }
                break;
            case (2):
                requestTitle = requestTitle + "%";
                break;
            case (3):
                requestTitle = "%" + requestTitle;
                break;
        }
        return requestTitle;
    }

    private void verifySearchLevel(RequestVideo req) {
        if (req.getCharError() < 0) req.setCharError(0);
        if (req.getCharError() > 5) req.setCharError(2);
    }


    int getStates(RequestVideo req, MySorter sorter) {
        int states = req.getReqTitleScoreYear() && !ifFilterTitlescoreyearNotModify(req) ? 1 : 0;
        states = states + (req.getReqTitleScoreYear() && ifFilterTitlescoreyearNotModify(req) ? 2 : 0);
        states = states + (req.getReqTypeEpisodeSeason() ? 4 : 0);
        states = states + (req.getReqImportSupportwidthSize() ? 8 : 0);
        states = states + (req.getReqNameandroles() ? 16 : 0);
        states = states + (req.getReqKeywordKindCountry() ? 32 : 0);
        states = states + (req.getReqDurationWidthLanguage() ? 64 : 0);
        return states;
    }

    /*****************************************************
     ***************** TypeMmi managment *****************
     *****************************************************/


    @Override
    public MyMediaInfo submitNewAudio(NewLanguage newLanguage, String remoteUser) {
        log.info(newLanguage.getIdMmiInEdition(),
                newLanguage.getIdLanguageInEdition(),
                newLanguage.getLanguage());
        MyMediaAudio mmaOld = myMediaAudioRepository
                .findByMyMediaInfo_IdMyMediaInfoAndMyMediaLanguage_IdMyMediaLanguage(
                        newLanguage.getIdMmiInEdition(),
                        newLanguage.getIdLanguageInEdition()
                );
        if (mmaOld == null) return null;
        MyMediaInfo mmi = myMediaInfoRepository
                .findByIdMyMediaInfo(newLanguage.getIdMmiInEdition());
        if (mmi == null) {
            log.info("mmi not found");
            return null;
        }
        MyMediaLanguage mml = getMyMediaLanguage(newLanguage);
        MyMediaAudio mma = new MyMediaAudio(mmi, mml);
        mma.setBitrate(mmaOld.getBitrate());
        mma.setChannels(mmaOld.getChannels());
        mma.setDuration(mmaOld.getDuration());
        mma.setForced(mmaOld.isForced());
        mma.setFormat(mmaOld.getFormat());
        myMediaAudioRepository.save(mma);
        myMediaAudioRepository.deleteOnelink(newLanguage.getIdMmiInEdition(),
                newLanguage.getIdLanguageInEdition());
        return myMediaInfoRepository.findById(newLanguage.getIdMmiInEdition())
                .orElse(null);
    }

    @Override
    public MyMediaInfo submitNewSubtitle(NewLanguage newLanguage, String remoteUser) {
        MyMediaText mmtOld = myMediaTextRepository
                .findByMyMediaInfo_IdMyMediaInfoAndMyMediaLanguage_IdMyMediaLanguage(
                        newLanguage.getIdMmiInEdition(),
                        newLanguage.getIdLanguageInEdition()
                );
        if (mmtOld == null) return null;
        MyMediaInfo mmi = myMediaInfoRepository
                .findByIdMyMediaInfo(newLanguage.getIdMmiInEdition());
        if (mmi == null) return null;
        MyMediaLanguage mml = getMyMediaLanguage(newLanguage);
        MyMediaText mmt = new MyMediaText(mmi, mml);
        mmt.setCodecId(mmtOld.getCodecId());
        mmt.setForced(mmtOld.isForced());
        mmt.setFormat(mmtOld.getFormat());
        mmt.setInternal(mmtOld.isInternal());
        myMediaTextRepository.save(mmt);
        myMediaTextRepository.deleteOnelink(newLanguage.getIdMmiInEdition(),
                newLanguage.getIdLanguageInEdition());
        return myMediaInfoRepository.findById(newLanguage.getIdMmiInEdition())
                .orElse(null);
    }

    @NotNull
    MyMediaLanguage getMyMediaLanguage(NewLanguage newLanguage) {
        MyMediaLanguage mml = myMediaLanguageRepository
                .findByLanguage(newLanguage.getLanguage());
        if (mml == null) {
            String nl = newLanguage.getLanguage().length() > 16 ?
                    newLanguage.getLanguage().substring(0, 15) :
                    newLanguage.getLanguage();
            mml = new MyMediaLanguage(null,
                    nl,null, null);
            mml = myMediaLanguageRepository.save(mml);
        }
        return mml;
    }

    @Override
    public MyMediaInfo submitserienumber(SubmitSerie submitSerie, String remoteUser) {
        TypeMmi tm = typeMmiRepository.findById(submitSerie.getIdTypemmi())
                .orElse(null);
        if (tm == null) return null;
        tm.setNameSerieVO(submitSerie.getNameSerieVo());
        tm.setSeason(submitSerie.getSeason());
        tm.setEpisode(submitSerie.getEpisode());
        typeMmiRepository.save(tm);
        return myMediaInfoRepository.findById(submitSerie.getIdMyMediaInfo())
                .orElse(null);
    }

    @Override
    public List<KeywordAndVFSize> searchKeyWords(String keywordEncoded, int keywordIs, String login) {
        List<Tuple> keywordsTuple;
        List<KeywordAndVFSize> keywords = new ArrayList<>();
        String keyword = new String(Base64.getDecoder().decode(keywordEncoded));
        keyword = getRequestTitleWithWildcardIfContainBeginEnd(keywordIs, keyword);
        keywordsTuple = videoKeywordRepository.findAllByKeywordEn(keyword);
        listTInKeywordVFSize(keywordsTuple, keywords);
        return keywords;
    }

    @Override
    public List<KeywordAndVFSize> getKeywordsWithId(List<String> lidk, String remoteUser) {
        List<Long> lid = lidk.stream().map(Long::parseLong).collect(Collectors.toList());
        List<Tuple> lt = videoKeywordRepository.findAllByIdkeyword(lid);
        List<KeywordAndVFSize> keywords = new ArrayList<>();
        listTInKeywordVFSize(lt, keywords);
        return keywords;
    }

    @Override
    public List<MyMediaLanguageAndNbMmi> searchLanguage(String keywordEncoded, int keywordIs, String login) {
        List<MyMediaLanguageAndNbMmi> lml = new ArrayList<>();
        List<Tuple> keywordsTuple;
        String language = new String(Base64.getDecoder().decode(keywordEncoded));
        language = getRequestTitleWithWildcardIfContainBeginEnd(keywordIs, language);
        List<Tuple> lt = myMediaLanguageRepository.findMyLanguageByLanguage(language);
        listTInLanguageMmisize(lml, lt);
        return lml;
    }

    @Override
    public List<MyMediaLanguageAndNbMmi> getLanguagesWithId(List<String> lidl, String remoteUser) {
        List<MyMediaLanguageAndNbMmi> lml = new ArrayList<>();
        List<Long> lid = lidl.stream().map(Long::parseLong).collect(Collectors.toList());
        List<Tuple> lt = myMediaLanguageRepository.findMyLanguageByIds(lid);
        listTInLanguageMmisize(lml, lt);
        return lml;
    }

    @Override
    public List<MyMediaInfo> saveTitleForCurrentVF(String titleSerieVO,
                                                   String idVF,
                                                   String remoteUser) {
        List<TypeMmi> lTmmi = typeMmiRepository.getAllIdtmmiWithIdVF(idVF);
        String title = titleSerieVO;
        if (titleSerieVO.length() > 32) {
            title = titleSerieVO.substring(0, 31);
        }
        String finalTitle = title;
        lTmmi.forEach(tm -> {
            tm.setNameSerieVO(finalTitle);
            typeMmiRepository.save(tm);
        });
        List<MyMediaInfo> lmmi = myMediaInfoRepository.findAllByTypeMmi(lTmmi);
        return lmmi;
    }

    @Override
    public VideoFilm saveyear(String idVF, Integer year, String remoteUser) {
        if (year > 2100 || year < 1880) return null;
        VideoFilm vf = videoFilmRepository.findById(idVF).orElse(null);
        if (vf == null) return null;
        vf.setYear(year);
        return videoFilmRepository.save(vf);
    }

    @Override
    public VideoFilm saveScore(String idVF, Integer score, String remoteUser) {
        if (score < 0 || score > 99) return null;
        VideoFilm vf = videoFilmRepository.findById(idVF).orElse(null);
        if (vf == null) return null;
        vf.setScoreOnHundred(score);
        return videoFilmRepository.save(vf);
    }

    @Override
    public VideoFilm saveurlImg(String idVF, String url, String remoteUser) {
        if (url.length() < 10 || url.length() > 256) return null;
        VideoFilm vf = videoFilmRepository.findById(idVF).orElse(null);
        if (vf == null) return null;
        VideoPoster vp = null;
        if (vf.getVideoPosters() != null && vf.getVideoPosters().size() != 0) {
            Long idPoster = vf.getVideoPosters().get(0).getIdPoster();
            vp = videoPosterRepository.findById(idPoster).orElse(null);
            vp.setUlrImg(url);
            if (vp != null) {
                vp.setUlrImg(url);
            } else {
                createNewVideoPoster(url, vf);
            }
        } else {
            createNewVideoPoster(url, vf);
        }
        vf.getVideoPosters().add(vp);
        return videoFilmRepository.save(vf);
    }

    @Override
    public VideoFilm newComment(String idVF, String comment, String remoteUser) {
        VideoFilm vf = videoFilmRepository.findById(idVF).orElse(null);
        if (vf == null) return null;
        String newComment = comment.length() < 1024 ? comment : comment.substring(0, 1023);
        VideoComment vc = null;
        if (vf.getVideoComment() == null) {
            vc = new VideoComment(null, newComment, vf);
        } else {
            vc = vf.getVideoComment();
            vc.setComment(newComment);
        }
        videoCommentRepository.save(vc);
        vf.setDateModifFilm(new Date());
        return videoFilmRepository.save(vf);
    }

    @Override
    public VideoFilm newTitle(String idVF, String title, String remoteUser) {
        VideoFilm vf = videoFilmRepository.findById(idVF).orElse(null);
        if (vf == null) return null;
        String newTitle = title.length() <= 128 ? title : title.substring(0, 127);

        int nb = 0;
        boolean test = true;
        while (test) {
            String c = remoteUser + "-" + nb;
            VideoCountry vc = videoCountryRepository.findByCountry(c).orElse(null);
            if (vc == null) {
                vc = videoCountryRepository.save(new VideoCountry(null, c, remoteUser
                        , null, null));
                VideoTitle vt = videoTitleRepository.findByVideoCountryAndVideoFilm(vc, vf).orElse(null);
                if (vt == null) {
                    // We found one videoCountry who doesn't exist and videotile doesn't exist too
                    // -> we can create new and finish
                    test = false;
                    vt = new VideoTitle(vc, vf);
                    vt.setTitle(newTitle);
                    videoTitleRepository.save(vt);
                } else { // This videoTitle exist. Next with : login+number
                    nb++;
                }
            } else {
                VideoTitle vt = videoTitleRepository.findByVideoCountryAndVideoFilm(vc, vf).orElse(null);
                if (vt == null) {
                    // We found one videoCountry who exist But videotile doesn't exist
                    // -> we can create new and finish
                    test = false;
                    vt = new VideoTitle(vc, vf);
                    vt.setTitle(newTitle);
                    videoTitleRepository.save(vt);
                } else { // This videoTitle exist. Next with : login+number
                    nb++;
                }
            }
        }
        vf.setDateModifFilm(new Date());
        return videoFilmRepository.save(vf);
    }

    /*************************************************************************************/
    @Override
    public VideoFilm newKeyword(String idVF, String keyword, String remoteUser) {
        VideoFilm vf = videoFilmRepository.findById(idVF).orElse(null);
        if (vf == null) return null;
        String newKeyword = keyword.length() <= 64 ? keyword : keyword.substring(0, 63);
        List<VideoFilm> lvf = new ArrayList<>();
        lvf.add(vf);

        VideoKeyword vk = videoKeywordRepository.findByKeywordEnAndVideoFilms(newKeyword, lvf).orElse(null);
        if (vk != null) return null;
        vk = videoKeywordRepository.findByKeywordEn(keyword).orElse(null);
        if (vk == null) {
            int nb = 0;
            boolean test = true;
            while (test) {
                VideoKeyword v = videoKeywordRepository.findByKeywordFr(remoteUser + "-" + nb).orElse(null);
                if (v == null) test = false;
                nb++;
            }
            vk = new VideoKeyword(null, newKeyword, remoteUser + "-" + nb, null);
            videoKeywordRepository.save(vk);

        }

        vf.getVideoKeywordSet().add(vk);
        vf.setDateModifFilm(new Date());
        return videoFilmRepository.save(vf);
    }

    /*************************************************************************************/

    private VideoPoster createNewVideoPoster(String url, VideoFilm vf) {
        return videoPosterRepository
                .save(new VideoPoster(null, "", "", url, vf));
    }

    void listTInLanguageMmisize(List<MyMediaLanguageAndNbMmi> lml, List<Tuple> lt) {
        if (lt.size() != 0) {
            for (Tuple t : lt) {
                Long l = (Long) t.toArray()[2];
                if (l != 0) lml.add(new MyMediaLanguageAndNbMmi(
                        (Long) t.toArray()[0], (String) t.toArray()[1], l));
            }
        }
    }

    void listTInKeywordVFSize(List<Tuple> lt, List<KeywordAndVFSize> keywords) {
        if (lt.size() != 0) {
            for (Tuple t : lt) {
                keywords.add(
                        new KeywordAndVFSize(
                                (String) t.toArray()[0],
                                (Long) t.toArray()[1],
                                (int) t.toArray()[2]
                        )
                );
            }
        }
    }


}
