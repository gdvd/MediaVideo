package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.daoMysql.*;
import fr.gdvd.media_manager.entitiesMysql.*;
import fr.gdvd.media_manager.entitiesNoDb.*;
import fr.gdvd.media_manager.tools.Parser;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

@Log4j2
@Service
public class ManagmentFilesImpl implements ManagmentFiles {

    @Autowired
    private PreferencesRepository preferencesRepository;
    @Autowired
    private Parser parser;
    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private MyMediaInfoRepository myMediaInfoRepository;
    @Autowired
    private VideoNameExportRepository videoNameExportRepository;
    @Autowired
    private VideoSupportPathRepository videoSupportPathRepository;
    @Autowired
    private UserToNameExportRepository userToNameExportRepository;
    @Autowired
    private MyMediaLanguageRepository myMediaLanguageRepository;
    @Autowired
    private MyMediaAudioRepository myMediaAudioRepository;
    @Autowired
    private MyMediaTextRepository myMediatextRepository;
    @Autowired
    private MyMediaCommentRepository myMediaCommentRepository;
    @Autowired
    private TypeMmiRepository typeMmiRepository;
    @Autowired
    private VideoArtistRepository videoArtistRepository;
    @Autowired
    private TypeNameRepository typeNameRepository;

    @Autowired
    private MyMediaTextRepository myMediaTextRepository;
    @Autowired
    private VideoPosterRepository videoPosterRepository;
    @Autowired
    private VideoFilmArtistRepository videoFilmArtistRepository;
    @Autowired
    private VideoFilmRepository videoFilmRepository;
    @Autowired
    private VideoResumeRepository videoResumeRepository;
    @Autowired
    private VideoKeywordRepository videoKeywordRepository;
    @Autowired
    private VideoKindRepository videoKindRepository;
    @Autowired
    private VideoSourceInfoRepository videoSourceInfoRepository;
    @Autowired
    private VideoCountryRepository videoCountryRepository;
    @Autowired
    private VideoLanguageRepository videoLanguageRepository;
    @Autowired
    private VideoTitleRepository videoTitleRepository;
    @Autowired
    private VideoMoreInformationRepository videoMoreInformationRepository;
    @Autowired
    private VideoCommentRepository videoCommentRepository;
    @Autowired
    private VideoTraillerRepository videoTraillerRepository;
    @Autowired
    private RequestWebImpl requestWeb;
    @Autowired
    private RemakeRepository remakeRepository;

    @Autowired
    private ManagmentFilesImpl managmentFiles;

    @Override
    public ScanMessage scanFolderWithPathdir(String path) {
        path = path.replace("\\", "");
        path = path.startsWith("\"") ? path.substring(1) : path;
        path = path.startsWith("'") ? path.substring(1) : path;
        path = path.endsWith("\"") ? path.substring(0, path.length() - 1) : path;
        path = path.endsWith("'") ? path.substring(0, path.length() - 1) : path;
        path = path.startsWith("Volumes/") ? "/" + path : path;
        File f;
        if (path.charAt(0) == '~') {
            path = path.substring(1);
            f = new File(System.getProperty("user.home") + path);
        } else {
            f = new File(path);
        }
        ScanMessage sm = new ScanMessage();
        sm.setPathVideo(f.getAbsolutePath());
        if (f.exists()) {
            Preferences pref = preferencesRepository.findByIdPreferences("01");
            if (pref == null) {
                sm.setMessage("Error, Preferences doesn't exist");
            } else {
                if (f.isDirectory()) {
                    sm.setExtentionsRead(new ArrayList<>(pref.getExtset()));
                    String imaxStr = pref.getPrefmap().get("minSizeOfVideoFile");
                    if (imaxStr.equals("")) imaxStr = "10000000";
                    int imax = parseInt(imaxStr);
                    sm.setMinSizeOfVideoFile(imax);
                    sm.setExtentionsRead(sm.getExtentionsRead());
                    sm.setExtentionsNotRead(new ArrayList<>());
                    sm.setFilesRead(new ArrayList<>());
                    sm.setMessage("Directory");
                    parser.listAllDirectories(sm);
                } else {//Just One file
                    sm.setMessage("File");
                    sm.setExtentionsRead(sm.getExtentionsRead());
                    sm.setExtentionsNotRead(new ArrayList<>());
                    List<String> listonefile = new ArrayList<>();
                    listonefile.add(f.getAbsolutePath());
                    sm.setFilesRead(listonefile);
                }
            }
        } else {
            sm.setFilesRead(new ArrayList<>());
            sm.setExtentionsNotRead(new ArrayList<>());
            sm.setExtentionsRead(new ArrayList<>());
            sm.setMessage("Directory/file didn't exist");
        }
        return sm;
    }

    @Override
    public List<MyMediaInfo> getAllVideoByUser() {
        List<MyMediaInfo> lmmi = myMediaInfoRepository.findAll();
        return lmmi;
    }

    @Override
    public Page getAllVideoByUserAndByPage(String login, int pos, int page, int size) {
        List<MyMediaInfo> lmmi = new ArrayList<>();

//        Pageable findAll = PageRequest.of(page, pos);
        Pageable findAll2 = PageRequest.of(page, pos, Sort.by("fileSize"));
        Page p = myMediaInfoRepository.findAll(findAll2);

        return p;
    }


    @Override
    public Page getAllVideoByUserAndByPage2(String login, int pos, int page, int size) {
        List<MyMediaInfo> lmmi = new ArrayList<>();
        Pageable findAll = PageRequest.of(2, 4);
//        Pageable findAll2 = PageRequest.of(page, pos, Sort.by("fileSize"));
        List<MyMediaInfo> lm = myMediaInfoRepository.findAll();
        Page p = new PageImpl<>(lm, findAll, 8);
        return p;
    }

    @Override
    public Page<VideoSupportPath> getVspPP(String path) {
        Pageable findAll = PageRequest.of(2, 4);
        List<VideoSupportPath> vsp = videoSupportPathRepository.findAllByPathGeneralContains(path);
        Page p = new PageImpl<>(vsp, findAll, 3);
        return p;
    }

    @Override
    public Page<VideoNameExport> getVnePP(String path) {
        Pageable findAll = PageRequest.of(1, 2);
        return videoNameExportRepository.findAll(findAll);
    }

    @Override
    public Page<VideoSupportPath> listVspForLoginPP(String login, int page,
                                                    int size, String toSort,
                                                    String filter) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(toSort));
