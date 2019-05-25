package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.dao.MediaPathRepository;
import fr.gdvd.media_manager.dao.MediaTitleRepository;
import fr.gdvd.media_manager.dao.MediaVideoRepository;
import fr.gdvd.media_manager.entities.FileVideo;
import fr.gdvd.media_manager.entities.MediaPath;
import fr.gdvd.media_manager.entities.MediaTitle;
import fr.gdvd.media_manager.entities.MediaVideo;
import fr.gdvd.media_manager.tools.Parser;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Log4j2
@Service
public class VideoAdminServiceImpl implements VideoAdminService {

    @Autowired
    private MediaPathRepository mediaPathRepository;
    @Autowired
    private MediaVideoRepository mediaVideoRepository;
    @Autowired
    private MediaTitleRepository mediaTitleRepository;
    @Autowired
    private Parser parser;


    @Override
    public int saveData(String names, MultipartFile[] file) {
        String[] namesTbl = names.split("[/]{3}");
//        log.info("names : "+ String.join(" - ",Arrays.asList(namesTbl)));

        String pahtdirids = "~/MediaVideo/";
        List<Map<String, Object>> llms = new ArrayList<>();
        int i = 0;
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

            Map<String, Object> lms = new HashMap<>();
            lms.put("filenameexport", ((namesTbl[i]).replace(".xml", "")));

            List<FileVideo> mfv = new ArrayList<>();
            for (String str : ls) {
                FileVideo fv = new FileVideo();
                fv.setId(parser.findTagInString("<id>", "</id>", str, false));
                fv.setPath(parser.findTagInString("<path>", "</path>", str, false));
                fv.setActivate(parser.findTagInString("<active>", "</active>", str, false).equals("1"));
                mfv.add(fv);
            }
            lms.put("oneexport", mfv);
            llms.add(lms);
            i++;
        }
        pushToDB(llms, pahtdirids);
        return llms.size();
    }

    private void pushToDB(List<Map<String, Object>> llms, String pahtdirids) {
        String npath = pahtdirids.replace("~", "");
        log.info("PushToDB begin");
        //*** for each MultipartFile.selected
        int llmssize = llms.size();
        int ifile = 1;
        for (Map<String, Object> mp : llms) {
            log.info("FileImport nb:" + ifile++ + "/" + llmssize);
            String pathGeneral = "";
            List<Map<String, String>> lmss = new ArrayList<>();

            //*** Create one entity of MediaPath (one export)
            MediaPath mpv = new MediaPath();
            mpv.setId((String) (new ObjectId().toString()));
            mpv.setActive(false);

            mpv.setNameExport((String) mp.get("filenameexport"));
            List<MediaTitle> mediaTitles = new ArrayList<>();
            mpv.setDateModif(new Date());
            mpv.setType("video");
            List<Map<String, String>> lms = new ArrayList<>();
            Map<String, String> ms = new HashMap<>();
            ms.put("admin", "gilles");
            lms.add(ms);
            mpv.setUsers(lms);

            int i = 0;
            //*** For each item (video)
            int mpsize = ((List<FileVideo>) (mp.get("oneexport"))).size();
//            int imp = 0;
            for (FileVideo fv : (List<FileVideo>) mp.get("oneexport")) {
                i++;
//                int j1=0;
                if(i%10 == 0) {
                    log.info("FileVideo nb:" + i + "/" + mpsize);
                    /*for(int j = 0; j<100000000 ; j++){
                        Random r = new Random();
                        j1 = r.nextInt((10) + 1) + 1;
                    }*/
                }
//                System.out.println(j1);
                if(i >975) {
                    log.info("FileVideo nb:" + i + "/" + mpsize);
                }
                File f = new File(System.getProperty("user.home") + npath + fv.getId() + ".txt");
                String content = "";
                try {
                    content = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String path = parser.findTagInString("<media ref=\"", "\">", content, false);
                String general = parser.findTagInString("<track type=\"General\">", "</track>", content, false);

                List<Map<String, Object>> listvideo = parser.xmlToList("VideoCount", "Video", content);
                List<Map<String, Object>> docsaudio = parser.xmlToList("AudioCount", "Audio", content);
                List<Map<String, Object>> docstext = parser.xmlToList("TextCount", "Text", content);

                String title = "";
                int pos = path.lastIndexOf('/');
                String absolutePath = path;
                if (i == 1) {
                    pathGeneral = path.substring(0, pos);
                }
                if (pos == -1) {
                    title = path;
                    path = "";
                } else if (pos == 0) {
                    title = path.substring(1);
                    path = "";
                } else if (pos>0){
                    title = path.substring(pos+1,path.length());
                    path = path.substring(0, pos);
                }
                if(i>=976){
                    log.info("Stop Here");
                }
                String idMediaTitle = getMd5(absolutePath + "/" + fv.getId());
                ///////////////////////////////////////////////////////////////////////////////////
                ////////// BUG here with file :
                ////////// DD1To-FilmsBase5AndMore+2019-03-28_20-49-52.xml
                ////////// At le 976th fileVideo => bug and memory overflow...
                ////////// MongoRepository or CrudRepository crash
                ////////// while it's search id, at the next line
                ///////////////////////////////////////////////////////////////////////////////////
                MediaTitle mediaTitle = mediaTitleRepository.getById(idMediaTitle) ;
                if(mediaTitle==null){
                    mediaTitle = new MediaTitle();
                    mediaTitle.setId(idMediaTitle);
                    mediaTitle.setActive(true);
                    mediaTitles.add(mediaTitle);
                }
                mediaTitle.setAbsolutePath(path);
                mediaTitle.setTitle(title);
                mediaTitle.setMediaPath(mpv);

                MediaVideo mv = mediaVideoRepository.getById(fv.getId());
                if(mv != null){ //Then update mediaTitles
                    mv.addMediaTitle(mediaTitle);
                }else{ //Else create new MediaVideo
                    mv = new MediaVideo();
                    mv.setId(fv.getId());
                    Map<String, Object> map = new HashMap();
                    mv.setInfo(parser.convertXmlToMapInt(general, map));
                    mv.setVideo(listvideo);
                    mv.setAudio(docsaudio);
                    mv.setText(docstext);
                }

                mediaTitle.setMediaVideo(mv);
                //mv.setMediaTitles(mediaTitle);

                mediaTitleRepository.save(mediaTitle);
                mediaVideoRepository.save(mv);

                Map<String, String> mss = new HashMap<>();
                mss.put("UID", new ObjectId().toString());
                mss.put("MD5", fv.getId());
                mss.put("Path", path);
                mss.put("Title", title);
                lmss.add(mss);
//                Document docGeneral = parser.convertXmlToDocument(general);
            }

            //Save import
            mpv.setPathGeneral(pathGeneral);
            mpv.setMediaTitles(mediaTitles);
            //TODO:set pathGeneral
            mediaPathRepository.save(mpv);
        }
        log.info("PushToDB end");
    }

    private String getMd5(String s){
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
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
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
