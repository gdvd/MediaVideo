package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.daoMysql.MyMediaInfoRepository;
import fr.gdvd.media_manager.daoMysql.TypeMmiRepository;
import fr.gdvd.media_manager.daoMysql.TypeNameRepository;
import fr.gdvd.media_manager.daoMysql.VideoFilmRepository;
import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesMysql.TypeMmi;
import fr.gdvd.media_manager.entitiesMysql.TypeName;
import fr.gdvd.media_manager.entitiesMysql.VideoFilm;
import fr.gdvd.media_manager.entitiesNoDb.RequestImdb;
import fr.gdvd.media_manager.tools.Download;
import fr.gdvd.media_manager.tools.Parser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

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

    @Override
    public VideoFilm linkIdttWithIdmmi(String idmmi, String idtt) {
        MyMediaInfo mmi = myMediaInfoRepository.findById(idmmi).orElse(null);
        if (mmi == null) throw new RuntimeException("This MyMediaInfo doesn't exist.");
        VideoFilm vf = videoFilmRepository.findById(idtt).orElse(null);
        if (vf == null) throw new RuntimeException("This VideoFilm doesn't exist.");
        TypeMmi tmmi = mmi.getTypeMmi();
        TypeMmi tmVf = typeMmiRepository.findByVideoFilm(vf);
        if (tmmi == null) {  //No link in mmi
            if (tmVf == null) {// No link in videofilm
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
//                myMediaInfoRepository.save(mmi);
            }else{
                mmi.setTypeMmi(tmVf);
            }
            myMediaInfoRepository.save(mmi);
        } else {
            if (tmVf == null) {
                tmmi.setVideoFilm(vf);
                typeMmiRepository.save(tmmi);
            }else{
                tmmi.setVideoFilm(vf);
                typeMmiRepository.save(tmmi);
            }

        }
        return vf;
    }

    @Override
    public TypeMmi savetypemmi(TypeMmi typeMmi, String idMmi) {
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
        typeMmi = typeMmiRepository.save(typeMmi);
        MyMediaInfo mmi = myMediaInfoRepository.findByIdMyMediaInfo(idMmi);
        if (mmi != null) {
            mmi.setTypeMmi(typeMmi);
            mmi.setDateModif(new Date());
            mmi = myMediaInfoRepository.save(mmi);
        }else{
            throw new RuntimeException("This MMI doesn't exist");
        }
        return typeMmi!=null&&mmi!=null? typeMmi : null;
    }

    @Override
    public TypeMmi gettypemmiwithidtt(String idtt) {
        VideoFilm vf = videoFilmRepository.findById(idtt).orElse(null);
        if(vf==null)throw new RuntimeException("VideoFilm doesn't exist");
        return typeMmiRepository.findByVideoFilm(vf);
    }

    @Override
    public List<RequestImdb> getResultRequestWeb(String query) {
        List<RequestImdb> lri = new ArrayList<>();
        query = query.replaceAll("[^\\w\\s\\']+", " ");
        query = query.replace("^[\\s]*", "").trim();
        query = query.replaceAll("[\\s]+", " ");
        String encodedQuery = encodeValue(query);
        String completeUrl = "https://www.imdb.com/find?ref_=nv_sr_fn&q=" + encodedQuery + "&s=all";

        URL url = null;
        File f = null;
        String res = "";
        try {
            url = new URL(completeUrl);
            f = new File(System.getProperty("user.home") + "/pathIdVideo/search/searchtitles.xml");
            res = download.download2String(url, f.getAbsolutePath());
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
                        boolean bool = videoFilmRepository.ifTtExist(linkToIdTt(linktt));
                        ri.setState(bool);

                        lri.add(ri);
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
    public VideoFilm getOneVideoFilm(String link) {
        String idtt = linkToIdTt(link);
        if (idtt.equals("")) throw new RuntimeException("link incorrect");

        // search in db
        VideoFilm vf = videoFilmRepository.findById(idtt).orElse(null);
        if (vf != null) return vf;

        // search in file system or internet
        File f = downloaWebPage(idtt);
        if (f == null) throw new RuntimeException("File ttxxxxxxx is empty");

        // copy the file to string
        String toParse = fileToString(f);

        // parse data and push to db
        vf = new VideoFilm();
        vf.setIdVideo(idtt);
        vf = parser.createOneVideoFilm(vf, toParse);

        return vf;
    }


    @Override
    public File downloaWebPage(String idtt) {
        URL url = null;
        File f = null;
        int response = 0;
        try {
            url = new URL("https://www.imdb.com/title/" + idtt + "/reference");
            f = new File(System.getProperty("user.home") + "/pathIdVideo/idtt/" + idtt + ".html");
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
                if (Pattern.matches("tt[\\d]{7,9}", str)) {
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
            if (Pattern.matches("tt[\\d]{7}", s)) {// 'tt' and 7 digits maybe 8 or 9
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
}