//        filter = cleanStringToUtf8(filter);
        return videoSupportPathRepository.findMyVspPP(login, filter, pageable);
    }

    @Override
    public Page<MyMediaInfo> listMmiForLoginPP(String login, int page,
                                               int size, String toSort,
                                               String filter, String vneName,
                                               boolean filename, String filtertt) {
        Pageable pageable = PageRequest.of(page, size);/*, Sort.by(toSort)*/
//        log.info(login + " || Filter : " + filter + " || vneName: " + vneName);
        filter = cleanStringToUtf16V1(filter);
        if (filtertt.equals("")) {
            if (filter.equals("%%")) { //Filter All name
                if (filename) { //filter on filename
                    if (vneName.equals("")) { //Filter on all VNE
                        return myMediaInfoRepository.findMmiPPFilterFilename(login, filter, pageable);
                    } else { //Filter on One VNE
                        Long idVne = videoNameExportRepository.findIdWithName(vneName).orElse(null);
                        if (idVne == null) {
                            List<MyMediaInfo> l = new ArrayList<>();
                            return new PageImpl<MyMediaInfo>(l);
                        }
                        return myMediaInfoRepository.findMmiWithFilterVNEPP(login, idVne, pageable);
                    }
                } else { //Filter on title.videofilm
                    if (vneName.equals("")) { //Filter on all VNE
                        // & tmmi.nameserie
                        return myMediaInfoRepository.findMmiPPAndTitleVfV6(login, filter, pageable);
                    } else { //Filter on One VNE
                        Long idVne = videoNameExportRepository.findIdWithName(vneName).orElse(null);
                        if (idVne == null) throw new RuntimeException("NameExport invalid");
                        return myMediaInfoRepository.findMmiPPWithFilterVNEAndTitleVf(login, filter, idVne, pageable);
                    }
                }

            } else {
                //Filter with one word
                if (filename) { //filter on filename
                    if (vneName.equals("")) { //Filter on all VNE
                        //Filter with word on filename & no Vne
                        return myMediaInfoRepository.findMmiPPFilterFilename(login, filter, pageable);
                    } else { //Filter on One VNE
                        //Filter with word on filename & with Vne
                        Long idVne = videoNameExportRepository.findIdWithName(vneName).orElse(null);
                        if (idVne == null) throw new RuntimeException("NameExport invalid");
                        return myMediaInfoRepository.findMmiWithFilterVNEPPV2(login, idVne, filter, pageable);
                    }
                } else { //Filter on title.videofilm & tmmi.nameserie
                    if (vneName.equals("")) { //Filter on all VNE
                        return myMediaInfoRepository.findMmiPPAndTitleVfV6(login, filter, pageable);
                    } else { //Filter on One VNE
                        Long idVne = videoNameExportRepository.findIdWithName(vneName).orElse(null);
                        if (idVne == null) throw new RuntimeException("NameExport invalid");
                        return myMediaInfoRepository.findMmiPPWithFilterVNEAndTitleVf(login, filter, idVne, pageable);
                    }
                }
            }
        } else {
            return myMediaInfoRepository.findMmiPPAndTitleTT(login, filtertt, pageable);
        }

    }

    private String cleanStringToUtf16V1(String str) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if ((int) str.charAt(i) < 255) {
                res.append(str.charAt(i));
            } else {
                log.info("cleanStringToUtf8 -> Char :  " + str.charAt(i) + " is filtered");
            }
        }
        return res.toString();
    }

    private String cleanStringToUtf16V2(String str) {
        byte[] bytes = str.getBytes();
        String res = null;
        res = new String(bytes, 0, bytes.length, StandardCharsets.UTF_16);
        return res;
    }

    @Override
    public Page<MyMediaInfo> listMmiForLoginWithNamePP(String login, int page, int size,
                                                       String toSort, String filter) {
        Pageable pageable = PageRequest.of(page, size);
        filter = cleanStringToUtf16V1(filter);
        return myMediaInfoRepository.findMmiLIKEfirstLastNamePP(login, filter, pageable);
    }

    @Override
    public void addcountry(String country) {
        if (country.length() < 2) throw new RuntimeException("Size incorect");
        if (country.length() > 32) country = country.substring(0, 31);

        Preferences pref = preferencesRepository.findByIdPreferences("c2title");
        if (pref != null) {
            pref.getExtset().add(country);
            preferencesRepository.save(pref);
        }
    }

    @Override
    public void deletecountry(String country) {
        Preferences pref = preferencesRepository.findByIdPreferences("c2title");
        if (pref != null) {
            if (pref.getExtset().contains(country)) {
                pref.getExtset().remove(country);
                preferencesRepository.save(pref);
//                preferencesRepository.modif(pref.getExtset() ,pref.getIdPreferences());
            }
        }
    }

    @Override
    public void updatealltitles() {
        Preferences pref = preferencesRepository.findByIdPreferences("01");
        if (pref != null) {
            Preferences prefTitles = preferencesRepository.findByIdPreferences("c2title");
            if (prefTitles != null) {
                List<String> listCountries = new ArrayList<>(prefTitles.getExtset());
                if (listCountries.size() == 0) throw new RuntimeException("Preference with counties doesn't exist");

                String pathTitles = pref.getPrefmap().get("pathFileTitles");
                if (pathTitles == null) throw new RuntimeException("pathFileTitles doesn't exist");
                if (pathTitles.equals("")) throw new RuntimeException("pathFileTitles doesn't exist");
                if (pathTitles.substring(0, 1).equals("~"))
                    pathTitles = pathTitles.substring(1);
                List<File> listfilestitles = parser.getAllFilesTitles(pathTitles);
                if (listfilestitles.size() != 0) {
                    List<String> listTitleOfFile = new ArrayList<>();
                    for (File file : listfilestitles) {
                        String t = file.getName();
                        if (t.substring(0, 2).equals("tt")) {
                            listTitleOfFile.add(file.getName());
                        }
                    }
                    List<String> listIdVideoFilms = videoFilmRepository.getAllIds();
                    // listIdtt listfilestitles listCountries listIdVideoFilms
                    for (String idtt : listIdVideoFilms) {
                        VideoFilm vf = videoFilmRepository.findById(idtt).orElse(null);
                        if (vf == null) throw new RuntimeException("VideoFilm doesn't exist");
                        String idVideoFilmWithReleaseinfo = idtt + "-releaseinfo.html";
                        if (listTitleOfFile.contains(idVideoFilmWithReleaseinfo)) {
                            File f = new File(System.getProperty("user.home") + pathTitles + idVideoFilmWithReleaseinfo);
                            if (f != null) {
                                String toParse4title = requestWeb.fileToString(f);
                                if (toParse4title.length() > 0)
                                    parser.addTitlesToVideoFilm(vf, toParse4title, listCountries);
                            } else {
                                log.error("File : " + idtt + "/releaseinfo is empty");
                            }
                        }
                    }
                }
            }
        } else {
            throw new RuntimeException("Preferences is not present");
        }
    }


    @Override // To test
    public Page<MyMediaInfo> researchByName(String nm, String login) {
        Pageable pageable = PageRequest.of(0, 5);
//        return videoArtistRepository.findMnWithNmPP(login, nm, pageable);
        return myMediaInfoRepository.findMmiWithNMamePP(nm, pageable);
    }

    @Override
    public void addnewext(String ext) {
        if (ext.length() > 1 && ext.length() < 10) {
            Preferences pref = preferencesRepository.findById("01").orElse(null);
            if (pref == null) throw new RuntimeException("Preferences doesn't exist");
            pref.getExtset().add(ext);
            preferencesRepository.save(pref);
        } else throw new RuntimeException("Extension must be : 1 < extension < 10");
    }


    @Override
    public List<Object> getObject() {
        return myMediaInfoRepository.getObject();
    }

    @Override
    public List<VideoSupportPath> getVsp(String pathGeneral) {
        return videoSupportPathRepository.findAllByPathGeneralContains(pathGeneral);
    }

    @Override
    public List<VideoNameExport> getVne(String path) {
//        return videoNameExportRepository.findAll();
        return null;
    }

    @Override
    public List<VideoSupportPath> getVneToAllPath(String login, String filter) {
        return videoSupportPathRepository.findMyVsp(login);
    }

    @Override
    public List<VideoSupportPath> listVspForLoginWithFilter(String login, String filter) {
//        filter = cleanStringToUtf8(filter);
        return videoSupportPathRepository.findMyVspWithFilter(login, filter);
    }

    @Override
    public List<VideoSupportPath> listVspForLogin(String login) {
        return videoSupportPathRepository.findMyVsp(login);
    }


    private String setMd5(String url) {
        File f = new File(url);
        String strMd5 = "";
        long lenghtFile = f.length();
        if (lenghtFile != 0) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(f);
                strMd5 = DigestUtils.md5Hex(fis);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strMd5;
    }

    public MyMediaInfo senddata(String url) {
        String md5 = setMd5(url);
        MyMediaInfo mmi = myMediaInfoRepository.findById(md5).orElse(null);
        if (!md5.equals("")) {
            if (mmi == null) {
                String mediaInfo = parser.readMediaInfo(url, "--Output=XML");
                mmi = new MyMediaInfo();
                mmi.setIdMyMediaInfo(md5);
                parser.createMyMediaInfo(mmi, mediaInfo);
            }
        }
        return mmi;
    }

    @Override
    public VideoNameExport ifNameExportExist(String nameExport) {
        return videoNameExportRepository.findByNameExport(nameExport);
    }

    @Override
    public VideoNameExport createNameExport(String nameExport, String login) {
        MyUser mu = myUserRepository.findByLogin(login);
        VideoNameExport vne = new VideoNameExport(null, nameExport, new Date(),
                false, false, new ArrayList<>(), new ArrayList<>());
        vne = videoNameExportRepository.save(vne);
        UserToNameExport utne = new UserToNameExport(mu, vne);
        utne.setActive(true);
        utne.setDateModif(new Date());
        userToNameExportRepository.save(utne);
        return vne;
    }

    @Override
    public VideoNameExport updateNameExport(Long idVideoNameExport, String login, int active, int complete) {
        VideoNameExport vne = videoNameExportRepository.findByIdVideoNameExport(idVideoNameExport);
        if (vne != null) {
            UserToNameExport userToNameExport = userToNameExportRepository.findAllByVideoNameExport_IdVideoNameExportAndMyUser_Login(idVideoNameExport, login);
            MyUser myUser = myUserRepository.findByLogin(login);
            if (userToNameExport.isActive() && myUser.isActive()) {
                vne.setActive(active == 1);
                vne.setComplete(complete == 1);
                return videoNameExportRepository.save(vne);
            }
        }
        return null;
    }

    @Override
    public void deleteOldVsp(List<String> oldPaths, Long idVideoNameExport) {
        for (String oldPath : oldPaths) {

            String path[] = oldPath.split("/");
            List<String> lstr = Arrays.asList(path);
            String title = lstr.get(lstr.size() - 1);

            String pathG = "";
            for (int lsi = 0; lsi < lstr.size() - 1; lsi++) {
                pathG = pathG + lstr.get(lsi) + "/";
            }

            VideoSupportPath vsp = videoSupportPathRepository.
                    findByTitleAndPathGeneralAndVideoNameExport_IdVideoNameExport(title, pathG, idVideoNameExport);
            if (vsp != null) {
                videoSupportPathRepository.delete(vsp);
            }
        }
    }

    @Override
    public List<VideoNameExport> getAllPath(String login) {
        return videoNameExportRepository.findMyVne(login);
    }

    @Override
    public List<VideoNameExport> getAllPathRemote(String login) {
        return videoNameExportRepository.findMyVneActive(login);
    }

    @Override
    public VideoNameExport storemmi(LinkMmiVf linkMmiVf, int idvne,
                                    int idVneRemote, int withUpdate) {

        List<MyMediaInfo> lmmi = getCompleteListMyMediaInfos(linkMmiVf);
        VideoNameExport vne = videoNameExportRepository.findByIdVideoNameExport((long) idvne);

        for (MyMediaInfo mmiremote : lmmi) {

            //Manage Mmi
            MyMediaInfo mmi = myMediaInfoRepository.findById(mmiremote.getIdMyMediaInfo()).orElse(null);
            if (mmi == null) {
                mmi = createMmiWithMmiremote(mmiremote);
            } else {
                if (withUpdate == 1 &&
                        mmiremote.getDateModif().compareTo(mmi.getDateModif()) > 0) {
                    // then verify mymediaaudio, mymediatext, or mymediacomment
                    verifyMmi(mmiremote, mmi);
                }
            }

            /*if (mmiremote.getIdMyMediaInfo().equals("1f2c9d2bd195a2dc6778804bcb9f3b12")) {
                System.out.println("Stop");
            }*/
            // Manage TypeMmi & VideoFilm
            if (mmiremote.getTypeMmi() != null && mmiremote.getTypeMmi().isActive()) {
                if (mmi.getTypeMmi() != null) {
                    // UPDATE typeMmi with typeMmiremote
                    upDateTypeMmiAndSave(mmi, mmiremote.getTypeMmi(), withUpdate);
                }else{
                    upDateTypeMmiAndSave(mmi, mmiremote.getTypeMmi(), 1);
                }
            }


            // MMI ok -> Create VSP if din't exist
            for (VideoSupportPath vspremote : mmiremote.getVideoSupportPaths()) {
                if (vspremote.getId().getIdVideoNameExport() == idVneRemote) {
                    String pathGen = vspremote.getId().getPathGeneral();
                    String title = vspremote.getId().getTitle();
                    VideoSupportPath vsp = videoSupportPathRepository
                            .findByTitleAndPathGeneralAndMyMediaInfo_IdMyMediaInfoAndVideoNameExport_IdVideoNameExport(
                                    title, pathGen, mmi.getIdMyMediaInfo(), (long) idvne);
                    if (vsp == null) {
                        vsp = new VideoSupportPath(title, pathGen, mmi.getIdMyMediaInfo(), (long) idvne);
                    }
                    vsp.setActive(true);
                    vsp.setDateModif(new Date());
                    vsp = videoSupportPathRepository.save(vsp);
                }
            }
        }
        // VSP are created -> active VNE
        vne.setActive(true);
        vne.setComplete(true);
        return videoNameExportRepository.save(vne);
    }

    @NotNull
    List<MyMediaInfo> getCompleteListMyMediaInfos(LinkMmiVf linkMmiVf) {
        List<MyMediaInfo> lmmi = linkMmiVf.getLmmi();
        List<LinkVfTmmi> linkVfTmmi = linkMmiVf.getLlvftmmi();

        lmmi.forEach(mmi -> {
            if (mmi.getTypeMmi() != null) {
                for (LinkVfTmmi link : linkVfTmmi) {
                    if (mmi.getTypeMmi().getIdTypeMmi().longValue() == link.getLink().longValue()) {
                        mmi.getTypeMmi().setVideoFilm(link.getVf());
                        break;
                    }
                }
            }
        });
        return lmmi;
    }

    @NotNull
    MyMediaInfo createMmiWithMmiremote(MyMediaInfo mmiremote) {
        MyMediaInfo mmi = new MyMediaInfo();
        mmi.setDateModif(new Date());
        mmi.setIdMyMediaInfo(mmiremote.getIdMyMediaInfo());
        mmi.setBitrate(mmiremote.getBitrate());
        mmi.setCodecId(mmiremote.getCodecId());
        mmi.setDuration(mmiremote.getDuration());
        mmi.setFileSize(mmiremote.getFileSize());
        mmi.setFormat(mmiremote.getFormat());
        mmi.setHeight(mmiremote.getHeight());
        mmi.setWidth(mmiremote.getWidth());
        mmi = myMediaInfoRepository.save(mmi);

        for (MyMediaComment mmcremote : mmiremote.getMyMediaComments()) {
            MyMediaComment mmc = new MyMediaComment();
            mmc.setMediaComment(mmcremote.getMediaComment());
            mmc.setMyMediaInfo(mmi);
            myMediaCommentRepository.save(mmc);
        }

        for (MyMediaAudio mmaremote : mmiremote.getMyMediaAudios()) {
            String l = mmaremote.getMyMediaLanguage().getLanguage();
            MyMediaLanguage ml = myMediaLanguageRepository.findByLanguage(l);
            if (ml == null) {
                ml = myMediaLanguageRepository.save(new MyMediaLanguage(l));
            }
            MyMediaAudio mma = myMediaAudioRepository.
                    findByMyMediaInfo_IdMyMediaInfoAndMyMediaLanguage_IdMyMediaLanguage(
                            mmi.getIdMyMediaInfo(), ml.getIdMyMediaLanguage());
            if (mma == null) {
                mma = new MyMediaAudio(mmi, ml);
//                        mma.setMyMediaInfo(mmi);
//                        mma.setMyMediaLanguage(ml);
                mma.setBitrate(mmaremote.getBitrate());
                mma.setChannels(mmaremote.getChannels());
                mma.setDuration(mmaremote.getDuration());
                mma.setForced(mmaremote.isForced());
                mma.setFormat(mmaremote.getFormat());
                myMediaAudioRepository.save(mma);
            }
        }
        for (MyMediaText mmtremote : mmiremote.getMyMediaTexts()) {
            String l = mmtremote.getMyMediaLanguage().getLanguage();
            MyMediaLanguage ml = myMediaLanguageRepository.findByLanguage(l);
            if (ml == null) {
                ml = myMediaLanguageRepository.save(new MyMediaLanguage(l));
            }
            MyMediaText mmt = myMediatextRepository.
                    findByMyMediaInfo_IdMyMediaInfoAndMyMediaLanguage_IdMyMediaLanguage(
                            mmi.getIdMyMediaInfo(), ml.getIdMyMediaLanguage());
            if (mmt == null) {
                mmt = new MyMediaText(mmi, ml);
//                        mmt.setMyMediaLanguage(ml);
//                        mmt.setMyMediaInfo(mmi);
                mmt.setCodecId(mmtremote.getCodecId());
                mmt.setForced(mmtremote.isForced());
                mmt.setFormat(mmtremote.getFormat());
                mmt.setInternal(mmtremote.isInternal());
                myMediatextRepository.save(mmt);
            }
        }
        return mmi;
    }

    /*void saveTypemmiWithTMRemote(MyMediaInfo mmiremote, MyMediaInfo mmi) {
        TypeMmi tm = new TypeMmi();
        tm.setActive(true);
        mmi.setTypeMmi(tm);
        upDateTypeMmiAndSave(mmi.getTypeMmi(), mmiremote.getTypeMmi());
        myMediaInfoRepository.save(mmi);
    }*/

    private void updateVideoFilmWithremoteVF(VideoFilm videoFilm, VideoFilm videoFilmRemote) {
        if (videoFilmRemote.getVideoPosters() != null && videoFilmRemote.getVideoPosters().size()!=0) {
            if (videoFilm.getVideoPosters() != null  && videoFilm.getVideoPosters().size()!=0) {
                VideoPoster oldVP = videoPosterRepository.findFirstByVideoFilm(videoFilm);
                //Update urlImg
                oldVP.setUlrImg(videoFilmRemote.getVideoPosters().get(0).getUlrImg());
                videoPosterRepository.save(oldVP);
            } else {
                VideoPoster newVP = new VideoPoster(
                        null,
                        videoFilmRemote.getVideoPosters().get(0).getIdMd5Poster(),
                        videoFilmRemote.getVideoPosters().get(0).getFileName(),
                        videoFilmRemote.getVideoPosters().get(0).getUlrImg(),
                        videoFilm
                );
                videoPosterRepository.save(newVP);
            }

        }
        if (videoFilmRemote.getVideoComment() != null) {
            if (videoFilm.getVideoComment() != null) {
                VideoComment oldVC = videoCommentRepository.findByVideoFilm(videoFilm);
                oldVC.setComment(videoFilmRemote.getVideoComment().getComment());
                videoCommentRepository.save(oldVC);
            } else {
                VideoComment newVC = new VideoComment(
                        null,
                        videoFilmRemote.getVideoComment().getComment(),
                        videoFilm);
                videoCommentRepository.save(newVC);
            }
        }
        if (videoFilmRemote.getVideoTrailler() != null) {
            if (videoFilm.getVideoTrailler() != null) {
                VideoTrailler oldVT = videoTraillerRepository.findByVideoFilm(videoFilm);
                oldVT.setTrailler(videoFilmRemote.getVideoTrailler().getTrailler());
                videoTraillerRepository.save(oldVT);
            } else {
                VideoTrailler newVT = new VideoTrailler(null,
                        videoFilmRemote.getVideoTrailler().getTrailler(),
                        videoFilm);
                videoTraillerRepository.save(newVT);
            }
        }
    }

    private TypeMmi upDateTypeMmiAndSave(MyMediaInfo mmi, TypeMmi typeMmiRemote, int withUpdate) {
        TypeMmi tmmiNew = null;
        if(mmi.getTypeMmi() != null) {
            tmmiNew = typeMmiRepository.findByIdTypeMmi( mmi.getTypeMmi().getIdTypeMmi());
        }else{
            tmmiNew = new TypeMmi();
        }
        if (withUpdate == 1) updateInfo(typeMmiRemote, tmmiNew);
        if (typeMmiRemote.getVideoFilm() != null) {
            String idVf = typeMmiRepository.getIdVideoFilmWithIdTmmi(tmmiNew.getIdTypeMmi())
                    .orElse("");
            if (idVf.length() > 0) {
                tmmiNew.setVideoFilm(videoFilmRepository.findById(idVf).orElse(null));
            } else {
                //Search if videoFilm with typeMmiRemote.VideoFilm.idVideo exist
                VideoFilm vf = videoFilmRepository.findById(
                        typeMmiRemote.getVideoFilm().getIdVideo()).orElse(null);
                if (vf != null) {
                    updateVideoFilmWithremoteVF(vf, typeMmiRemote.getVideoFilm());
                    tmmiNew.setVideoFilm(typeMmiRemote.getVideoFilm());
                } else {
                    //Create new VideoFilm with VideoFilmRemote
                    vf = managmentFiles.addVideoFilm(
                            typeMmiRemote.getVideoFilm());
                    tmmiNew.setVideoFilm(vf);
                }
            }
            tmmiNew = typeMmiRepository.save(tmmiNew);
            mmi.setTypeMmi(tmmiNew);
            myMediaInfoRepository.save(mmi);
        }
        return tmmiNew;
    }

    private void updateInfo(TypeMmi typeMmiRemote, TypeMmi tmmiNew) {
        tmmiNew.setNameSerieVO(typeMmiRemote.getNameSerieVO());
        tmmiNew.setNameSerie(typeMmiRemote.getNameSerie());
        TypeName tn = typeNameRepository.findByTypeName(typeMmiRemote.getTypeName().getTypeName()).orElse(null);
        if (tn == null) {
            tn = typeNameRepository.save(
                    new TypeName(null,
                            typeMmiRemote.getTypeName().getTypeName().substring(0, 15), null));
        }
        tmmiNew.setTypeName(tn);
        tmmiNew.setEpisode(typeMmiRemote.getEpisode());
        tmmiNew.setSeason(typeMmiRemote.getSeason());
        if(typeMmiRemote.getDateModif()!=null){
            tmmiNew.setDateModif(typeMmiRemote.getDateModif());
        }
        tmmiNew.setActive(true);
    }

    private void verifyMmi(MyMediaInfo mmiremote, MyMediaInfo mmi) {
        boolean setDate = false;
        //Verify comment
        if (mmiremote.getMyMediaComments() != null && mmiremote.getMyMediaComments().size() > 0) {
            if (mmi.getMyMediaComments() != null && mmi.getMyMediaComments().size() > 0) {
                for (MyMediaComment mmcremote : mmiremote.getMyMediaComments()) {
                    boolean test = false;
                    for (MyMediaComment mmc : mmi.getMyMediaComments()) {
                        if (mmc.getMediaComment().equals(mmcremote.getMediaComment())) test = true;
                    }
                    if (!test) {// Only if didn't exist in mmi
                        MyMediaComment nmmc = new MyMediaComment(null,
                                mmcremote.getMediaComment(), mmi);
                        myMediaCommentRepository.save(nmmc);
                        setDate = true;
                    }
                }
            } else { //Add all
                for (MyMediaComment mmcremote : mmiremote.getMyMediaComments()) {
                    MyMediaComment nmmc = new MyMediaComment(null,
                            mmcremote.getMediaComment(), mmi);
                    myMediaCommentRepository.save(nmmc);
                    setDate = true;
                }
            }
        }
        //Verify audio
        if (mmiremote.getMyMediaAudios() != null && mmiremote.getMyMediaAudios().size() > 0) {
            List<MyMediaAudio> mmaRemoteOnly = new ArrayList<>();
            List<MyMediaAudio> mmaToAdd = new ArrayList<>();
            List<MyMediaAudio> mmaToDelete = new ArrayList<>();
            List<MyMediaAudio> mmaStayIn = new ArrayList<>();
            if (!(mmi.getMyMediaAudios() != null && mmi.getMyMediaAudios().size() > 0)) {
                //Add all mmaremote to news
                mmaToAdd.addAll(mmiremote.getMyMediaAudios());
            } else {
                for (MyMediaAudio mmaremote : mmiremote.getMyMediaAudios()) {
                    boolean test = false;
                    for (MyMediaAudio mma : mmi.getMyMediaAudios()) {
                        if (mmaremote.getMyMediaLanguage().getLanguage()
                                .equals(mma.getMyMediaLanguage().getLanguage())) {
                            test = true;
                            mmaStayIn.add(mma);
                        }
                    }
                    if (!test) {
                        mmaRemoteOnly.add(mmaremote);
                    }
                }
                if (mmaRemoteOnly.size() > 0) {
                    // arbitrary choice -> if each mmaRemoteOnly.getMyMediaLanguage().getLanguage().length()
                    // is between 2 and 5 ,then we rewrite language.local with language.remote
                    boolean test = true;
                    for (MyMediaAudio ma : mmaRemoteOnly) {
                        int length = ma.getMyMediaLanguage().getLanguage().length();
                        if (length <= 2 || length >= 5) {
                            test = false;
                            break;
                        }
                    }
                    if (test) { //then we rewrite the remote language
                        //  Save  all in mmaNewToAdd
                        for (MyMediaAudio mma : mmi.getMyMediaAudios()) {
                            boolean test2 = false;
                            for (MyMediaAudio mmastay : mmaStayIn) {
                                if (mma.getMyMediaLanguage().getLanguage().equals(mmastay.getMyMediaLanguage().getLanguage())) {
                                    test2 = true;
                                    break;
                                }
                            }
                            if (!test2) {
                                mmaToDelete.add(mma);
                            }
                        }
                        mmaToAdd = mmaRemoteOnly;
                    }
                }
                if (mmaToDelete.size() == mmaToAdd.size()) { // If the deal is correct, Erase all in mmaToDelete
                    for (MyMediaAudio mma : mmaToDelete) {
                        myMediaAudioRepository.deleteOnelink(mmi.getIdMyMediaInfo(),
                                mma.getMyMediaLanguage().getIdMyMediaLanguage());
                    }
                }
            }
            if (mmaToAdd.size() != 0) {// Add mma with mmaToAdd
                for (MyMediaAudio mma : mmaToAdd) {
                    String l = mma.getMyMediaLanguage().getLanguage();
                    MyMediaLanguage mml = myMediaLanguageRepository.findByLanguage(l);
                    if (mml == null) mml = myMediaLanguageRepository.save(new MyMediaLanguage(null, l, null, null));
                    MyMediaAudio ma = new MyMediaAudio(mmi, mml);
                    ma.setBitrate(mma.getBitrate());
                    ma.setChannels(mma.getChannels());
                    ma.setDuration(mma.getDuration());
                    ma.setForced(mma.isForced());
                    myMediaAudioRepository.save(ma);
                }
                setDate = true;
            }
        }
        //Verify text
        if (mmiremote.getMyMediaTexts() != null && mmiremote.getMyMediaTexts().size() > 0) {
            List<MyMediaText> mmaRemoteOnly = new ArrayList<>();
            List<MyMediaText> mmaToAdd = new ArrayList<>();
            List<MyMediaText> mmaToDelete = new ArrayList<>();
            List<MyMediaText> mmaStayIn = new ArrayList<>();
            if (!(mmi.getMyMediaTexts() != null && mmi.getMyMediaTexts().size() > 0)) {
                //Add all mmaremote to news
                mmaToAdd.addAll(mmiremote.getMyMediaTexts());
            } else {
                for (MyMediaText mmaremote : mmiremote.getMyMediaTexts()) {
                    boolean test = false;
                    for (MyMediaText mma : mmi.getMyMediaTexts()) {
                        if (mmaremote.getMyMediaLanguage().getLanguage()
                                .equals(mma.getMyMediaLanguage().getLanguage())) {
                            test = true;
                            mmaStayIn.add(mma);
                        }
                    }
                    if (!test) {
                        mmaRemoteOnly.add(mmaremote);
                    }
                }
                if (mmaRemoteOnly.size() > 0) {
                    // arbitrary choice -> between 2 and 5
                    boolean test = true;
                    for (MyMediaText ma : mmaRemoteOnly) {
                        int length = ma.getMyMediaLanguage().getLanguage().length();
                        if (length <= 2 || length >= 5) {
                            test = false;
                            break;
                        }
                    }
                    if (test) { //then we rewrite the remote language
                        //  Save  all in mmaNewToAdd
                        for (MyMediaText mma : mmi.getMyMediaTexts()) {
                            boolean test2 = false;
                            for (MyMediaText mmastay : mmaStayIn) {
                                if (mma.getMyMediaLanguage().getLanguage().equals(mmastay.getMyMediaLanguage().getLanguage())) {
                                    test2 = true;
                                    break;
                                }
                            }
                            if (!test2) {
                                mmaToDelete.add(mma);
                            }
                        }
                        mmaToAdd = mmaRemoteOnly;
                    }
                }
                if (mmaToDelete.size() == mmaToAdd.size()) { // If the deal is correct, Erase all in mmaToDelete
                    for (MyMediaText mma : mmaToDelete) {
                        myMediaTextRepository.deleteOnelink(mmi.getIdMyMediaInfo(), mma.getMyMediaLanguage().getIdMyMediaLanguage());
                    }
                }
            }
            if (mmaToAdd.size() != 0) {// Add mma with mmaToAdd
                for (MyMediaText mma : mmaToAdd) {
                    String l = mma.getMyMediaLanguage().getLanguage();
                    MyMediaLanguage mml = myMediaLanguageRepository.findByLanguage(l);
                    if (mml == null) mml = myMediaLanguageRepository.save(new MyMediaLanguage(null, l, null, null));
                    MyMediaText ma = new MyMediaText(mmi, mml);
                    ma.setFormat(mma.getFormat());
                    ma.setInternal(mma.isInternal());
                    ma.setCodecId(mma.getCodecId());
                    ma.setForced(mma.isForced());
                    myMediaTextRepository.save(ma);
                }
                setDate = true;
            }
        }
        if (setDate) {
            mmi.setDateModif(mmiremote.getDateModif());
            myMediaInfoRepository.save(mmi);
        }

    }
