package fr.gdvd.media_manager.service;


import fr.gdvd.media_manager.daoMysql.*;
import fr.gdvd.media_manager.entitiesMysql.*;
import fr.gdvd.media_manager.entitiesNoDb.FileVideo;
import fr.gdvd.media_manager.entitiesNoDb.StateImport;
import fr.gdvd.media_manager.tools.Parser;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Log4j2
@Service
public class VideoAdminServiceImpl implements VideoAdminService {

    @Autowired
    private MyMediaInfoRepository myMediaInfoRepository;
    @Autowired
    private MyMediaLanguageRepository myMediaLanguageRepository;
    @Autowired
    private MyMediaTextRepository myMediaTextRepository;
    @Autowired
    private MyMediaAudioRepository myMediaAudioRepository;
    @Autowired
    private VideoNameExportRepository videoNameExportRepository;
    @Autowired
    private VideoSupportPathRepository videoSupportPathRepository;
    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private Parser parser;


    /*@Transactional
    @Override
    public StateImport saveData(String pahtdirids, MultipartFile[] file, String loginName) {
        MyUser mu = myUserRepository.findByLogin(loginName);
        StateImport si = new StateImport();
        //For each fileImport
        for (MultipartFile f : file) {
            File fi = convert(f);
            String content = "";
            try {
                content = new String(Files.readAllBytes(Paths.get(fi.getAbsolutePath())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<String> ls = parser.findAllTagsInString("<item>", "</item>", content, false);
            boolean updateExport = false;

            String ne = f.getOriginalFilename().replace(".xml", "");
            VideoNameExport vne = videoNameExportRepository.findByNameExport(ne);
            if (vne == null) {
                updateExport = true;
                vne = new VideoNameExport();
                vne.setNameExport(ne);
                vne.setVideoSupportPaths(new ArrayList<>());
                setVne(mu, vne);
                for (String str : ls) {
                    FileVideo fv = new FileVideo();
                    setfileVideo(fv, str);

                    MyMediaInfo mmi = myMediaInfoRepository.findById(fv.getId()).orElse(null);
                    if (mmi == null) {
                        mmi = new MyMediaInfo();
                        mmi.setIdMyMediaInfo(fv.getId());
                        createMyMediaInfo(mmi, pahtdirids);
                    }
                    VideoSupportPath vsp = new VideoSupportPath(fv.getPath(), mmi, vne);
                    setVsp(vne, vsp);
                }
                vne.setActive(true);
                videoNameExportRepository.save(vne);
            } else {
                List<VideoSupportPath> oldVideoSupportPaths = vne.getVideoSupportPaths();
                setVne(mu, vne);
                for (String str : ls) {
                    FileVideo fv = new FileVideo();
                    setfileVideo(fv, str);
                    MyMediaInfo mmi = myMediaInfoRepository.findById(fv.getId()).orElse(null);
                    if (mmi == null) {
                        mmi = new MyMediaInfo();
                        mmi.setIdMyMediaInfo(fv.getId());
                        createMyMediaInfo(mmi, pahtdirids);
                        VideoSupportPath vsp = new VideoSupportPath(fv.getPath(), mmi, vne);
                        setVsp(vne, vsp);
                        videoSupportPathRepository.save(vsp);
                    } else {
                        VideoSupportPath oldVsp = videoSupportPathRepository
                                .findByPathGeneralAndMyMediaInfoAndVideoNameExport(fv.getPath(), mmi, vne);
                        if (oldVsp == null) {
                            oldVsp = new VideoSupportPath(fv.getPath(), mmi, vne);
                            setVsp(vne, oldVsp);
                            videoSupportPathRepository.save(oldVsp);
                        } else {
                            oldVsp.setDateModif(new Date());
                            if(oldVsp.isActive())oldVsp.setActive(true);
                            videoSupportPathRepository.save(oldVsp);
                            // Delete vsp of oldVideoSupportPaths
                            isVSPExist(oldVideoSupportPaths, mmi, vne);
                        }
                    }
                }
                if (oldVideoSupportPaths.size() != 0) {
                    // **** inactive old vsp *****
                    for(VideoSupportPath v:oldVideoSupportPaths){
                        v.setActive(false);
                        videoSupportPathRepository.save(v);
                    }
                }
                vne.setActive(true);
                videoNameExportRepository.save(vne);
            }
        }
        return si;
    }*/

    private void setVne(MyUser mu, VideoNameExport vne) {
        vne.setDateModifNameExport(new Date());
        vne.setActive(false);// while it's not finish, stay false
        videoNameExportRepository.save(vne);
    }

