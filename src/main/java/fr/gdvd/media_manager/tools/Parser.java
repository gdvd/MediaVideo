package fr.gdvd.media_manager.tools;

import fr.gdvd.media_manager.daoMysql.*;
import fr.gdvd.media_manager.entitiesMysql.*;
import fr.gdvd.media_manager.entitiesNoDb.ScanMessage;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Double.parseDouble;

@Service
@Log4j2
public class Parser {

    @Autowired
    private MyMediaInfoRepository myMediaInfoRepository;
    @Autowired
    private MyMediaLanguageRepository myMediaLanguageRepository;
    @Autowired
    private MyMediaAudioRepository myMediaAudioRepository;
    @Autowired
    private MyMediaTextRepository myMediaTextRepository;
    @Autowired
    private VideoPosterRepository videoPosterRepository;
    @Autowired
    private VideoArtistRepository videoArtistRepository;
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
    private TypeNameRepository typeNameRepository;
    @Autowired
    private TypeMmiRepository typeMmiRepository;
    @Autowired
    private PreferencesRepository preferencesRepository;
    @Autowired
    private VideoMoreInformationRepository videoMoreInformationRepository;
    @Autowired
    private VideoSupportPathRepository videoSupportPathRepository;



//############################## Parser ##################################
//########################################################################
//###################### Parser version 2019-08-04 #######################
//########################################################################
    public String findTagInString(String mcd, String mcf, String toParserA, boolean withTag) {

        List<char[]> tags = new ArrayList();
        tags.add("<div *>".toCharArray());
        tags.add("</div>".toCharArray());
        tags.add("<div>".toCharArray());
        tags.add("</div>".toCharArray());
        /*tags.add("<section*>".toCharArray());
        tags.add("</section>".toCharArray());
        tags.add("<section>".toCharArray());
        tags.add("</section>".toCharArray());*/

//        List<char[]> tags = new ArrayList();
        String str = "";
        // __________________________________________________
        char e = '*';
        char[] motClefDebut = mcd.toCharArray();
        int sizeMotClefDebut = motClefDebut.length;
        char[] motClefFin = mcf.toCharArray();
        int sizeMotClefFin = motClefFin.length;
        String toParser = toParserA;
        char[] toPars = toParser.toCharArray();
        int sizeToPars = toParser.length();

        tags.add(motClefDebut);
        tags.add(motClefFin);

        List<char[]> listMCF = new ArrayList<char[]>();
        int pos = -1;
        int deep = 0;
        int posDebut = 0;
        int posFin = 0;
        int posEcritureDebut = sizeToPars - 1;
        int posEcritureFin = sizeToPars - 1;
        int wclong = 1;

        int charMcd = 0;
        int charMcf = 0;
        boolean mCInclus = withTag;

        // ******* Parcour de la chaine
        while (pos < (sizeToPars - 1)) {
            pos++;
            wclong = 0;
            posDebut = pos;
            posFin = pos;
            // *** Recherche d'un motClefDebut
            for (int i = 0; i < tags.size(); i += 2) {
                if (deep == 0) {
                    i = tags.size() - 2;
                }
                charMcd = 0;
                motClefDebut = tags.get(i);
                sizeMotClefDebut = motClefDebut.length;
                while ((int) toPars[posDebut] == (int) motClefDebut[charMcd]) {
                    posDebut++;
                    charMcd++;
                    // MCD Sans wildcard
                    if (charMcd == (sizeMotClefDebut)) {

                        pos = posDebut;
                        listMCF.add(tags.get(i + 1));
                        if (deep == 0) {
                            if (mCInclus) {
                                posEcritureDebut = posDebut - sizeMotClefDebut;
                            } else {
                                posEcritureDebut = posDebut;
                            }
                        }
                        deep++;
                        //pos--;
                        i = tags.size();
                        break;
                    }
                    if (charMcd < (sizeMotClefDebut - 1)) {
                        // MCD avec wildcard
                        if (motClefDebut[charMcd] == e) {
                            if ((charMcd + 1) < sizeMotClefDebut) {
                                wclong = 0;
                                while (toPars[posDebut + wclong] != (int) motClefDebut[charMcd + 1]) {
                                    if ((charMcd + wclong) < sizeToPars || wclong > 500) {
                                        wclong++;
                                    } else {
                                        break;
                                    }
                                }
                                if (wclong < 500) {
                                    listMCF.add(tags.get(i + 1));
                                    if (deep == 0) {
                                        if (mCInclus) {
                                            posEcritureDebut = posDebut - sizeMotClefDebut + 3;
                                        } else {
                                            posEcritureDebut = posDebut + wclong + 1;
                                        }
                                    }
                                    pos = posDebut + wclong + 1;//
                                    deep++;
                                    //pos--;
                                    i = tags.size();
                                    break;
                                }
                                break;
                            }
                        }
                    }
                }
                // *** Recherche du motClefFin
                posFin = pos;
                if (deep != 0) {
                    motClefFin = listMCF.get(listMCF.size() - 1);
                    sizeMotClefFin = motClefFin.length;
                    if (posFin + sizeMotClefFin <= sizeToPars) {
                        charMcf = 0;
                        while ((int) toPars[posFin] == (int) motClefFin[charMcf]) {
                            charMcf++;
                            posFin++;
                            if (sizeMotClefFin == 1) {
                                deep--;
                                listMCF.remove(listMCF.size() - 1);
                                pos = posFin;
                                if (deep == 0) {
                                    if (mCInclus) {
                                        posEcritureFin = posFin;
                                    } else {
                                        posEcritureFin = posFin - sizeMotClefFin;
                                    }
                                    str = toParserA.substring(posEcritureDebut, posEcritureFin);
                                    posEcritureDebut = posFin - 1;
                                    pos = posFin - 1;
                                }
                                break;
                            } else {
                                if (charMcf >= (sizeMotClefFin)) {
                                    deep--;
                                    listMCF.remove(listMCF.size() - 1);
                                    pos = posFin - 1;
                                    if (deep == 0) {
                                        if (mCInclus) {
                                            posEcritureFin = posFin;
                                        } else {
                                            posEcritureFin = posFin - sizeMotClefFin;
                                        }
                                        str = toParser.substring(posEcritureDebut, posEcritureFin);// PB ICI ******************
                                        pos = sizeToPars;
                                        break;
                                    }
                                    break;
                                }
                            }

                        }
                    }
                }
            }
        }
        // __________________________________________________
        return str;
    }

//########################################################################
//###################### Parser version 2019-08-04 #######################
//########################################################################
    public List<String> findAllTagsInString(String mcd, String mcf, String strEntre, boolean withTag) {

        List<char[]> tags = new ArrayList();
        tags.add("<div *>".toCharArray());
        tags.add("</div>".toCharArray());
        tags.add("<div>".toCharArray());
        tags.add("</div>".toCharArray());

        String str = "";
        char e = '*';
        char[] motClefDebut = mcd.toCharArray();
        int sizeMotClefDebut = motClefDebut.length;
        char[] motClefFin = mcf.toCharArray();
        int sizeMotClefFin = motClefFin.length;

        tags.add(motClefDebut);
        tags.add(motClefFin);

        List<char[]> listMCF = new ArrayList<char[]>();
        int pos = -1;
        int deep = 0;
        int posDebut = 0;
        int posFin = 0;
        int posEcritureDebut = -1;
        int posEcritureFin = -1;
        char[] toPars = strEntre.toCharArray();
        int sizeToPars = strEntre.length();

        int wclong = 1;
        List<String> listStr = new ArrayList<String>();
        int charMcd = 0;
        int charMcf = 0;
        boolean mCInclus = withTag;
        while (pos < (sizeToPars - 1)) {

            posEcritureFin = sizeToPars;
            wclong = 0;

            pos++;
            posDebut = pos;
            posFin = pos;
            // *** Recherche d'un MotClefDebut
            for (int i = 0; i < tags.size(); i += 2) {
                if (deep == 0) {
                    i = tags.size() - 2;
                } else {
                    posDebut = pos;
                }
                charMcd = 0;
                motClefDebut = tags.get(i);
                sizeMotClefDebut = motClefDebut.length;
                while ((int) toPars[posDebut] == (int) motClefDebut[charMcd]) {
                    posDebut++;
                    charMcd++;
                    // MCD Sans wildcard
                    if (charMcd == (sizeMotClefDebut)) {
                        pos = posDebut + 0;
                        listMCF.add(tags.get(i + 1));
                        if (deep == 0) {
                            if (mCInclus) {
                                posEcritureDebut = posDebut - sizeMotClefDebut;
                            } else {
                                posEcritureDebut = posDebut;
                            }
                        }
                        // posEcritureFin = sizeToPars;
                        deep++;
                        i = tags.size();
                        break;
                    }
                    if (charMcd < (sizeMotClefDebut - 1)) {
                        // MCD avec wildcard
                        if (motClefDebut[charMcd] == e) {
                            if ((charMcd + 1) < sizeMotClefDebut) {
                                wclong = 0;
                                while (toPars[posDebut + wclong] != (int) motClefDebut[charMcd + 1]) {
                                    if ((charMcd + wclong) < sizeToPars || wclong > 500) {
                                        wclong++;
                                    } else {
                                        break;
                                    }
                                }
                                if (wclong < 500) {
                                    listMCF.add(tags.get(i + 1));
                                    if (deep == 0) {
                                        if (mCInclus) {
                                            posEcritureDebut = posDebut - sizeMotClefDebut + 3;
                                        } else {
                                            posEcritureDebut = posDebut + wclong + 1;
                                        }
                                    }
                                    pos = posDebut + wclong + 1;//
                                    posEcritureFin = sizeToPars;
                                    deep++;
                                    i = tags.size();
                                    break;
                                }

                                break;
                            }
                        }
                    }
                }
            }
            // *** Recherche du MotClefFin
            posFin = pos;
            if (deep != 0) {
                motClefFin = listMCF.get(listMCF.size() - 1);
                sizeMotClefFin = motClefFin.length;
                if (posFin + sizeMotClefFin <= sizeToPars) {
                    charMcf = 0;
                    // MCF sans wildcard
                    while ((int) toPars[posFin] == (int) motClefFin[charMcf]) {
                        charMcf++;
                        posFin++;
                        if (sizeMotClefFin == 1) {
                            deep--;
                            listMCF.remove(listMCF.size() - 1);
                            pos = posFin;
                            if (deep == 0) {
                                if (mCInclus) {
                                    posEcritureFin = posFin;
                                } else {
                                    posEcritureFin = posFin - sizeMotClefFin;
                                }
                                str = strEntre.substring(posEcritureDebut, posEcritureFin);
                                posEcritureDebut = posFin - 1;
                                pos = posFin - 1;
                                listStr.add(str);
                                str = "";
                            }
                            break;
                        } else {
                            if (charMcf >= (sizeMotClefFin)) {
                                deep--;
                                listMCF.remove(listMCF.size() - 1);
                                pos = posFin - 1;

                                if (deep == 0) {
                                    if (mCInclus) {
                                        posEcritureFin = posFin;
                                    } else {
                                        posEcritureFin = posFin - sizeMotClefFin;
                                    }
                                    str = strEntre.substring(posEcritureDebut, posEcritureFin);
                                    posEcritureDebut = posFin - 1;
                                    pos = posFin - 1;
                                    listStr.add(str);
                                    str = "";
                                }
                                break;
                            }
                        }

                    }
                }
            }
        }
        return listStr;
    }

