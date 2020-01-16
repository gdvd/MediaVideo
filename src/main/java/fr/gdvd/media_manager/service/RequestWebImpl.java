package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.daoMysql.*;
import fr.gdvd.media_manager.entitiesMysql.*;
import fr.gdvd.media_manager.entitiesNoDb.*;
import fr.gdvd.media_manager.tools.Download;
import fr.gdvd.media_manager.tools.Parser;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Service
public class RequestWebImpl implements RequestWeb {

    @Autowired
    private Download download;
    @Autowired
    private Parser parser;
    @Autowired
    private VideoFilmRepository videoFilmRepository;
    @Autowired
    private TypeNameRepository typeNameRepository;
    @Autowired
    private MyMediaInfoRepository myMediaInfoRepository;
    @Autowired
    private TypeMmiRepository typeMmiRepository;
    @Autowired
    private MyMediaAudioRepository myMediaAudioRepository;
    @Autowired
    private MyMediaTextRepository myMediaTextRepository;
    @Autowired
    private MyMediaLanguageRepository myMediaLanguageRepository;
    @Autowired
    private VideoSupportPathRepository videoSupportPathRepository;
    @Autowired
    private PreferencesRepository preferencesRepository;
    @Autowired
    private BasketRepository basketRepository;
    @Autowired
    private BasketNameRepository basketNameRepository;
    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private VideoTitleRepository videoTitleRepository;
    @Autowired
    private MyMediaCommentRepository myMediaCommentRepository;
    @Autowired
    private VideoCommentRepository videoCommentRepository;
    @Autowired
    private RemakeRepository remakeRepository;

    @Override
    public VideoFilm linkIdttWithIdmmi(String idmmi, String idtt) {
        MyMediaInfo mmi = myMediaInfoRepository.findById(idmmi).orElse(null);
        if (mmi == null) throw new RuntimeException("This MyMediaInfo doesn't exist.");
        VideoFilm vf = videoFilmRepository.findById(idtt).orElse(null);
        if (vf == null) throw new RuntimeException("This VideoFilm doesn't exist.");
        TypeMmi tmmi = mmi.getTypeMmi();
        List<TypeMmi> tmmiVf = typeMmiRepository.findByVideoFilm(vf);

        // if there's one or more typemmi with season > 0
        boolean makemanytypemmi = false;
        for (TypeMmi typeMmi : tmmiVf) {
            if (typeMmi.getSeason() > 0) {
                makemanytypemmi = true;
                break;
            }
        }

        if (tmmi == null) {  // No link in mmi
            mmi.setDateModif(new Date());

            if (tmmiVf != null && tmmiVf.size() != 0) {

                if (makemanytypemmi) {
                    tmmi = initTmmiWithTn(tmmiVf.get(0).getTypeName().getTypeName(), vf);
                    tmmi.setActive(true);
                    String title = vf.getVideoTitles().get(0).getTitle();
                    if (title.length() > 32) title = title.substring(0, 31);
                    tmmi.setNameSerieVO(title);
                    parseTitleToGetNumberOfSerie(tmmi, mmi.getIdMyMediaInfo());
                } else {
                    tmmi = tmmiVf.get(0);
                    if (tmmiVf.get(0).getTypeName().getTypeName().toLowerCase().equals("serie")) {
                        String title = vf.getVideoTitles().get(0).getTitle();
                        if (title.length() > 32) title = title.substring(0, 31);
                        tmmi.setNameSerieVO(title);
                        parseTitleToGetNumberOfSerie(tmmi, mmi.getIdMyMediaInfo());
                    }
                    tmmi.setDateModif(new Date());
                    tmmi.setActive(true);
                }

                tmmi = typeMmiRepository.save(tmmi);
                mmi.setTypeMmi(tmmi);
                myMediaInfoRepository.save(mmi);

            } else {

                tmmi = initTmmiWithTn("autre", vf);
                tmmi.setActive(true);
                tmmi = typeMmiRepository.save(tmmi);
                mmi.setTypeMmi(tmmi);
                myMediaInfoRepository.save(mmi);
            }

        } else { // tmmi is set

//            if (tmmiVf == null || tmmiVf.size() == 0) {
            tmmi.setVideoFilm(vf);
            tmmi.setDateModif(new Date());
            tmmi.setActive(true);
            typeMmiRepository.save(tmmi);
            /*} else {
                if (tmmiVf.get(0).getTypeName().getTypeName().toLowerCase().equals("serie")) {
                    parseTitleToGetNumberOfSerie(tmmi, mmi.getIdMyMediaInfo());
                }
                mmi.setTypeMmi(tmmi);
                mmi.setDateModif(new Date());
                myMediaInfoRepository.save(mmi);
            }*/
        }
        return vf;
    }