    private void setVsp(VideoNameExport vne, VideoSupportPath vsp) {
        vsp.setActive(true);
        vsp.setDateModif(new Date());
        vsp.setType("film");
        vsp = videoSupportPathRepository.save(vsp);
        List<VideoSupportPath> videoSupportPaths = vne.getVideoSupportPaths();
        videoSupportPaths.add(vsp);
        vne.setVideoSupportPaths(videoSupportPaths);
    }

    private void isVSPExist(List<VideoSupportPath> oldVideoSupportPaths, MyMediaInfo mmi, VideoNameExport vne) {
        for (VideoSupportPath onevsp : oldVideoSupportPaths) {
            if (onevsp.getMyMediaInfo().getIdMyMediaInfo().equals(mmi.getIdMyMediaInfo())) {
                if (onevsp.getVideoNameExport().getNameExport().equals(vne.getNameExport())) {
                    oldVideoSupportPaths.remove(onevsp);
                }
            }
        }
    }

    private void setfileVideo(FileVideo fv, String str) {
        fv.setId(parser.findTagInString("<id>", "</id>", str, false));
        fv.setPath(parser.findTagInString("<path>", "</path>", str, false));
        fv.setActivate(parser.findTagInString("<active>", "</active>", str, false).equals("1"));
    }

    /*@Transactional
    @Override
    public MyMediaInfo createMyMediaInfo(MyMediaInfo mmi, String pahtdirids) {
        String res = readFileMd5(pahtdirids, mmi.getIdMyMediaInfo());
        if (res.length() != 0) {
            // Parse File.md5
            String general = parser.findTagInString("<track type=\"General\">", "</track>", res, false);
            Map<String, Object> listGeneral = parser.convertXmlToMap(general);
            List<Map<String, Object>> listvideo = parser.xmlToList("VideoCount", "Video", res);
            List<Map<String, Object>> listaudio = parser.xmlToList("AudioCount", "Audio", res);
            List<Map<String, Object>> listtext = parser.xmlToList("TextCount", "Text", res);

            // Create audio
            List<MyMediaAudio> lmma = new ArrayList<>();
            for (Map<String, Object> msa : listaudio) {

                // Informed general
                Double ac = (Double) listGeneral.get("AudioCount");
                if (ac == null) ac = 0.0;
                mmi.setAudioCount(ac.intValue());
                *//*Double vc = (Double) listGeneral.get("VideoCount");
                if (vc == null) vc = 1.0;
                mmi.setVideoCount(vc.intValue());*//*
                Double tc = (Double) listGeneral.get("TextCount");
                if (tc == null) tc = 0.0;
                mmi.setTextCount(tc.intValue());
                Double dr = (Double) listGeneral.get("Duration");
                mmi.setDuration(dr);
                Double fs = (Double) listGeneral.get("FileSize");
                mmi.setFileSize(fs);
                String ft = (String) listGeneral.get("Format");
                mmi.setFormat(ft);
                mmi = myMediaInfoRepository.save(mmi);


                String lg = (String) msa.get("Language");
                if (lg == null) lg = "?";
                MyMediaLanguage mml = myMediaLanguageRepository.findByLanguage(lg);
                if (mml == null) {
                    if (lg.length() > 16) lg = lg.substring(0, 15);
                    mml = myMediaLanguageRepository.save(new MyMediaLanguage(null, lg, null, null));
                }

                MyMediaAudio mma = new MyMediaAudio(mmi, mml);
                Double br = (Double) msa.get("BitRate");
                if (br == null) br = (Double) msa.get("OverallBitRate");
                if (br == null) br = 0.0;
                mma.setBitrate(br);
                Double ch = (Double) msa.get("Channels");
                if (ch == null) ch = 1.0;
                mma.setChannels(ch.intValue());
                dr = (Double) msa.get("Duration");
                if (dr == null) dr = 0.0;
                mma.setDuration(dr);
                String fc = (String) msa.get("Forced");
                if (fc == null) fc = "no";
                mma.setForced(fc.equals("Yes"));
                ft = (String) msa.get("Format");
                if (ft == null) ft = "";
                mma.setFormat(ft);

                lmma.add(myMediaAudioRepository.save(mma));
            }

            // create text
            List<MyMediaText> lmmt = new ArrayList<>();
            for (Map<String, Object> mmmt : listtext) {
                String lg = (String) mmmt.get("Language");
                if (lg == null) lg = "?";
                MyMediaLanguage mml = myMediaLanguageRepository.findByLanguage(lg);
                if (mml == null) {
                    if (lg.length() > 16) lg = lg.substring(0, 15);
                    mml = myMediaLanguageRepository.save(new MyMediaLanguage(null, lg, null, null));
                }
                MyMediaText mmt = new MyMediaText(mmi, mml);
                String cd = (String) mmmt.get("CodecID");
                if (cd == null) cd = "";
                if (cd.length() > 16) cd = cd.substring(0, 15);
                mmt.setCodecId(cd);
                String fc = (String) mmmt.get("Forced");
                if (fc == null) fc = "no";
                mmt.setForced(fc.equals("Yes"));
                String ft = (String) mmmt.get("Format");
                if (ft == null) ft = "";
                mmt.setFormat(ft);
                mmt.setInternal(true);

                lmmt.add(myMediaTextRepository.save(mmt));

            }
            List<MyMediaVideo> lmmv = new ArrayList<>();
            for (Map<String, Object> mmv : listvideo) {
                // Create video
                MyMediaVideo mv = new MyMediaVideo();
                Double bt = (Double) mmv.get("BitRate");
                if (bt == null) bt = (Double) mmv.get("OverallBitRate");
                if (bt == null) bt = (Double) listGeneral.get("OverallBitRate");
                if (bt == null) bt = (Double) listGeneral.get("BitRate");
                if (bt == null) bt = 0.0;
                mv.setBitrate(bt);
                String cd = (String) mmv.get("CodecID");
                if (cd == null) cd = "";
                if (cd.length() > 16) cd = cd.substring(0, 15);
                mv.setCodecId(cd);
                String ft = (String) mmv.get("Format");
                if (ft == null) ft = "";
                mv.setFormat(ft);
                Double he = (Double) mmv.get("Height");
                if (he == null) he = 0.0;
                mv.setHeight(he.intValue());
                Double wi = (Double) mmv.get("Width");
                if (wi == null) wi = 0.0;
                mv.setWidth(wi.intValue());
                mv.setMyMediaInfo(mmi);

                mv = myMediaVideoRepository.save(mv);
                lmmv.add(mv);
            }

            // Add list to mmi : audio, video, text
//            mmi.setMyMediaAudios(lmma);
//            mmi.setMyMediaTexts(lmmt);
            mmi.setMyMediaVideos(lmmv);
            mmi = myMediaInfoRepository.save(mmi);

            return mmi;
        }
        log.error("MediaInfo id : " + mmi.getIdMyMediaInfo() + " failed");
        return null;
    }*/