    //#######################################################################
    public Map<String, Object> convertXmlToMap(String parse) {
        Map<String, Object> map = new HashMap();
        return convertXmlToMapInt(parse, map);
    }

    public Map<String, Object> convertXmlToMapInt(String topars, Map<String, Object> map) {
        // Variables
        String keywordBegin = "";
        String keywordEnd = "";
        char cardeb = '<';
        char carfin = '>';
        char echap = '/';
        int pos = -1;
        int posDebut = 0;
        String toparser = topars;
        int lengthToPars = toparser.length();
        int keywordEndLength = 0;
        boolean tagfound;
        boolean ifOneTagIsFound = false;

        //while it's not the end of the stringToPars
        while (pos < (lengthToPars - 3)) {
            tagfound = false;
            pos++;
            // If's the beginning of a tag
            if (toparser.charAt(pos) == cardeb &&
                    toparser.charAt(pos + 1) != echap &&
                    toparser.charAt(pos + 1) != carfin) {
                // Memorize the beginning
                posDebut = pos++;
                //while it's not the end of the stringToPars
                while (pos < lengthToPars - 2) {
                    pos++;
                    // If the end of tag is found
                    if (toparser.charAt(pos) == carfin) {
                        keywordBegin = toparser.substring(++posDebut, pos);
                        keywordBegin = keywordBegin
                                .replaceAll("\\n", "")
                                .replaceAll("[\\.]", "-")
                                .replaceAll("\\s+", " ");
                        int keywordBeginLength = keywordBegin.length();
                        String keywordBeginTest[] = keywordBegin.split(" ");
                        keywordEnd = keywordBeginTest[0];
                        keywordEndLength = keywordEnd.length();
                        while (pos + keywordEndLength + 3 <= lengthToPars - 1) {
                            pos++;
                            // If the tag's end is found
                            if (toparser.charAt(pos) == cardeb &&
                                    toparser.charAt(pos + 1) == echap &&
                                    toparser.substring(pos + 2, pos + 2 + keywordEndLength)
                                            .equals(keywordEnd) &&
                                    toparser.charAt(pos + 2 + keywordEndLength) == carfin
                            ) {
                                String toparserTmp = toparser.substring(posDebut + keywordBeginLength + 1, pos);
                                pos = pos + 1 + keywordEndLength;
                                Map<String, Object> mapTmpOut = new HashMap<>();
                                Map<String, Object> mapTmpIn = new HashMap<>();
                                if (toparserTmp.length() >= 7) {
                                    mapTmpIn = convertXmlToMapInt(toparserTmp, mapTmpOut);
                                    if (mapTmpIn.size() == 0) {
                                        map.put(keywordBegin, dataType(toparserTmp));
                                    } else {
                                        map.put(keywordBegin, mapTmpIn);
                                    }
                                } else {
                                    map.put(keywordBegin, dataType(toparserTmp));
                                }
                                tagfound = true;
                                ifOneTagIsFound = true;
                                break;
                            } else {
                                if (pos + keywordEndLength + 2 == lengthToPars - 1) {
                                    pos = posDebut + keywordBeginLength + 1;
                                    tagfound = true;
                                    break;
                                }
                            }
                        }
                        if (tagfound) break;
                    }
                }
            }
        }
        return map;
    }

