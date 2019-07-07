package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.daoMysql.*;
import fr.gdvd.media_manager.entitiesMysql.*;
import fr.gdvd.media_manager.entitiesNoDb.MediaInfoLight;
import fr.gdvd.media_manager.entitiesNoDb.ScanMessage;
import fr.gdvd.media_manager.entitiesNoDb.VNELight;
import fr.gdvd.media_manager.tools.Parser;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.*;
import java.util.*;

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
    private MyMediaInfoRepositoryPage myMediaInfoRepositoryPage;
//    @Autowired
//    private MediaInfoLightRepository mediaInfoLightRepository;


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

        Pageable findAll = PageRequest.of(page, pos);
        Pageable findAll2 = PageRequest.of(page, pos, Sort.by("fileSize"));
        Page p = myMediaInfoRepositoryPage.findAll(findAll2);

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
        return videoSupportPathRepository.findMyVspPP(login, filter, pageable);
    }

    @Override
    public Page<MyMediaInfo> listMmiForLoginPP(String login, int page,
                                               int size, String toSort,
                                               String filter) {
        Pageable pageable = PageRequest.of(page, size);/*, Sort.by(toSort)*/
        Page p = myMediaInfoRepository.findMmiPP(login, filter, pageable);

        return p;
    }

    @Override
    public void addnewext(String ext) {
        if(ext.length()>1 && ext.length()<10){
            Preferences pref = preferencesRepository.findById("01").orElse(null);
            if(pref==null) throw new RuntimeException("Preferences doesn't exist");
            pref.getExtset().add(ext);
            preferencesRepository.save(pref);
        }else throw new RuntimeException("Extension must be : 1 < extension < 10");
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
    public List<VideoSupportPath> getVneToAllPath(String login, String filtre) {
        return videoSupportPathRepository.findMyVsp(login);
    }

    @Override
    public List<VideoSupportPath> listVspForLoginWithFilter(String login, String filter) {
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
                String mediaInfo = parser.readMediaInfo(url);
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
            String title = lstr.get(lstr.size()-1);

            String pathG = "";
            for(int lsi = 0; lsi < lstr.size()-1; lsi++){
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
        return videoNameExportRepository.findMyVne(login);
    }

    @Override
    public VideoNameExport storemmi(List<MyMediaInfo> lmmi, int idvne, int idVneRemote) {
        VideoNameExport vne = videoNameExportRepository.findByIdVideoNameExport((long) idvne);
        for (MyMediaInfo mmiremote : lmmi) {
            MyMediaInfo mmi = myMediaInfoRepository.findById(mmiremote.getIdMyMediaInfo()).orElse(null);
            if (mmi == null) {
                mmi = new MyMediaInfo();
                mmi.setIdMyMediaInfo(mmiremote.getIdMyMediaInfo());
                mmi.setBitrate(mmiremote.getBitrate());
                mmi.setCodecId(mmiremote.getCodecId());
                mmi.setDuration(mmiremote.getDuration());
                mmi.setFileSize(mmiremote.getFileSize());
                mmi.setFormat(mmiremote.getFormat());
                mmi.setHeight(mmiremote.getHeight());
                mmi.setTextCount(mmiremote.getTextCount());
                mmi.setWidth(mmiremote.getWidth());
                mmi.setVideoFilm(mmiremote.getVideoFilm());
                mmi = myMediaInfoRepository.save(mmi);

                for(MyMediaComment mmcremote: mmiremote.getMyMediaComments()){
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
            }// MMI ok -> Create VSP if din't exist
            for (VideoSupportPath vspremote : mmiremote.getVideoSupportPaths()) {
                if (vspremote.getId().getIdVideoNameExport() == idVneRemote) {
                    String pathGen = vspremote.getId().getPathGeneral();
                    String title = vspremote.getId().getTitle();
                    VideoSupportPath vsp = videoSupportPathRepository
                            .findByTitleAndPathGeneralAndMyMediaInfo_IdMyMediaInfoAndVideoNameExport_IdVideoNameExport(
                                    title,  pathGen, mmi.getIdMyMediaInfo(), (long) idvne);
                    if (vsp == null) {
                        vsp = new VideoSupportPath(title, pathGen, mmi.getIdMyMediaInfo(), (long) idvne);
                        vsp.setType(vspremote.getType());
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

//    @Override
//    public List<MediaInfoLight> findAllMediaInfoLight() {
//        return mediaInfoLightRepository.findAll();
//    }

    @Override
    public MyMediaInfo createMmiAndGetMd5(String pathGeneral) {
        String md5 = setMd5(pathGeneral);
        MyMediaInfo mmi = myMediaInfoRepository.findById(md5).orElse(null);
        if (mmi == null) {
            mmi = new MyMediaInfo();
            mmi.setIdMyMediaInfo(md5);
            String mediaInfoStr = parser.readMediaInfo(pathGeneral);
            parser.createMyMediaInfo(mmi, mediaInfoStr);
        }
        // recall mmi to have videoList
        return myMediaInfoRepository.findById(md5).orElse(null);
    }

    @Override
    public VideoSupportPath createVSP(Long idVideoNameExport, String md5, String pathGeneral) {
        String path[] = pathGeneral.split("/");
        List<String> lstr = Arrays.asList(path);
        String title = lstr.get(lstr.size()-1);

        String pathG = "";
        for(int lsi = 0; lsi < lstr.size()-1; lsi++){
            pathG = pathG + lstr.get(lsi) + "/";
        }

        VideoSupportPath vsp = videoSupportPathRepository
                .findByTitleAndPathGeneralAndMyMediaInfo_IdMyMediaInfoAndVideoNameExport_IdVideoNameExport(
                        title, pathG, md5, idVideoNameExport);
        if (vsp == null) {
            vsp = new VideoSupportPath(title, pathG, md5, idVideoNameExport);
            vsp.setDateModif(new Date());
            vsp.setType("Film");
        } else {
            vsp.setDateModif(new Date());
        }
        vsp.setActive(true);
        return videoSupportPathRepository.save(vsp);
    }

    public void senddatas(List<String> urls, String nameExportDecode, String login) {
        /*log.info("***** Receive datas : " + urls.size()
                + " nameExportDecode : " + nameExportDecode
                + " login : " + login);*/
        MyUser myUser = myUserRepository.findByLogin(login);
        // VSP & VNE
        for (String url : urls) {
            MyMediaInfo mmi = senddata(url);
        }
    }

    @Override
    public List<VNELight> lVneIdToName(String login) {
        List<Tuple> lmls = videoNameExportRepository.lVneIdToName(login);
//        Map<Long, String> mls = new HashMap<>();
        List<VNELight> lvne = new ArrayList<>();
        for(Tuple t:lmls){
//            mls.put((Long) t.toArray()[0], (String) t.toArray()[1]);
            lvne.add(new VNELight((Long) t.toArray()[0], (String) t.toArray()[1]));
        }
        return lvne;
    }
}