    private String readFileMd5(String pathFileMd5, String md5) {
        if (!(pathFileMd5.substring(pathFileMd5.length() - 1)).equals("/")) {
            pathFileMd5 = pathFileMd5 + "/";
        }
        pathFileMd5 = pathFileMd5 + md5 + ".txt";
        pathFileMd5 = pathFileMd5.replace(System.getProperty("user.home"), "");
        pathFileMd5 = pathFileMd5.replace("~", "");
        if (!pathFileMd5.substring(0, 1).equals("/")) {
            pathFileMd5 = "/" + pathFileMd5;
        }
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(System.getProperty("user.home") +
                pathFileMd5), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return contentBuilder.toString();
    }

    private void pushToDB(List<Map<String, Object>> llms, String pahtdirids) {
        String npath = pahtdirids.replace("~", "");
//        log.info("PushToDB begin");
        //*** for each MultipartFile.selected
        int llmssize = llms.size();
        int ifile = 1;
        for (Map<String, Object> mp : llms) {
//            log.info("FileImport nb:" + ifile++ + "/" + llmssize);
            String pathGeneral = "";
            List<Map<String, String>> lmss = new ArrayList<>();

            int i = 0;
            //*** For each item (video)
            int mpsize = ((List<FileVideo>) (mp.get("oneexport"))).size();
//            int imp = 0;
            for (FileVideo fv : (List<FileVideo>) mp.get("oneexport")) {
                i++;
                //                System.out.println(j1);
                /*if(i >975) {
                    log.info("FileVideo nb:" + i + "/" + mpsize);
                }*/
                File f = new File(System.getProperty("user.home") + npath + fv.getId() + ".txt");
                String content = "";
                try {
                    content = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("PushToDB end");
    }

    private String getMd5(String s) {
        String res = "";
        res = DigestUtils.md5Hex(s);
        return res;
    }

    private String getMd5b(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    private File convert(MultipartFile file) {
        File convFile = new File(file.getOriginalFilename());
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convFile;
    }
}