    private Object dataType(String keyword) {
        Object ob = null;

        // Identify double
        Pattern pattern = Pattern.compile("(^[+-]?(-?\\d+\\.)?\\d+)$");
        Matcher matcher = pattern.matcher(keyword);
        if (matcher.find()) {
            ob = (Double) parseDouble(keyword);
        } else {
            // Identify Date UTC BUT Create sometimes some bugs
            /*pattern = Pattern
                    .compile("UTC\\s[0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2}$");
            matcher = pattern.matcher(keyword);
            if (matcher.find()) {
                try {
                    DateFormat df = new SimpleDateFormat("z yyyy-MM-dd HH:mm:ss");
                    ob = df.parse(keyword);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                // Identify Date
                pattern = Pattern.compile("^[0-9]{4}(-|/)[0-9]{2}(-|/)[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2}$");
                matcher = pattern.matcher(keyword);
                if (matcher.find()) {
                    try {
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        ob = df.parse(keyword);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    ob = keyword;
                    // Else, it' will be a String
                }
            }*/
            ob = keyword;
        }

        return ob;
    }

    public List<Map<String, Object>> xmlToList(String mcNbItem, String domain, String mediaInfo) {
        Map map = new HashMap();
        List<String> vid = findInfoByDomain(mcNbItem, domain, mediaInfo);
        List<Map<String, Object>> listF = new ArrayList<>();
        if (vid != null) {
            List<String> list0 = vid;
            for (int i = 1; i <= list0.size(); i++) {
                listF.add(convertXmlToMap(list0.get(i - 1)));
            }
            return listF;// If there's one or some track(s)
        } else {
            listF.add(map);
        }
        return listF;//If there's no track
    }

    //############################## Nb of track : video audio and text ################
    private List<String> findInfoByDomain(String mcNbItem, String domain, String infosMediaXml) {
        String nbStr = findTagInString("<" + mcNbItem + ">"
                , "</" + mcNbItem + ">", infosMediaXml, false);
        List<String> res = new ArrayList<>();
        if (nbStr.length() != 0) {
            int nbv = Integer.parseInt(nbStr);
            if (nbv == 1) {
                res.add(findTagInString("<track type=\"" + domain + "\">"
                        , "</track>", infosMediaXml, false));

            } else if (nbv > 1) {
                for (int i = 1; i <= nbv; i++) {
                    String tagin = "<track type=\"" + domain + "\" typeorder=\"" + i + "\">";
                    res.add(findTagInString(tagin, "</track>", infosMediaXml, false));
                }
            }
        } else {
            res.add("");
        }
        return res;
    }

    public List<String> listAllDirectories(ScanMessage sm) {
        List<String> files = new ArrayList<>();
        listDirectory(sm.getPathVideo(), sm);
        return files;
    }

    private void listDirectory(String dirTmp, ScanMessage sm) {
        File file = new File(dirTmp);
        File[] fs = file.listFiles();
        if (fs != null) {
            for (File f : fs) {
                if (f.length() > sm.getMinSizeOfVideoFile() && !f.isDirectory()) {
                    String ext = FilenameUtils.getExtension(f.getName());
                    if (sm.getExtentionsRead().contains(ext.toLowerCase())) {
                        List<String> ls = (sm.getFilesRead());
                        ls.add(f.getAbsolutePath());
                        sm.setFilesRead(ls);
                    } else {
                        if (!sm.getExtentionsNotRead().contains(ext.toLowerCase())) {
                            List<String> ls = sm.getExtentionsNotRead();
                            ls.add(ext);
                            sm.setExtentionsNotRead(ls);
                        }
                    }
                } else {
                    if (f.isDirectory()) {
                        String newDir = f.getAbsolutePath();
                        listDirectory(newDir, sm);
                    }
                }

            }
        }
    }
    public List<File> getAllFilesTitles(String pathTitles){
        List<File> listFile = new ArrayList<>();
        File file = new File(System.getProperty("user.home") + pathTitles);
        File[] fs = file.listFiles();
        if (fs != null) {
            listFile.addAll(Arrays.asList(fs));
        }
        return listFile;
    }