    private TypeMmi initTmmiWithTn(String typename, VideoFilm vf) {
        TypeName tn = typeNameRepository.findByTypeName(typename).orElse(null);
        if (tn == null) tn = new TypeName(null, typename, null);
        return new TypeMmi(null, 0, 0, "", ""
                , false, new Date(), tn, vf, null);
    }

    private TypeMmi parseTitleToGetNumberOfSerie(TypeMmi tmmi, String idMyMediaInfo) {
        List<String> titles = videoSupportPathRepository
                .getTitleswhereidmd5(idMyMediaInfo);
        for (String str : titles) {
            Pattern p = Pattern.compile("[\\s\\.]{1}([Ss]{1}[0-9]{1,2}[Ee]{1}[0-9]{1,2})[\\s\\.]{1}");
            Matcher m = p.matcher(str);
            if (m.find()) {
                String seasonepisode = m.group();
                seasonepisode = seasonepisode.substring(1).trim();
                String res[] = seasonepisode.split("[Ee]{1}");
                return writeValueSeason(tmmi, res);
            } else {
                p = Pattern.compile("[\\s\\.]{1}([0-9]{1,2}[Xx*]{1}[0-9]{1,2})[\\s\\.]{1}");
                m = p.matcher(str);
                if (m.find()) {
                    String seasonepisode = m.group();
                    seasonepisode = seasonepisode.substring(1).trim();
                    String res[] = seasonepisode.split("[Xx*]{1}");
                    return writeValueSeason(tmmi, res);
                } else {
                    p = Pattern.compile("[\\s\\[]{1}([0-9]{1,2}[Xx*]{1}[0-9]{1,2})[\\]\\s]{1}");
                    m = p.matcher(str);
                    if (m.find()) {
                        String seasonepisode = m.group();
                        seasonepisode = seasonepisode.substring(1).trim();
                        seasonepisode = seasonepisode.replace("]", "");
                        String res[] = seasonepisode.split("[Xx*]{1}");
                        return writeValueSeason(tmmi, res);
                    } else {
                        p = Pattern.compile("[\\s\\.]{1}([Ss]{1}[0-9]{1,2}[Ee]{1}[Pp]{1}[0-9]{1,2})[\\s\\.]{1}");
                        m = p.matcher(str);
                        if (m.find()) {
                            String seasonepisode = m.group();
                            seasonepisode = seasonepisode.substring(1).trim();
                            String res[] = seasonepisode.split("[Ee]{1}[Pp]{1}");
                            return writeValueSeason(tmmi, res);
                        }
                    }
                }
            }

        }
        return tmmi;
    }

    private TypeMmi writeValueSeason(TypeMmi tmmi, String[] res) {
        String s = res[0].replaceAll("\\s", "");
        s = s.replaceAll("s", "");
        s = s.replaceAll("S", "");
        s = s.replace(".", "");
        s = s.replace("[", "");
        s = s.replace("]", "");
        tmmi.setSeason(Integer.parseInt(s));
        String e = res[1].replaceAll("\\s", "");
        e = e.replace(".", "");
        e = e.replace("[", "");
        e = e.replace("]", "");
        tmmi.setEpisode(Integer.parseInt(e));
        return tmmi;
    }

/*    private void initTypeMmi(MyMediaInfo mmi, TypeMmi tmmi, VideoFilm vf) {
        TypeName tn = typeNameRepository.findByTypeName("Autre").orElse(null);
        if (tn == null) {//Add typename 'Autre'
            tn = new TypeName(null, "Autre", null);
            tn = typeNameRepository.save(tn);
        }
        tmmi = new TypeMmi(null, 0, 0,
                "", "", true,
                new Date(),
                tn, vf, null);
        tmmi = typeMmiRepository.save(tmmi);
        mmi.setTypeMmi(tmmi);
        myMediaInfoRepository.save(mmi);
    }*/