/*
    private TypeMmi addTypeMmi(TypeMmi tmmi) {
        TypeName tn = tmmi.getTypeName();
        if (tn == null) {
            tn = new TypeName(null, "autre", null);
            typeNameRepository.save(tn);
        }
        TypeMmi tmmiNew = new TypeMmi(null, tmmi.getSeason(), tmmi.getEpisode(), tmmi.getNameSerie(),
                tmmi.getNameSerieVO(), true, new Date(), tn, null, null);
        return typeMmiRepository.save(tmmiNew);
    }*/

    private VideoFilm addVideoFilm(VideoFilm vfRemote) {
        VideoFilm vf = new VideoFilm();
        vf.setDateModifFilm(vfRemote.getDateModifFilm());

        VideoSourceInfo vsi = videoSourceInfoRepository
                .findBySourceUrl(vfRemote.getVideoSourceInfo().getSourceUrl()).orElse(null);
        if (vsi == null) {
            vsi = new VideoSourceInfo(vfRemote.getVideoSourceInfo().getIdSourceInfo(),
                    vfRemote.getVideoSourceInfo().getSourceUrl(),
                    vfRemote.getVideoSourceInfo().getName(),
                    new Date(), null);
            vsi = videoSourceInfoRepository.save(vsi);
        }
        vf.setVideoSourceInfo(vsi);
        vf.setYear(vfRemote.getYear());
        vf.setScoreOnHundred(vfRemote.getScoreOnHundred());
        vf.setNbOfVote(vfRemote.getNbOfVote());
        Remake nrem = null;
        if (vfRemote.getRemake() != null) {
            Optional<Remake> rem = getOneRemake(vfRemote.getRemake());
            if (rem.isPresent()) {
                rem.get().getRemakes().add(vfRemote.getIdVideo());
                nrem = remakeRepository.save(rem.get());
            }
        }

        vf.setIdVideo(vfRemote.getIdVideo());
        vf.setRemake(nrem);
        vf = videoFilmRepository.save(vf);
        if (nrem != null) {
            for (String r : vfRemote.getRemake().getRemakes()) {
                VideoFilm v = videoFilmRepository.findById(r).orElse(null);
                if (v != null) {
                    if (v != null && vf.getRemake().getIdRemake() != nrem.getIdRemake()) {
                        v.setRemake(nrem);
                        videoFilmRepository.save(v);
                    }
                }
            }
        }
        if (vfRemote.getVideoPosters() != null && vfRemote.getVideoPosters().size() > 0) {
            for (VideoPoster vp : vfRemote.getVideoPosters()) {
                VideoPoster vpnew = new VideoPoster(null, vp.getIdMd5Poster(), vp.getFileName(), vp.getUlrImg(), vf);
                videoPosterRepository.save(vpnew);
            }
        }
        if (vfRemote.getVideoResumes() != null && vfRemote.getVideoResumes().size() > 0) {
            for (VideoResume vr : vfRemote.getVideoResumes()) {
                VideoResume vrnew = new VideoResume(null, vr.getTextResume(), new Date(), vf);
                videoResumeRepository.save(vrnew);
            }
        }
        if (vfRemote.getVideoTrailler() != null) {
            VideoTrailler vt = new VideoTrailler(null, vfRemote.getVideoTrailler()
                    .getTrailler(), vf);
            videoTraillerRepository.save(vt);
        }
        if (vfRemote.getVideoComment() != null) {
            VideoComment vc = new VideoComment(null,
                    vfRemote.getVideoComment().getComment(), vf);
            videoCommentRepository.save(vc);
        }
        if (vfRemote.getVideoFilmArtists() != null) {
            for (VideoFilmArtist vfa : vfRemote.getVideoFilmArtists()) {
                VideoArtist vaNew = videoArtistRepository.findById(vfa.getVideoArtist()
                        .getIdVideoArtist()).orElse(null);
                if (vaNew == null)
                    vaNew = videoArtistRepository.save(new VideoArtist(vfa.getVideoArtist()
                            .getIdVideoArtist(),
                            vfa.getVideoArtist().getFirstLastName(), null));
                VideoFilmArtist vfaNew = videoFilmArtistRepository
                        .findByVideoFilmAndVideoArtist(vf, vaNew).orElse(null);
                if (vfaNew == null) {
                    vfaNew = new VideoFilmArtist(vaNew, vf);
                }
                vfaNew.setMusic(vfa.isMusic());
                vfaNew.setProducer(vfa.isProducer());
                vfaNew.setDirector(vfa.isDirector());
                vfaNew.setActor(vfa.isActor());
                vfaNew.setNumberOrderActor(vfa.getNumberOrderActor());
                vfaNew.setWriter(vfa.isWriter());
                videoFilmArtistRepository.save(vfaNew);
            }
        }
        if (vfRemote.getVideoKeywordSet() != null) {
            List<VideoKeyword> lvk = new ArrayList<>();
            for (VideoKeyword vk : vfRemote.getVideoKeywordSet()) {
                VideoKeyword vknew = videoKeywordRepository.findByKeywordEn(vk.getKeywordEn()).orElse(null);
                if (vknew == null) {
                    vknew = new VideoKeyword(null, vk.getKeywordEn(),
                            vk.getKeywordFr(), null);
                    vknew = videoKeywordRepository.save(vknew);
                }
                lvk.add(vknew);
            }
            vf.getVideoKeywordSet().addAll(lvk);
        }
        if (vfRemote.getVideoKinds() != null) {
            List<VideoKind> lvk = new ArrayList<>();
            for (VideoKind vk : vfRemote.getVideoKinds()) {
                VideoKind vknew = videoKindRepository.findByKindEn(vk.getKindEn()).orElse(null);
                if (vknew == null) {
                    vknew = new VideoKind(null, vk.getKindEn(), vk.getKindFr(),
                            null);
                    vknew = videoKindRepository.save(vknew);
                }
                lvk.add(vknew);
            }
            vf.getVideoKinds().addAll(lvk);
        }
        if (vfRemote.getVideoLanguages() != null) {
            List<VideoLanguage> lvl = new ArrayList<>();
            for (VideoLanguage vl : vfRemote.getVideoLanguages()) {
                VideoLanguage vlnew = videoLanguageRepository.findByUrlLanguage(vl.getUrlLanguage()).orElse(null);
                if (vlnew == null) {
                    vlnew = new VideoLanguage(null, vl.getLanguage(),
                            vl.getUrlLanguage(), null);
                    vlnew = videoLanguageRepository.save(vlnew);
                }
                lvl.add(vlnew);
            }
            vf.getVideoLanguages().addAll(lvl);
        }
        if (vfRemote.getVideoMoreInformation() != null) {
            VideoMoreInformation vmi = new VideoMoreInformation(null,
                    vfRemote.getVideoMoreInformation().getInformap(), vf);
            videoMoreInformationRepository.save(vmi);
        }
        if (vfRemote.getVideoCountries() != null) {
            List<VideoCountry> lvc = new ArrayList<>();
            for (VideoCountry vc : vfRemote.getVideoCountries()) {
                VideoCountry vcnew = videoCountryRepository.findByCountry(vc.getCountry()).orElse(null);
                if (vcnew == null) {
                    vcnew = new VideoCountry(null, vc.getCountry(), vc.getUrlCountry(),
                            null, null);
                    vcnew = videoCountryRepository.save(vcnew);
                }
                lvc.add(vcnew);
            }
            vf.getVideoCountries().addAll(lvc);
        }
        if (vfRemote.getVideoTitles() != null) {
            for (VideoTitle vt : vfRemote.getVideoTitles()) {
                VideoCountry vcnew = videoCountryRepository.findByCountry(vt.getVideoCountry()
                        .getCountry()).orElse(null);
                if (vcnew == null) {
                    vcnew = new VideoCountry(null, vt.getVideoCountry()
                            .getCountry(), vt.getVideoCountry().getUrlCountry(),
                            null, null);
                    vcnew = videoCountryRepository.save(vcnew);
                }
                VideoTitle vtnew = videoTitleRepository.findByVideoFilmAndVideoCountry(vf, vcnew).orElse(null);
                if (vtnew == null) {
                    vtnew = new VideoTitle(vcnew, vf);
                    vtnew.setTitle(vt.getTitle());
                    videoTitleRepository.save(vtnew);
                }
            }
        }
        return videoFilmRepository.save(vf);
    }

    private Optional<Remake> getOneRemake(Remake rem) {
        if (rem != null) {
            for (String r : rem.getRemakes()) {
                VideoFilm vf = new VideoFilm();
                vf.setIdVideo(r);
                Optional<Remake> nr = remakeRepository.findFirstByVideoFilms(vf);
                if (nr.isPresent()) {
                    return nr;
                }
            }
            List<VideoFilm> lvf = new ArrayList<>();
            for (String r : rem.getRemakes()) {
                Optional<VideoFilm> vf = videoFilmRepository.findById(r);
                vf.ifPresent(lvf::add);
            }
            if (lvf.size() != 0) {
                Remake r = new Remake(null,
                        lvf.stream().map(VideoFilm::getIdVideo).collect(Collectors.toSet()),
                        null);
                return Optional.ofNullable(r);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<MyMediaInfo> getAllMmi(List<String> listIdMmi) {
        List<MyMediaInfo> lmmi = new ArrayList<>();
        for (String md5 : listIdMmi) {
            lmmi.add(myMediaInfoRepository.findByIdMyMediaInfo(md5));
        }
        return lmmi;
    }

    @Override
    public List<VideoSupportPath> getLisOldVsp(String oldVsp) {
        return videoSupportPathRepository.findAllByVideoNameExport_NameExport(oldVsp);
    }

    @Override
    public List<VideoNameExport> toggleActivationLogintonameexport(Long idvne,
                                                                   String oldstate,
                                                                   String logintoapply,
                                                                   String login) {
        MyUser mu = myUserRepository.findByLogin(login);
        if (mu == null) throw new UsernameNotFoundException("invalid login");
        boolean test = false;
        for (MyRole r : mu.getRoles()) {
            if (r.getRole().equals("ADMIN")) test = true;
        }
        MyUser mutoapply = myUserRepository.findByLogin(logintoapply);
        if (mutoapply == null) throw new UsernameNotFoundException("invalid login");
        if (!test) throw new UsernameNotFoundException("invalid role");
        VideoNameExport onevne = videoNameExportRepository.findByIdVideoNameExport(idvne);
        if (onevne == null) throw new UsernameNotFoundException("invalid idvne");
        UserToNameExport utne = userToNameExportRepository
                .findAllByVideoNameExport_IdVideoNameExportAndMyUser_Login(idvne, logintoapply);
        if (utne == null) {
            utne = new UserToNameExport(mutoapply, onevne);
            utne.setActive(false);
        }
        utne.setDateModif(new Date());
        utne.setActive(!utne.isActive());
        userToNameExportRepository.save(utne);
//        return videoNameExportRepository.findAll();
        return videoNameExportRepository.findMyVne(login);
    }


    @Override
    public MyMediaInfo createMmiAndGetMd5(String pathGeneral) {

        MyMediaInfo mmi = searchMmiWithManyFeatures(pathGeneral);
//        MyMediaInfo mmi = null;
        String md5 = "";

        if (mmi == null) {
            md5 = setMd5(pathGeneral);
            if (md5.equals("")) throw new RuntimeException("idMd5 doesn't exist");
            mmi = myMediaInfoRepository.findById(md5).orElse(null);
            if (mmi == null) {
                mmi = new MyMediaInfo();
                mmi.setDateModif(new Date());
                mmi.setIdMyMediaInfo(md5);
                String mediaInfoStr = parser.readMediaInfo(pathGeneral, "--Output=XML");
                parser.createMyMediaInfo(mmi, mediaInfoStr);
                mmi = myMediaInfoRepository.findById(md5).orElse(null);
            }
        }
        // recall mmi to have videoList
        return mmi;
    }

    private MyMediaInfo searchMmiWithManyFeatures(String pathGeneral) {
        MyMediaInfo mmi = null;

        // method to keep the floating point of duration value
        String[] mediaInfoDurationTbl = parser.readMediaInfo(pathGeneral,
                "--Inform=General;%Duration/String3%").replace("\n", "").split(":");
        Collections.reverse(Arrays.asList(mediaInfoDurationTbl));
        Double duration = 0.0;
        if (mediaInfoDurationTbl.length != 0 & (!mediaInfoDurationTbl[0].equals(""))) {
            String secStr = mediaInfoDurationTbl[0];
            duration = Double.parseDouble(secStr);
            if (mediaInfoDurationTbl.length > 1) {
                String minStr = mediaInfoDurationTbl[1];
                duration = ((Double.parseDouble(minStr)) * 60) + duration;
                if (mediaInfoDurationTbl.length > 2) {
                    String hrsStr = mediaInfoDurationTbl[2];
                    duration = ((Double.parseDouble(hrsStr)) * 3600) + duration;
                }
            }
        } else {
            return mmi;
        }

        String mediaInfoFileSizeStr = parser.readMediaInfo(pathGeneral,
                "--Inform=General;%FileSize%")
                .replace("\n", "");

        String mediaInfoVideoLightStr = parser.readMediaInfo(pathGeneral,
                "--Inform=Video;%Width%§%Height%§%BitRate%§%CodecID%")
                .replace("\n", "");

        String[] mediaInfoVideoLightTbl = mediaInfoVideoLightStr.split("§");

        if (mediaInfoFileSizeStr.length() > 2 && mediaInfoVideoLightTbl.length == 4) {

            Double filesize = 0.0;
            if (Pattern.matches("[\\d]{2,12}", mediaInfoFileSizeStr)) {
                int width = 0;
                String withStr = mediaInfoVideoLightTbl[0];
                if (Pattern.matches("[\\d]{1,4}", withStr)) {
                    int height = 0;
                    String heightStr = mediaInfoVideoLightTbl[1];
                    if (Pattern.matches("[\\d]{1,4}", heightStr)) {
                        Double bitrate = 0.0;
                        String biterateStr = mediaInfoVideoLightTbl[2];
                        if (Pattern.matches("[\\d]{2,10}", biterateStr)) {

                            String codecidStr = mediaInfoVideoLightTbl[3];

                            filesize = Double.parseDouble(mediaInfoFileSizeStr);
                            width = Integer.parseInt(withStr);
                            height = Integer.parseInt(heightStr);
                            bitrate = Double.parseDouble(biterateStr);

                            List<String> lmmi = myMediaInfoRepository.findMmiWithFeatures(duration,
                                    filesize, width, height, bitrate, codecidStr);

                            if (lmmi.size() != 0) {
                                mmi = myMediaInfoRepository.findByIdMyMediaInfo(lmmi.get(0));
                                if (lmmi.size() > 1) {
                                    final StringBuilder lid = new StringBuilder();
                                    lmmi.forEach(lid::append);
                                    log.error("****** Many idMmi with same features, ListMmi : "
                                            + lid.toString());
                                }
                            }
                        }
                    }
                }
            }
        }
        return mmi;
    }

    @Override
    public VideoSupportPath createVSP(Long idVideoNameExport, String md5, String pathGeneral) {
        String path[] = pathGeneral.split("/");
        List<String> lstr = Arrays.asList(path);
        String title = lstr.get(lstr.size() - 1);

        String pathG = "";
        for (int lsi = 0; lsi < lstr.size() - 1; lsi++) {
            pathG = pathG + lstr.get(lsi) + "/";
        }

        VideoSupportPath vsp = videoSupportPathRepository
                .findByTitleAndPathGeneralAndMyMediaInfo_IdMyMediaInfoAndVideoNameExport_IdVideoNameExport(
                        title, pathG, md5, idVideoNameExport);
        if (vsp == null) {
            vsp = new VideoSupportPath(title, pathG, md5, idVideoNameExport);
            vsp.setDateModif(new Date());
        } else {
            vsp.setDateModif(new Date());
        }
        vsp.setActive(true);
        return videoSupportPathRepository.save(vsp);
    }

    public void senddatas(List<String> urls, String nameExportDecode, String login) {
        MyUser myUser = myUserRepository.findByLogin(login);
        // VSP & VNE
        for (String url : urls) {
            MyMediaInfo mmi = senddata(url);
        }
    }

    @Override
    public List<VNELight> lVneIdToName(String login) {
        List<Tuple> lmls = videoNameExportRepository.lVneIdToName(login);
        List<VNELight> lvne = new ArrayList<>();
        for (Tuple t : lmls) {
            lvne.add(new VNELight((Long) t.toArray()[0], (String) t.toArray()[1]));
        }
        return lvne;
    }

    @Override
    public List<VideoKind> getAllKinds(String login) {
        return videoKindRepository.findAll();
    }

    @Override
    public List<UserLight> listUserWithId() {
        List<Tuple> ltul = myUserRepository.lUserWithId();
        List<UserLight> lul = new ArrayList<>();
        for (Tuple t : ltul) {
            lul.add(new UserLight(
                    (Long) t.toArray()[0],
                    (String) t.toArray()[1],
                    (String) t.toArray()[3]));
        }
        return lul;
    }
}