    public String readMediaInfo(String url) {
        Process p;
        String resulta = "";
        StringBuffer output = new StringBuffer();
        String[] commande = {"mediainfo", "--Output=XML", url};
        try {
            p = Runtime.getRuntime().exec(commande);
//            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            resulta = output.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resulta;
    }

    public MyMediaInfo createMyMediaInfo(MyMediaInfo mmi, String mediaInfo) {
        if (mediaInfo.length() != 0) {
            // Parse File.md5
            String general = findTagInString("<track type=\"General\">", "</track>", mediaInfo, false);
            Map<String, Object> listGeneral = convertXmlToMap(general);
            List<Map<String, Object>> listvideo = xmlToList("VideoCount", "Video", mediaInfo);
            List<Map<String, Object>> listaudio = xmlToList("AudioCount", "Audio", mediaInfo);
            List<Map<String, Object>> listtext = xmlToList("TextCount", "Text", mediaInfo);

            Double dr = (Double) listGeneral.get("Duration");
            mmi.setDuration(dr);
            Double fs = (Double) listGeneral.get("FileSize");
            mmi.setFileSize(fs);
            String ft = (String) listGeneral.get("Format");
            if (ft == null) ft = "?";
            if (ft.length() > 16) ft = ft.substring(0, 16);
            mmi.setFormat(ft);
            mmi.setDateModif(new Date());
            mmi = myMediaInfoRepository.save(mmi);

            /* Without TableVideo */
            Map<String, Object> mmv = listvideo.get(0);
            if (mmv.get("CodecID") != null) {
                if (mmv.get("CodecID").getClass().getName().contains("Double")) {
                    Double dbl = (Double) mmv.get("CodecID");
                    mmi.setCodecId(dbl.toString());
                } else {
                    String cd = (String) mmv.get("CodecID");
                    if (cd == null) cd = "";
                    if (cd.length() > 16) cd = cd.substring(0, 15);
                    mmi.setCodecId(cd);
                }
            } else {
                mmi.setCodecId("");
            }
            Double bt = (Double) mmv.get("BitRate");
            if (bt == null) bt = (Double) mmv.get("OverallBitRate");
            if (bt == null) bt = (Double) listGeneral.get("OverallBitRate");
            if (bt == null) bt = (Double) listGeneral.get("BitRate");
            if (bt == null) bt = 0.0;
            mmi.setBitrate(bt);

            Double wi = (Double) mmv.get("Width");
            if (wi == null) wi = 0.0;
            mmi.setWidth(wi.intValue());
            Double he = (Double) mmv.get("Height");
            if (he == null) he = 0.0;
            mmi.setHeight(he.intValue());

            // Create audio
            List<MyMediaAudio> lmma = new ArrayList<>();
            for (Map<String, Object> msa : listaudio) {
                if (msa.size() == 0) break;
                String lg = (String) msa.get("Language");
                if (lg == null) lg = "?";
                if (lg.length() > 16) lg = lg.substring(0, 15);
                MyMediaLanguage mml = myMediaLanguageRepository.findByLanguage(lg);
                if (mml == null) {
                    mml = myMediaLanguageRepository.save(new MyMediaLanguage(null, lg, null, null));
                }
                MyMediaAudio mma = new MyMediaAudio(mmi, mml);

                Double br = 0.0;
                if (msa.get("BitRate") != null) {
                    if (msa.get("BitRate").getClass().getName().contains("String")) {
                        String c = (String) msa.get("BitRate");
                        c.replace("[\\D]*", "");
                        br = (Double) Double.parseDouble(c);
                    } else {
                        br = (Double) msa.get("BitRate");
                        if (br == null) br = (Double) msa.get("OverallBitRate");
                        if (br == null) br = 0.0;
                    }
                } else {
                    br = 0.0;
                }
                mma.setBitrate(br);
                Double ch = 0.0;
                if (msa.get("Channels") != null) {
                    if (msa.get("Channels").getClass().getName().contains("String")) {
                        String c = (String) msa.get("Channels");
                        c.replace("[\\D]*", "");
                        ch = (Double) Double.parseDouble(c);

                    } else {
                        ch = (Double) msa.get("Channels");
                    }
                } else {
                    ch = 1.0;
                }
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
                if (mmmt.size() == 0) break;
                String lg = (String) mmmt.get("Language");
                if (lg == null) lg = "?";
                if (lg.length() > 16) lg = lg.substring(0, 15);
                MyMediaLanguage mml = myMediaLanguageRepository.findByLanguage(lg);
                if (mml == null) {
                    mml = myMediaLanguageRepository.save(new MyMediaLanguage(null, lg, null, null));
                }
                MyMediaText mmt = new MyMediaText(mmi, mml);
                if (mmmt.get("CodecID") != null) {
                    if (mmmt.get("CodecID").getClass().getName().contains("Double")) {
                        Double dbl = (Double) mmmt.get("CodecID");
                        mmt.setCodecId(dbl.toString());
                    } else {
                        String cd = (String) mmmt.get("CodecID");
                        if (cd == null) cd = "";
                        if (cd.length() > 16) cd = cd.substring(0, 15);
                        mmt.setCodecId(cd);
                    }
                }
                mmt.setCodecId("");
                String fc = (String) mmmt.get("Forced");
                if (fc == null) fc = "no";
                mmt.setForced(fc.equals("Yes"));
                ft = (String) mmmt.get("Format");
                if (ft == null) ft = "";
                mmt.setFormat(ft);
                mmt.setInternal(true);
                lmmt.add(myMediaTextRepository.save(mmt));
            }
//            mmi.setMyMediaVideos(lmmv);
            mmi = myMediaInfoRepository.save(mmi);
            return mmi;
        }
        log.error("MediaInfo id : " + mmi.getIdMyMediaInfo() + " failed");
        return null;
    }

    private int getYear4VideoFilm(String partHeader1) {
        String releaseinfo = findTagInString("releaseinfo\">", "</a>", partHeader1, false);
        String releaseinfotmp = releaseinfo.replaceAll("[^\\s\\d]", "");
        releaseinfotmp = releaseinfotmp.replaceAll("^[\\s]*", "").trim();
        int year = 0;
        for (String part : releaseinfotmp.split(" "))
            if (Pattern.matches("[\\d]{4}", part)) {
                year = Integer.parseInt(part);
            }
        return year;
    }

    private void geturlImgIfExist4VideoFilm(VideoFilm vf, String imgitemprop) {
        String urlImg = findTagInString("src=\"", "\"", imgitemprop, false);
        if (urlImg.length() != 0) {
            if (urlImg.length() > 255) urlImg = urlImg.substring(0, 254);
            VideoPoster vp = new VideoPoster(null, "",
                    "", urlImg, vf);
            vp = videoPosterRepository.save(vp);
            vf.getVideoPosters().add(vp);
        }
    }

    private int getDurationInAddDetails(String strDuration) {
        int duration = 0;
        for (String du : strDuration.split(" ")) {
            if (Pattern.matches("[\\d]{1,3}", du)) {
                duration = Integer.parseInt(du);
                break;
            }
        }
        return duration;
    }

    private void getRating4VideoFilm(VideoFilm vf, String ratingfilm) {
        int rating = 0;
        int ratingVote = 0;
        if (ratingfilm.length() != 0) {
            String ratingstr = findTagInString("<span class=\"ipl-rating-star__rating*>", "</span>", ratingfilm, false);
            Pattern pattern = Pattern.compile("[0-9]([/.][0-9])?");
            Matcher match = pattern.matcher(ratingstr);
            if (match.matches()) {
                rating = (int) (Float.parseFloat(ratingstr) * 10);
            }
            String ratingvotestr = findTagInString("<span class=\"ipl-rating-star__total-votes*>",
                    "</span>", ratingfilm, false);
            ratingvotestr = ratingvotestr.replaceAll("^[\\s]*", "").trim();
            ratingvotestr = ratingvotestr.replace("(", "");
            ratingvotestr = ratingvotestr.replace(")", "");
            ratingvotestr = ratingvotestr.replaceAll(",", "");
            if (Pattern.matches("[0-9]*", ratingvotestr)) {
                ratingVote = Integer.parseInt(ratingvotestr);
            }
        }
        vf.setScoreOnHundred(rating);
        vf.setNbOfVote(ratingVote);

    }

    private List<VideoFilmArtist> getAllActors(VideoFilm vf, String castlistActors) {
        List<VideoFilmArtist> lvfa = new ArrayList<>();
        List<String> lactors = findAllTagsInString("<tr class*>", "</tr>", castlistActors, false);
        int nb = 1;
        for (String act : lactors) {
            String actortmp = findTagInString("<td class=\"itemprop*>", "</td>", act, false);
            String idname = getidnamewithurl(findTagInString("<a href=\"", "\"", actortmp, false));
            if (idname.equals("")) continue;
            VideoArtist va = videoArtistRepository.findById(idname).orElse(null);
            if (va == null) {
                String name = findTagInString("<span class=\"itempro*>", "</span>", actortmp, false);
                va = new VideoArtist(idname, name, null);
                va = videoArtistRepository.save(va);
            }
            VideoFilmArtist vfa = videoFilmArtistRepository.findByVideoFilmAndVideoArtist(vf, va)
                    .orElse(null);
            if (vfa == null) {
                vfa = new VideoFilmArtist(va, vf);
            }
            vfa.setActor(true);
            vfa.setNumberOrderActor(nb++);
            videoFilmArtistRepository.save(vfa);
            lvfa.add(vfa);
        }
        return lvfa;
    }

    private String getidnamewithurl(String url) {
        String id = "";
        for (String s : url.split("/")) {
            Pattern pattern = Pattern.compile("nm[\\d]{6,9}");// 'nm' and 7 digits
            Matcher match = pattern.matcher(s);
            if (match.matches()) {
                id = s;
                break;
            }
        }
        return id;
    }

    private List<VideoFilmArtist> getAllPerson(VideoFilm vf, String headerandtable, String domain) {
        List<VideoFilmArtist> lvfa = new ArrayList<>();
        List<String> lpers = findAllTagsInString("<tr>", "</tr>", headerandtable, false);
        for (String per : lpers) {
            String probablyonepers = findTagInString("<a href=\"/", "\"", per, false);
            if (probablyonepers.length() != 0) {// IF We have one person
                // Yse -> verrify if already exist, and make videofilm
                String idname = getidnamewithurl(probablyonepers);
                if (idname.equals("")) continue;
                VideoArtist va = videoArtistRepository.findById(idname).orElse(null);
                if (va == null) {
                    String name = findTagInString("<a href=\"/name*>", "</a>", per, false);
                    va = new VideoArtist(idname, name, null);
                    va = videoArtistRepository.save(va);
                }
                VideoFilmArtist vfa = videoFilmArtistRepository.findByVideoFilmAndVideoArtist(vf, va)
                        .orElse(null);
                if (vfa == null) {
                    vfa = new VideoFilmArtist(va, vf);
                }
                if (domain.equals("directors")) {
                    vfa.setDirector(true);
                } else {
                    if (domain.equals("writers")) {
                        vfa.setWriter(true);
                    } else {
                        if (domain.equals("producers")) {
                            vfa.setProducer(true);
                        } else {
                            if (domain.equals("composers")) vfa.setMusic(true);
                        }
                    }
                }
                lvfa.add(videoFilmArtistRepository.save(vfa));
            }
        }
        return lvfa;
    }

    private void setOneMoreVideoResumeToVideoFilm(VideoFilm vf, String oneElement) {
        if (oneElement.length() != 0) {
            String plotSummary = findTagInString("<p>", "</p>", oneElement, false);
            VideoResume vr = new VideoResume();
            vr.setDateModifResume(new Date());
            if (plotSummary.length() > 2048) plotSummary = plotSummary.substring(0, 2048);
            vr.setTextResume(plotSummary);
            vr.setVideoFilm(vf);
            videoResumeRepository.save(vr);

        }
    }

    private List<VideoKeyword> getAllKeywordsOfVideoFilm(VideoFilm vf, String oneElement) {
        List<VideoKeyword> lvk = new ArrayList<>();
        List<String> lstr = findAllTagsInString("<li class*>", "</li>", oneElement, false);
        for (String str : lstr) {
            String keyword = findTagInString("<a href*>", "</a>", str, false);
            if (keyword.length() != 0) {
                if (!(keyword.length() > 6 && keyword.substring(0, 7).equals("See All"))) {
                    VideoKeyword vk = videoKeywordRepository.findByKeywordEn(keyword).orElse(null);
                    if (vk == null) {
                        vk = new VideoKeyword(null, keyword, null, null);
                        vk = videoKeywordRepository.save(vk);
                    }
                    lvk.add(vk);
                }
            }
        }
        return lvk;
    }

    private List<VideoKind> getAllKindsOfVideoFilm(VideoFilm vf, String oneElement) {
        List<VideoKind> lvk = new ArrayList<>();
        List<String> lstr = findAllTagsInString("<li class*>", "</li>", oneElement, false);
        for (String str : lstr) {
            String kind = findTagInString("<a href*>", "</a>", str, false);
            if (kind.length() != 0) {
                VideoKind vk = videoKindRepository.findByKindEn(kind).orElse(null);
                if (vk == null) {
                    vk = new VideoKind(null, kind, null, null);
                    vk = videoKindRepository.save(vk);
                }
                lvk.add(vk);
            }
        }
        return lvk;
    }

    private List<VideoCountry> getAllCountries(List<String> lstr) {
        List<VideoCountry> lvc = new ArrayList<>();
        for (String str : lstr) {
            String c = findTagInString("<a href*>", "</a>", str, false);
            if (c.length() > 0) {
                VideoCountry vc = videoCountryRepository.findByCountry(c).orElse(null);
                if (vc == null) {
                    String u = findTagInString("<a href=\"/", "\"", str, false);
                    if (u.split("/").length > 1) u = u.split("/")[1];
                    if (u.length() > 32) u = u.substring(0, 31);
                    vc = new VideoCountry(null, c, u, null, null);
                }
                vc = videoCountryRepository.save(vc);
                lvc.add(vc);
            }
        }
        return lvc;
    }

    private List<VideoLanguage> getAllLanguages(List<String> lstr) {
        List<VideoLanguage> lvl = new ArrayList<>();
        for (String str : lstr) {
            String l = findTagInString("<a href*>", "</a>", str, false);
            String u = findTagInString("<a href=\"/", "\"", str, false);
            if (u.split("/").length > 1) u = u.split("/")[1];
            if (u.length() > 32) u = u.substring(0, 31);
            if (l.length() > 32) l = l.substring(0, 31);
            if (l.length() > 0 && u.length() > 0) {
                VideoLanguage vl = videoLanguageRepository.findByUrlLanguage(u).orElse(null);
                if (vl == null) {
                    vl = new VideoLanguage(null, l, u, null);
                }
                vl = videoLanguageRepository.save(vl);
                lvl.add(vl);
            }
        }
        return lvl;
    }

    private VideoTitle setTitleAndHisLanguage(VideoFilm vf, String partHeader) {
        String title = findTagInString("itemprop=\"name*>", "<", partHeader, false);
        if (title.equals("")) title = "?";
        String country = "";
        String part1 = findTagInString("/releaseinfo\"", "</a>", partHeader, false);
        String part1Tbl [] = part1.split("\\(");
        if (part1Tbl.length > 1) {
            String part2 = part1.split("\\(")[1];
            country = part2.split("\\)")[0];
        } else {
            if(part1Tbl.length == 1 && (! part1Tbl[0].equals(""))){
                country = vf.getVideoCountries().get(0).getCountry();
            }
        }
        if (country.equals("")) country = "?";
        if (country.length() > 32) country = country.substring(0, 31);
        VideoCountry vc = videoCountryRepository.findByCountry(country).orElse(null);
        if (vc == null) {
            vc = new VideoCountry(null, country, "", null, null);
            vc = videoCountryRepository.save(vc);
        }

        VideoTitle vt = videoTitleRepository.findByVideoFilmAndVideoCountry(vf, vc).orElse(null);
        if (vt == null) {
            vt = new VideoTitle(vc, vf);
            vt.setTitle(cleanString(title));
            videoTitleRepository.save(vt);
        }
        return vt;
    }

    private VideoResume getResume(String resumeandmore, VideoFilm vf) {
        VideoResume vr = null;
        if (resumeandmore.length() > 10) {
            String res = findTagInString("<div>", "</div>", resumeandmore, false);
            res = cleanString(res);
            if (res.length() > 20148) res = res.substring(0, 2047);
            vr = new VideoResume(null, res, new Date(), vf);
            vr = videoResumeRepository.save(vr);
        }
        return vr;
    }

    private void searchTypeMmi(VideoFilm vf, String idMyMediaInfo, List<String> lstr) {
        if (lstr.size() != 0) {
            TypeMmi tm = new TypeMmi();
            TypeName tn = null;
            for (String str : lstr) {
                if (str.contains("Documentary")) {
                    tn = typeNameRepository.findByTypeName("doc").orElse(null);
                    if (tn == null) tn = typeNameRepository.save(
                            new TypeName(null, "doc", null));
                    break;
                } else {
                    if (str.contains("Serie") || str.contains("TV Episode")) {
                        tn = typeNameRepository.findByTypeName("serie").orElse(null);
                        if (tn == null) tn = typeNameRepository.save(
                                new TypeName(null, "serie", null));
                        break;
                    } else {
                        if (str.contains("Animation")) {
                            tn = typeNameRepository.findByTypeName("anime").orElse(null);
                            if (tn == null) tn = typeNameRepository.save(
                                    new TypeName(null, "anime", null));
                            break;
                        } else {
                            if (str.contains("Short")) {
                                tn = typeNameRepository.findByTypeName("court").orElse(null);
                                if (tn == null) tn = typeNameRepository.save(
                                        new TypeName(null, "court", null));
                                break;
                            } else {
                                if (str.contains("TV Movie")) {
                                    tn = typeNameRepository.findByTypeName("tv movie").orElse(null);
                                    if (tn == null) tn = typeNameRepository.save(
                                            new TypeName(null, "tv movie", null));
                                    break;
                                } else {
                                    if (str.contains("Movie")) {
                                        tn = typeNameRepository.findByTypeName("film").orElse(null);
                                        if (tn == null) tn = typeNameRepository.save(
                                                new TypeName(null, "film", null));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (tn == null) {
                tn = typeNameRepository.findByTypeName("autre").orElse(null);
                if (tn == null) tn = typeNameRepository.save(
                        new TypeName(null, "autre", null));
            }
            tm.setTypeName(tn);

            tm.setDateModif(new Date());
            tm.setVideoFilm(vf);
            typeMmiRepository.save(tm);
        }
    }

    private String cleanString(String strToClean) {
        strToClean = strToClean.replaceAll("\\s+\\n+", " ");
        strToClean = strToClean.replaceAll("^[\\s]*", "").trim();
        return strToClean;
    }

    public VideoFilm createOneVideoFilm(VideoFilm vf, String toParse, String idMyMediaInfo) {
        if (vf == null || toParse.equals("")) throw new RuntimeException("Data no valide to create one videoFilm");
        log.warn("===> Add one VideoFilm with idTt : "+vf.getIdVideo()+" and idMmi : "+idMyMediaInfo);
        vf.setDateModifFilm(new Date());
        VideoSourceInfo vsi = videoSourceInfoRepository.findByName("imdb").orElse(null);
        if (vsi == null) {
            vsi = new VideoSourceInfo("tt", "http://www.imdb.com", "imdb",
                    new Date(), null);
            vsi = videoSourceInfoRepository.save(vsi);
        }
        vf.setVideoSourceInfo(vsi);
        vf = videoFilmRepository.save(vf);

        // First bloc of webPage : VideoPoster, Year, Rating
        String partHeader = findTagInString("<div class=\"titlereference-header*>",
                "</div>", toParse, false);
        // get titles
        vf.getVideoTitles().add(setTitleAndHisLanguage(vf, partHeader));
        // get url img
        geturlImgIfExist4VideoFilm(vf, findTagInString("<img", "\">", partHeader, true));
        // get year
        vf.setYear(getYear4VideoFilm(findTagInString("<ul class=\"ipl-inline-list*>",
                "</ul>", partHeader, false)));
        // rating
        getRating4VideoFilm(vf, findTagInString("<div class=\"ipl-rating-star*>", "</div>",
                partHeader, false));
        // Search TypeMmi
        searchTypeMmi(vf, idMyMediaInfo, findAllTagsInString("<li class=\"ipl-inline-list__item", "</li>",
                partHeader, false));

        // Get VideoResume
        vf.getVideoResumes().add(getResume(findTagInString("<section class=\"titlereference-section-overview*>",
                "</div>", toParse, true), vf));


        // Section credits : Actors(cast), Directors, "writers", "producers", "composers"
        String partCredits = findTagInString("<section class=\"titlereference-section-credits*>",
                "</section>", toParse, false);
        List<String> ltable = findAllTagsInString("<header", "</table>", partCredits, true);

        List<String> lsearch = Stream.of("cast", "directors", "writers", "producers", "composers")
                .collect(Collectors.toList());
        List<VideoFilmArtist> lvfa = new ArrayList<>();
        for (String headerandtable : ltable) {
            String sdomain = findTagInString("<h4", "</h4>", headerandtable, true);
            String namedomain = findTagInString("id=\"", "\"", sdomain, false);
            if (namedomain.contains("cast")) {
                lvfa.addAll(getAllActors(vf, headerandtable));
            } else {
                lvfa.addAll(getAllPerson(vf, headerandtable, namedomain));
            }
            lsearch.remove(namedomain);
            if (lsearch.size() == 0) break;
        }
        vf.getVideoFilmArtists().addAll(lvfa);
        vf = videoFilmRepository.save(vf);
        partCredits = null;
        // Section Plot summary, keyword, and kind
        String storyline = findTagInString("<section class=\"titlereference-section-storyline*>",
                "</section>", toParse, false);
        List<String> storyElements = findAllTagsInString("<tr class*>", "</tr>", storyline, false);
        for (String oneElement : storyElements) {
            String element = findTagInString("<td class=*>", "</td>", oneElement, false);
            element = element.replace("^[\\s]*", "").trim();
            if (element.contains("Plot Summary")) {
                setOneMoreVideoResumeToVideoFilm(vf, oneElement);
            }
            if (element.contains("Plot Keywords")) {
                List<VideoKeyword> lvk = getAllKeywordsOfVideoFilm(vf, oneElement);
                //Save vf with keywords
                if (lvk.size() != 0) {
                    vf.getVideoKeywordSet().addAll(lvk);
                    vf = videoFilmRepository.save(vf);
                }
            }
            if (element.contains("Genres")) {
                List<VideoKind> lvk = getAllKindsOfVideoFilm(vf, oneElement);
                if (lvk.size() != 0) {
                    vf.setVideoKinds(lvk);
                    vf = videoFilmRepository.save(vf);
                }
            }
        }
        storyline = null;
        storyElements = null;

        // get Runtime, Country, Language, and Filming Locations
        String addDetails = findTagInString("<section class=\"titlereference-section-additional-details*>"
                , "</section>", toParse, false);
        List<String> addAllDetails = findAllTagsInString("<tr class=*>", "</tr>", addDetails, false);

        for (String detail : addAllDetails) {
            String titleDetail = findTagInString("<td class*>", "</td>", detail, false);
            if (titleDetail.contains("Runtime")) {
                vf.setDuration(getDurationInAddDetails(findTagInString("<li class*>", "</li>", detail, false)));
            } else {
                if (titleDetail.contains("Country")) {
                    vf.getVideoCountries()
                            .addAll(getAllCountries(findAllTagsInString("<li class*>",
                                    "</li>", detail, false)));
                } else {
                    if (titleDetail.contains("Language")) {
                        vf.getVideoLanguages()
                                .addAll(getAllLanguages(findAllTagsInString("<li class*>",
                                        "</li>", detail, false)));
                    }
                }
            }
        }
        videoFilmRepository.save(vf);
        addDetails = null;
        addAllDetails = null;

        Preferences pr = preferencesRepository.findById("01").orElse(null);
        if (pr != null) {
            List<ItemToSearch> itss = pr.getItemToSearches();
            for (ItemToSearch its : itss) {
                String item = its.getItemImdb();
                if (item.equals("Box Office")) {
                    String boxOffice = findTagInString("<section class=\"titlereference-section-box-office*>"
                            , "</section>", toParse, false);
                    if (boxOffice.length() != 0) {
                        parseGoodies(boxOffice, its, vf);
                    }
                }
                if (item.equals("Did You Know?")) {
                    String didYouKnow = findTagInString("<section class=\"titlereference-section-did-you-know*>"
                            , "</section>", toParse, false);
                    if (didYouKnow.length() != 0) {
                        parseGoodies(didYouKnow, its, vf);
                    }
                }
            }
        }
        return videoFilmRepository.save(vf);
    }

    private void parseGoodies(String toParse, ItemToSearch its, VideoFilm vf) {
        List<String> eachItemBoxOffice = findAllTagsInString("<tr class=*>", "</tr>"
                , toParse, false);
        for (String detail : eachItemBoxOffice) {
            String key = findTagInString("<td class=*>", "</td>", detail, false);
            for (String it : its.getKeyset()) {
                if (key.equals(it)) {
                    String value = findTagInString("<td>", "</td>", detail, false);

                    value = findTagInStringToDelete("<a*>", value, true);
                    value = findTagInStringToDelete("<b>", value, true);
                    value = findTagInStringToDelete("<i>", value, true);
                    value = findTagInStringToDelete("<u>", value, true);
                    value = findTagInStringToDelete("</a>", value, true);
                    value = value.replaceAll("See more &raquo;", "");
                    if(value.length()>1024)value=value.substring(0, 1024);
                    Map<String, String> mp = new HashMap<>();
                    VideoMoreInformation vmi = videoMoreInformationRepository.findByVideoFilm(vf).orElse(null);
                    if (vmi == null) {
                        mp.put(key, cleanString(value));
                        vmi = new VideoMoreInformation(null, mp, vf);
                        vmi = videoMoreInformationRepository.save(vmi);
                        vf.setVideoMoreInformation(vmi);

                    } else {
                        vmi.getInformap().put(key, cleanString(value));
                        videoMoreInformationRepository.save(vmi);
                    }
                }
            }
        }
    }

    public String findTagInStringToDelete(String mc, String toParser, boolean all) {
        String str = "";
        char e = '*';
        boolean trouver = false;
        char[] motClefDebut = mc.toCharArray();
        int sizeMotClefDebut = motClefDebut.length;
        char[] toPars = toParser.toCharArray();
        int sizeToPars = toParser.length();

        int pos = -1;
        int posDebut = 0;
        int wclong = 1;
        int charMcd = 0;

        // ******* Parcour de la chaine
        while (pos < (sizeToPars - 1)) {
            pos++;
            wclong = 0;
            posDebut = pos;
            charMcd = 0;
            while ((int) toPars[posDebut] == (int) motClefDebut[charMcd] && !trouver) {
                posDebut++;
                charMcd++;
                // MCD Sans wildcard
                if (charMcd == (sizeMotClefDebut)) {
                    pos = posDebut;
                    if(!all)trouver=true;
                    break;
                }
                if (charMcd < (sizeMotClefDebut - 1)) {
                    // MCD avec wildcard
                    if (motClefDebut[charMcd] == e) {
                        if ((charMcd + 1) < sizeMotClefDebut) {
                            wclong = 0;
                            while (toPars[posDebut + wclong] != (int) motClefDebut[charMcd + 1]) {
                                if ((charMcd + wclong) < sizeToPars || wclong > 500) {
                                    wclong++;
                                } else {
                                    break;
                                }
                            }
                            if (wclong < 500) {
                                pos = posDebut + wclong + 1;
                                if(!all)trouver=true;
                            }
                        }
                    }
                }
                //if(posDebut>=sizeToPars)break;
            }
            if(pos<sizeToPars)
                str += toPars[pos];
        }
        return str;
    }

    public void addTitlesToVideoFilm(VideoFilm vf, String toParse4title, List<String> lcountry2search) {
        String toParse = findTagInString("class=\"ipl-list-title\">Also Known As", "</table>",toParse4title, false );
        List<String> listTitlesAndCountries = findAllTagsInString("<tr class=*>", "</tr>", toParse, false);

        // For each titleAndCountry of web page
//        List<VideoTitle> videoTitlesInDb = (List<VideoTitle>) videoTitleRepository.findByVideoFilm(vf).orElse(null);
        List<VideoTitle> videoTitlesInDb = new ArrayList<>(videoTitleRepository.findByVideoFilm_IdVideo(vf.getIdVideo()));
//        if(videoTitlesInDb == null)videoTitlesInDb=new ArrayList<VideoTitle>();
        for(String tAndC: listTitlesAndCountries){
            // Get countryRemote
            String countryRemote = findTagInString("<td class=\"aka-item__name*>","</td>", tAndC, false);
            // if country is asked
            for(String country2search: lcountry2search){
                if(countryRemote.contains(country2search)){
                    // we found what we were looking for
                    String titleremote = findTagInString("<td class=\"aka-item__title*>","</td>", tAndC, false);
                    // if titleRemote is in DB ?

                    countryRemote =countryRemote.replaceFirst("[\\s]*", "").trim();
                    if(countryRemote.length()>32)countryRemote=countryRemote.substring(0, 31);
                    VideoCountry vc = videoCountryRepository.findByCountry(countryRemote).orElse(null);

                    if(vc==null){
                        vc=new VideoCountry(null, countryRemote, null, null, null);
                        vc = videoCountryRepository.save(vc);
                    }

                    VideoTitle vt = videoTitleRepository.findByVideoFilmAndVideoCountry(vf, vc).orElse(null);
//                    VideoTitle vt = videoTitleRepository.findByVideoFilm_IdVideoAndVideoCountry_IdCountry(vf.getIdVideo()
//                            , vc.getIdCountry()).orElse(null);
                    if(vt==null){
                        boolean test = false;
                        if(videoTitlesInDb.size()>0){
                            for(VideoTitle videoTitleInDb: videoTitlesInDb){
                                if(videoTitleInDb.getTitle().equals(titleremote)){
                                    test = true;
                                    break;
                                }
                            }
                        }
                        if(!test){
                            vt = new VideoTitle(vc,vf);
                            vt.setTitle(titleremote);
                            vt = videoTitleRepository.save(vt);
                            videoTitlesInDb.add(vt);
                        }
                    }
                }
            }
        }
    }
    public void addKeywordsToVideoFilm(VideoFilm vf, String toParse4title, int limite){

    }
}