    @Override
    public TypeMmi savetypemmi(TypeMmi typeMmi, String idMmi, String idVideo) {
        TypeName tn = typeNameRepository
                .findById(typeMmi.getTypeName()
                        .getIdTypeName())
                .orElse(null);
        if (tn == null) {
            tn = typeNameRepository
                    .findByTypeName(typeMmi.getTypeName().getTypeName())
                    .orElse(null);
            if (tn == null) {
                tn = typeNameRepository.findByTypeName("Autre").orElse(null);
                if (tn == null) {
                    tn = new TypeName(null, "Autre", null);
                    tn = typeNameRepository.save(tn);
                }
            }
        }
        typeMmi.setDateModif(new Date());
        typeMmi.setActive(true);
        typeMmi.setTypeName(tn);
        if (!idVideo.equals("")) {
            VideoFilm vf = videoFilmRepository.findById(idVideo).orElse(null);
            if (vf != null) typeMmi.setVideoFilm(vf);
        }
        typeMmi = typeMmiRepository.save(typeMmi);
        MyMediaInfo mmi = myMediaInfoRepository.findByIdMyMediaInfo(idMmi);
        if (mmi != null) {
            mmi.setTypeMmi(typeMmi);
            mmi.setDateModif(new Date());
            mmi = myMediaInfoRepository.save(mmi);
        } else {
            throw new RuntimeException("This MMI doesn't exist");
        }
        return typeMmi != null && mmi != null ? typeMmi : null;
    }

    @Override
    public TypeMmi gettypemmiwithidtt(String idtt) {
        VideoFilm vf = videoFilmRepository.findById(idtt).orElse(null);
        if (vf == null) throw new RuntimeException("VideoFilm doesn't exist");
        return typeMmiRepository.findByVideoFilm(vf).get(0);
    }

    @Override
    public MyMediaAudio updatelanguage(Langtopost langtopost) {
        MyMediaLanguage mmlold = myMediaLanguageRepository.findByLanguage(langtopost.getOldLang());
        MyMediaInfo mmi = myMediaInfoRepository.findById(langtopost.getIdMd5()).orElse(null);
        MyMediaLanguage mmlnew = prepareChangeLanguage(mmlold, mmi, langtopost);
        // create new mediaAudio
        MyMediaAudio mmaOld = myMediaAudioRepository.findByMyMediaInfoAndMyMediaLanguage(mmi, mmlold);
        MyMediaAudio mmaNew = new MyMediaAudio(mmi, mmlnew);
        mmaNew.setFormat(mmaOld.getFormat());
        mmaNew.setForced(mmaOld.isForced());
        mmaNew.setDuration(mmaOld.getDuration());
        mmaNew.setChannels(mmaOld.getChannels());
        mmaNew.setBitrate(mmaOld.getBitrate());
        myMediaAudioRepository.deleteOnelink(mmi.getIdMyMediaInfo(), mmlold.getIdMyMediaLanguage());
        mmaNew = myMediaAudioRepository.save(mmaNew);
        mmi.setDateModif(new Date());
        myMediaInfoRepository.save(mmi);
        return mmaNew;
    }

    @Override
    public MyMediaText updatetext(Langtopost langtopost) {
        MyMediaLanguage mmlold = myMediaLanguageRepository.findByLanguage(langtopost.getOldLang());
        MyMediaInfo mmi = myMediaInfoRepository.findById(langtopost.getIdMd5()).orElse(null);
        MyMediaLanguage mmlnew = prepareChangeLanguage(mmlold, mmi, langtopost);
        // create new mediaText
        MyMediaText mmtOld = myMediaTextRepository.findByMyMediaInfoAndMyMediaLanguage(mmi, mmlold);
        MyMediaText mmtNew = new MyMediaText(mmi, mmlnew);
        mmtNew.setCodecId(mmtOld.getCodecId());
        mmtNew.setInternal(mmtOld.isInternal());
        mmtNew.setForced(mmtOld.isForced());
        mmtNew.setFormat(mmtOld.getFormat());
//        myMediaTextRepository.deleteByMyMediaInfoAndMyMediaLanguage(mmi, mmlold);
        myMediaTextRepository.deleteOnelink(mmi.getIdMyMediaInfo(), mmlold.getIdMyMediaLanguage());
        mmtNew = myMediaTextRepository.save(mmtNew);
        mmi.setDateModif(new Date());
        myMediaInfoRepository.save(mmi);
        return mmtNew;
    }

    @Override
    public TypeMmi gettypemmiwithidmmi(String idtmmi) {
        Long idTmmi = myMediaInfoRepository.findFkTypeMmiWithIdmmi(idtmmi);
        if (idTmmi > 0) {
            return typeMmiRepository.findById(idTmmi).orElse(null);
        }
        return null;
    }

