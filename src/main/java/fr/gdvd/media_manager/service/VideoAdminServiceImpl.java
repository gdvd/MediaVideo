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


    private void setVne(MyUser mu, VideoNameExport vne) {
        vne.setDateModifNameExport(new Date());
        vne.setActive(false);// while it's not finish, stay false
        videoNameExportRepository.save(vne);
    }

    private void setVsp(VideoNameExport vne, VideoSupportPath vsp) {
        vsp.setActive(true);
        vsp.setDateModif(new Date());
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
        //*** for each MultipartFile.selected
        int llmssize = llms.size();
        int ifile = 1;
        for (Map<String, Object> mp : llms) {
            String pathGeneral = "";
            List<Map<String, String>> lmss = new ArrayList<>();

            int i = 0;
            //*** For each item (video)
            int mpsize = ((List<FileVideo>) (mp.get("oneexport"))).size();
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