    @Override
    public MyMediaInfo eraseLinkTmmiVideofilm(Long idTmmi, String idVideo, String idMmi) {
        MyMediaInfo mmi = myMediaInfoRepository.findById(idMmi).orElse(null);
        if (mmi == null) throw new RuntimeException("MyMediaInfo doesn't exist");
        TypeMmi tmmi = typeMmiRepository.findById(idTmmi).orElse(null);
        if (tmmi == null) throw new RuntimeException("TypeMedia doesn't exist");
        VideoFilm vf = videoFilmRepository.findById(idVideo).orElse(null);
        if (vf == null) throw new RuntimeException("VideoFilm doesn't exist");
        tmmi.setVideoFilm(null);
        tmmi.setDateModif(new Date());
        typeMmiRepository.save(tmmi);
        return mmi;
    }

    @Override
    public MyMediaInfo eraseLinkMmiTmmi(Long idTmmi, String idMmi) {
        MyMediaInfo mmi = myMediaInfoRepository.findById(idMmi).orElse(null);
        if (mmi == null) throw new RuntimeException("MyMediaInfo doesn't exist");
        TypeMmi tmmi = typeMmiRepository.findById(idTmmi).orElse(null);
        if (tmmi == null) throw new RuntimeException("TypeMedia doesn't exist");
        mmi.setTypeMmi(null);
        mmi.setDateModif(new Date());
        return myMediaInfoRepository.save(mmi);
    }

    @Override
    public MyMediaInfo eraseTmmi(Long idTmmi, String idVideo, String idMmi) {
        MyMediaInfo mmi = myMediaInfoRepository.findById(idMmi).orElse(null);
        if (mmi == null) throw new RuntimeException("MyMediaInfo doesn't exist");
        TypeMmi tmmi = typeMmiRepository.findById(idTmmi).orElse(null);
        if (tmmi == null) throw new RuntimeException("TypeMedia doesn't exist");
        if (!idVideo.equals("0")) {
            VideoFilm vf = videoFilmRepository.findById(idVideo).orElse(null);
            if (vf == null) throw new RuntimeException("VideoFilm doesn't exist");
            tmmi.setVideoFilm(null);
        }
        tmmi.setDateModif(new Date());
        tmmi.setActive(false);
        typeMmiRepository.save(tmmi);

        mmi.setTypeMmi(null);
        mmi.setDateModif(new Date());
        return myMediaInfoRepository.save(mmi);
    }


    private MyMediaLanguage prepareChangeLanguage(MyMediaLanguage mmlold, MyMediaInfo mmi, Langtopost langtopost) {
        // If old language exist
        if (mmlold == null) throw new RuntimeException("The language doesn't exist");
        if (mmi == null) throw new RuntimeException("IdMd5 doesn't exist");

        MyMediaLanguage mmlnew = myMediaLanguageRepository
                .findByLanguage(langtopost.getNewLlang());
        if (mmlnew == null) {
            String lg = langtopost.getNewLlang();
            if (lg.length() > 16) lg = lg.substring(0, 15);
            mmlnew = myMediaLanguageRepository.save(
                    new MyMediaLanguage(null, lg, null, null));
        }
        return mmlnew;
    }

    @Override
    public List<RequestImdb> getResultRequestWeb(String query1, Long idTmmi) {
        List<RequestImdb> lri = new ArrayList<>();
        String completeUrl = "";
        String query2 = "";
        List<String> lquery = new ArrayList<>();
        if (Pattern.matches("tt[\\d]{6,9}", query1)) {
            RequestImdb ri = new RequestImdb();
            ri.setLink("/title/" + query1);
            ri.setName(query1);
            ri.setVideo(true);
            lri.add(ri);

        } else {
            query1 = query1.replaceAll("[^\\w\\s\\é\\'\\è\\ç\\à\\ë\\ê\\û\\ù\\ô\\ï\\î]+", " ");
            query1 = query1.replace("^[\\s]*", "").trim();
            query1 = query1.replaceAll("[\\s]+", " ");

            lquery.add(query1);

            query2 = query1.replaceAll("\\sl\\s", " l'");
            query2 = query2.replaceAll("\\sL\\s", " l'");
            query2 = query2.replaceAll("^l\\s", " l'");
            query2 = query2.replaceAll("^L\\s", " l'");

            query2 = query1.replaceAll("\\sj\\s", " j'");
            query2 = query2.replaceAll("\\sJ\\s", " j'");
            query2 = query2.replaceAll("^j\\s", " j'");
            query2 = query2.replaceAll("^J\\s", " j'");

            query2 = query1.replaceAll("\\sm\\s", " m'");
            query2 = query2.replaceAll("\\sM\\s", " m'");
            query2 = query2.replaceAll("^m\\s", " m'");
            query2 = query2.replaceAll("^M\\s", " m'");

            query2 = query1.replaceAll("\\ss\\s", " s'");
            query2 = query2.replaceAll("\\sS\\s", " s'");
            query2 = query2.replaceAll("^s\\s", " s'");
            query2 = query2.replaceAll("^S\\s", " s'");

            query2 = query2.replaceAll("\\sc\\s", " c'");
            query2 = query2.replaceAll("\\sC\\s", " C'");
            query2 = query2.replaceAll("^c\\s", " c'");
            query2 = query2.replaceAll("^C\\s", " C'");

            query2 = query2.replaceAll("\\sd\\s", " d'");
            query2 = query2.replaceAll("\\sD\\s", " D'");
            query2 = query2.replaceAll("^d\\s", " d'");
            query2 = query2.replaceAll("^D\\s", " D'");

            query2 = query2.replaceAll("\\sn\\s", " n'");
            query2 = query2.replaceAll("\\sN\\s", " N'");
            query2 = query2.replaceAll("^n\\s", " n'");
            query2 = query2.replaceAll("^N\\s", " N'");

            query2 = query2.replaceAll("\\st\\s", " t'");
            query2 = query2.replaceAll("\\sT\\s", " T'");
            query2 = query2.replaceAll("^t\\s", " t'");
            query2 = query2.replaceAll("^T\\s", " T'");

            if (!query1.equals(query2)) {
                lquery.add(query2);
            }

            for (String query : lquery) {
                String encodedQuery = encodeValue(query);
                completeUrl = "https://www.imdb.com/find?ref_=nv_sr_fn&q=" + encodedQuery + "&s=all";

                URL url = null;
                File f = null;
                String res = "";
                try {
                    url = new URL(completeUrl);
                    f = new File(System.getProperty("user.home") + "/pathIdVideo/search/searchtitles.xml");
                    res = download.download2String(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (res.length() > 1000) {
                    List<String> lr = parser.findAllTagsInString(
                            "<div class=\"findSection\">",
                            "</div>", res, true);
//            res = "";
                    String fd = "";
                    for (String str : lr) {
                        fd = parser.findTagInString("<a name=\"tt\">", "</a>", str, true);
                        if (fd.length() > 0) {
                            List<String> lres = parser.findAllTagsInString("<tr class=\"findResult*>",
                                    "</tr>", str, true);
                            //For each result
                            for (String resName : lres) {
                                RequestImdb ri = new RequestImdb();
                                ri.setUrlImg(parser.findTagInString("<img src=\"", "\"", resName, false));
                                String titlePackage = parser.findTagInString("<td class=\"result_text\">",
                                        "</tr>", resName, false);// 'end of tr' to keep one last 'tag s end'
                                ri.setLink(parser.findTagInString("<a href=\"", "\"", titlePackage, false));
                                ri.setName(parser.findTagInString("<a href=*>", "</a>", titlePackage, false));
                                String infoandmore = parser.findTagInString("</a>", "</td>", titlePackage, false);
                                String part2 = "";
                                String part3 = "";
                                String part4 = "";
                                if (infoandmore.split("\\(").length > 1) {
                                    part2 = infoandmore.split("\\(")[1];
                                    part2 = part2.split("\\)")[0];
                                }
                                if (infoandmore.split("\\(").length > 2) {
                                    part3 = infoandmore.split("\\(")[2];
                                    part3 = part3.split("\\)")[0];
                                    part3 = ") (" + part3;
                                }
                                if (infoandmore.split("\\(").length > 3) {
                                    part4 = infoandmore.split("\\(")[3];
                                    part4 = part4.split("\\)")[0];
                                    part4 = ") (" + part4;
                                }

                                ri.setInfo("(" + part2 + part3 + part4 + ")");
                                ri.setVideo(true);

                                String linktt = parser.findTagInString("<a href=\"", "\"", resName, false);
                                TypeMmi tmmi = typeMmiRepository.findById(idTmmi).orElse(null);
                                boolean bool = false;
                                if (tmmi != null) {
                                    if (tmmi.getVideoFilm() != null) {
                                        if (tmmi.getVideoFilm().getIdVideo().equals(linkToIdTt(linktt))) {
                                            bool = true;
                                        }
                                    }
                                }
//                        boolean bool = videoFilmRepository.ifTtExist(linkToIdTt(linktt));
                                ri.setState(bool);
                                boolean test = true;
                                if (lri.size() > 0) for (RequestImdb r : lri) {
                                    if (r.getLink().split("\\?")[0].equals(ri.getLink().split("\\?")[0])) test = false;
                                }
                                if (test) lri.add(ri);
//                                lri.add(ri);
                            }
                            //We keep the 'More title' & 'Exact title'
                            String moreinfo = parser.findTagInString("<div class=\"findMoreMatches*>", "</div>", str, false);
                            List<String> lstr = parser.findAllTagsInString("<a href=", "</a>", moreinfo, true);

                            for (String s : lstr) {
                                RequestImdb ri = new RequestImdb();
                                ri.setLink(parser.findTagInString("<a href=\"", "\"", s, false));
                                ri.setName(parser.findTagInString("<a href=*>", "</a>", s, false));
                                ri.setVideo(false);
                                lri.add(ri);
                            }

                        }
                    }
                }
            }
        }
        return lri;
    }

    @Override
    public List<String> getAllTypeName() {
        return typeNameRepository.findAllTypeName();
    }

    @Override
    public List<TypeName> getAllTypeNameWithId() {
        return typeNameRepository.findAll();
    }


    @Override
    public VideoFilm getOneVideoFilm(String link, String idMyMediaInfo) {
        String idtt = linkToIdTt(link);
        if (idtt.equals("")) throw new RuntimeException("link incorrect");

        // search in db
        VideoFilm vf = videoFilmRepository.findById(idtt).orElse(null);
        if (vf != null) {
            return vf;
        }

        // search in file system or internet
        File f = downloaWebPage(idtt + "/reference", "/idtt/" + idtt);
        if (f == null) {
            String msg = "===> File : " + idtt + " cannot be download";
            log.warn(msg);
            throw new RuntimeException(msg);
        }

        // parse data and push to db
        String toParse = fileToString(f);
        vf = new VideoFilm();
        vf.setIdVideo(idtt);
        vf = parser.createOneVideoFilm(vf, toParse, idMyMediaInfo);
//        log.warn("File : " + idtt + " is added to the DB");

        // Add titles
        File f2 = downloaWebPage(idtt + "/releaseinfo", "/titles/"
                + idtt + "-releaseinfo");
        if (f2 == null) {
            log.error("File : " + idtt + "/releaseinfo is empty");
        } else {
            String toParse4title = fileToString(f2);
            Preferences pref = preferencesRepository.findByIdPreferences("c2title");
            if (pref != null) {
                List<String> ltitles2search = new ArrayList<>(pref.getExtset());
                parser.addTitlesToVideoFilm(vf, toParse4title, ltitles2search);
//                log.warn("Titles : " + idtt + " is added to the DB");
            }
        }

        //Add Keywords
        /*File f3 = downloaWebPage(idtt + "/keywords", "/keywords/"
                + idtt + "-keywords");
        if (f3 == null) {
            log.error("File : " + idtt + "/keywords is empty");
        } else {
            String toParse4keywords = fileToString(f3);
            Preferences pref = preferencesRepository.findByIdPreferences("01");
            if (pref != null) {
                Map<String, String> mp = pref.getPrefmap();
                String lim = mp.get("limitekeywods");

                if ( lim.length()>0 && Pattern.matches("[\\d]{1,6}", lim)) {
                    int limite = Integer.parseInt(lim);
                    parser.addKeywordsToVideoFilm(vf, toParse4keywords, limite);
                    log.warn("Keywords : " + idtt + " is added to the DB");
                }

            }

        }*/

        return vf;
    }


    @Override
    public File downloaWebPage(String idttcomplete, String filedestination) {
        URL url = null;
        File f = null;
        int response = 0;
        try {
            url = new URL("https://www.imdb.com/title/" + idttcomplete);
            f = new File(System.getProperty("user.home") + "/pathIdVideo" + filedestination + ".html");
            if (f.exists() && f.length() > 1000) return f;
            response = download.download2File(url, f);
            if (!(response >= 200 && response < 206 && f.length() > 1000)) throw new RuntimeException("Internet error");
        } catch (IOException e) {
            e.printStackTrace();
            return f;
        }
        return f;
    }

    private String linkToIdTtOLD(String link) {
        // format link
        String[] path = link.split("/");
        String idtt = "";
        for (String str : path) {
            if (str.length() > 0) {
                if (Pattern.matches("tt[\\d]{7,10}", str)) {
                    idtt = str;
                    break;
                }
            }
        }
        return idtt;
    }

    @Override
    public String linkToIdTt(String url) {
        String id = "";
        for (String s : url.split("/")) {
            if (Pattern.matches("tt[\\d]{7,10}", s)) {// 'tt' and 7 digits maybe 8 or 9
                id = s;
                break;
            }
        }
        return id;
    }

    @Override
    public String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    @Override
    public String fileToString(File f) {
        {
            byte[] encoded = new byte[0];
            try {
                encoded = Files.readAllBytes(f.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new String(encoded, StandardCharsets.UTF_8);
        }
    }

    @Override
    public List<Basket> getbaskets(String login) {
        return basketRepository.findAllByMyUser_Login(login);
    }

    @Override
    public List<Basket> addtobasket(String idMmi, String login, String nameBasket) {
        MyMediaInfo mmi = myMediaInfoRepository.findById(idMmi).orElse(null);
        if (mmi == null) throw new RuntimeException("idMmi is wrong");
        BasketName bn = basketNameRepository.findByBasketName(nameBasket).orElse(null);
        if (bn == null) bn = basketNameRepository.save(new BasketName(null, nameBasket, null, null));
        MyUser mu = myUserRepository.findByLogin(login);
        Basket b = new Basket(mmi, mu, bn);
        b.setDateModif(new Date());
        b = basketRepository.save(b);
        b.setBasketName(bn);
        b.setId_basket_name(bn.getIdBasketName());
        List<Basket> lb = basketRepository.findAllByMyUser_LoginAndBasketName_BasketName(login, nameBasket);
        return lb;
    }

    @Override
    public BasketInfo getfilenameofidsbasket(BasketInfo bi) {
        List<Basket> lIdMmi = basketRepository.findAllByMyUser_IdMyUserAndBasketName_BasketName(bi.getUserId(), bi.getBasketName());
        List<BasketInfoElement> lbie = new ArrayList<>();
        for (Basket basket : lIdMmi) {
            // Foreach idmmi -> get path/title -> BasketInfoElement & List<nameExport> -> BasketInfoPahs -> BasketInfoElement -> BasketInfo
            List<Tuple> lt = videoSupportPathRepository.findTitlePathAndVneWithidMmi(basket.getMyMediaInfo().getIdMyMediaInfo());
            List<BasketInfoPahs> lbip = new ArrayList<>();
            BasketInfoElement bie = new BasketInfoElement(basket.getMyMediaInfo().getIdMyMediaInfo(), lbip);
            for (Tuple t : lt) {
                BasketInfoPahs bip = new BasketInfoPahs((String) t.toArray()[0], (String) t.toArray()[1], (String) t.toArray()[2]);
                lbip.add(bip);
            }
            lbie.add(bie);
        }
        bi.getBasketInfoElements().addAll(lbie);
        return bi;
    }

    @Override
    public void deletelocalbasketname(String nameBasket, Long idUser) {
        BasketName bn = basketNameRepository.findByBasketName(nameBasket).orElse(null);
        if (bn == null)
            throw new RuntimeException("This Basket cannot be delete, because this nameBasket doesn't exist");
        basketRepository.deleteAllByBasketNameAndMyUser_IdMyUser(bn, idUser);
    }

    @Override
    public void deleteOneId(String nameBasket, Long idUser, String idMmi) {
        BasketName bn = basketNameRepository.findByBasketName(nameBasket).orElse(null);
        if (bn == null)
            throw new RuntimeException("This Basket cannot be delete, because this nameBasket doesn't exist");
        Basket b = basketRepository.findByBasketNameAndMyUser_IdMyUserAndMyMediaInfo_IdMyMediaInfo(bn, idUser, idMmi).orElse(null);
        if (b == null) throw new RuntimeException("This Basket cannot be delete, because this Basket doesn't exist");
        basketRepository.deleteByBasketNameAndMyUser_IdMyUserAndMyMediaInfo_IdMyMediaInfo(bn, idUser, idMmi);
    }

    @Override
    public List<OneSimpleScore> getLastScore() {
        int nb = 5;
        List<OneSimpleScore> loss = new ArrayList<>();

        Pageable findScore = PageRequest.of(0, nb);
        Page<VideoFilm> lt = videoFilmRepository.getLastScore(findScore);

        /*if(lt.getContent().size()!=0){
            for(VideoFilm vf: lt.getContent()){
                List<VideoTitle> lvt = videoTitleRepository.findFirstByIdVideo(vf);
                String oneTitle = lvt.get(0).getTitle();
                OneSimpleScore oss = new OneSimpleScore(vf.getIdVideo(), oneTitle, vf.getScoreOnHundred(),
                        vf.getNbOfVote(),0, vf.getDateModifFilm(), "", "videofilm");
                loss.add(oss);
            }
        }*/
        return loss;
    }

    @Override
    public MyMediaInfo postcommentforuser(String idMmi, @NotNull String comment) {
        MyMediaInfo mmi = myMediaInfoRepository.findByIdMyMediaInfo(idMmi);
        if (comment.length() > 1024) comment = comment.substring(0, 1023);
        MyMediaComment mmc = new MyMediaComment(null, comment, mmi);
        myMediaCommentRepository.save(mmc);
        return myMediaInfoRepository.findByIdMyMediaInfo(idMmi);
    }

    @Override
    public VideoFilm postcommentvideo(String idVideo, @NotNull String comment) {
        if (comment.length() > 1024) comment = comment.substring(0, 1023);
        VideoFilm vf = videoFilmRepository.findById(idVideo).orElse(null);
        if (vf == null) throw new RuntimeException("VideoFilm doesn't exist");
        VideoComment vc = videoCommentRepository.findByVideoFilm(vf);
        if (vc == null) {
            vc = new VideoComment(null, comment, vf);
        } else {
            vc.setComment(comment);
        }
        vc = videoCommentRepository.save(vc);
        vf.setVideoComment(vc);
        return vf;
    }

    @Override
    public Remake setremake(@NotNull String idvf, @NotNull String idremake) {
        Remake rm = null;
        if (Pattern.matches("tt[\\d]{7,10}", idvf)
                && Pattern.matches("tt[\\d]{7,10}", idremake)
                && !(idvf.equals(idremake))) {
            VideoFilm vf1 = videoFilmRepository.findById(idvf).orElse(null);
            if (vf1 == null) throw new RuntimeException("Wrong IdVideoFilm1");
            VideoFilm vf2 = videoFilmRepository.findById(idremake).orElse(null);
            if (vf2 == null) throw new RuntimeException("Wrong IdVideoFilm2");
            if (vf1.getRemake() == null) {
                if (vf2.getRemake() == null) {
                    rm = new Remake(null,
                            Stream.of(idvf, idremake).collect(Collectors.toSet()),
                            null);
                    rm = remakeRepository.save(rm);
                    vf1.setRemake(rm);
                    vf1.setDateModifFilm(new Date());
                    videoFilmRepository.save(vf1);
                    vf2.setRemake(rm);
                    vf2.setDateModifFilm(new Date());
                    videoFilmRepository.save(vf2);
                } else {
                    rm = remakeRepository.findById(vf2.getRemake().getIdRemake()).orElse(null);
                    if (rm==null) throw new RuntimeException("Remarke(remote) is null");
                    rm.getRemakes().add(vf1.getIdVideo());
                    rm = remakeRepository.save(rm);
                    vf1.setRemake(rm);
                    vf1.setDateModifFilm(new Date());
                    videoFilmRepository.save(vf1);
                }
            } else {
                if (vf2.getRemake() == null) {
                    rm = remakeRepository.findById(vf1.getRemake().getIdRemake()).orElse(null);
                    if (rm==null) throw new RuntimeException("Remarke(first) is null");
                    rm.getRemakes().add(vf2.getIdVideo());
                    rm = remakeRepository.save(rm);
                    vf2.setRemake(rm);
                    vf2.setDateModifFilm(new Date());
                    videoFilmRepository.save(vf2);
                } else {
                    rm = remakeRepository.findById(vf1.getRemake().getIdRemake()).orElse(null);
                    Remake rm2 = remakeRepository.findById(vf2.getRemake().getIdRemake()).orElse(null);
                    if (rm==null) throw new RuntimeException("Remarke(first) is null");
                    if (rm2==null) throw new RuntimeException("Remarke(remote) is null");
                    for (String idvideofilm: rm2.getRemakes()) {
                        VideoFilm vftmp = videoFilmRepository.findById(idvideofilm).orElse(null);
                        if(vftmp==null)continue;
                        vftmp.setRemake(rm);
                        vftmp.setDateModifFilm(new Date());
                        videoFilmRepository.save(vftmp);
                    }
                    rm.getRemakes().addAll(rm2.getRemakes());
                    rm = remakeRepository.save(rm);
                    remakeRepository.deleteByIdRemake(rm2.getIdRemake());
                    vf1.setRemake(rm);
                    vf1.setDateModifFilm(new Date());
                    videoFilmRepository.save(vf1);
                }
            }

        } else {
            throw new RuntimeException("Wrong IdVideoFilm format");
        }
        return rm;
    }

    @Override
    public TitileWithIdttt getTitleWithId(TitileWithIdttt titileWithIdttt) {
        VideoFilm vf = videoFilmRepository.findById(titileWithIdttt.getIdtt()).orElse(null);
        if(vf!=null){
            titileWithIdttt.setTitle(vf.getVideoTitles().get(0).getTitle());
        }
        return titileWithIdttt;
    }


}
