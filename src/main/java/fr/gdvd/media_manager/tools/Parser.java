package fr.gdvd.media_manager.tools;

import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Double.parseDouble;

@Service
public class Parser {

    //############################## Parser #######################################
    public String findTagInString(String mcd, String mcf, String toParserA, boolean withTag) {
        List<char[]> tags = new ArrayList();
        String str = "";
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
                        // pos--;
                        i = tags.size();
                        break;
                    }
                    if (charMcd < (sizeMotClefDebut - 1)) {
                        // MCD avec wildcard
                        if (motClefDebut[charMcd + 1] == e) {
                            if ((charMcd + 1) < sizeMotClefDebut) {
                                wclong = 0;
                                while (toPars[posDebut + wclong] != (int) motClefDebut[charMcd + 2]) {
                                    if ((charMcd + 1 + wclong) < sizeToPars || wclong > 500) {
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
                                    // pos--;
                                    i = tags.size();
                                    break;
                                }
                                break;
                            }
                        }
                    }
                    // if(posDebut>=sizeToPars)break;
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
                                        str = toParser.substring(posEcritureDebut, posEcritureFin);
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
        return str;
    }
    //#######################################################
    public List<String> findAllTagsInString(String mcd, String mcf, String strEntre, boolean withTag) {
        List<char[]> tags = new ArrayList();
        tags.add("<div*>".toCharArray());
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
                        deep++;
                        i = tags.size();
                        break;
                    }
                    if (charMcd < (sizeMotClefDebut - 1)) {
                        // MCD avec wildcard
                        if (motClefDebut[charMcd + 1] == e) {
                            if ((charMcd + 1) < sizeMotClefDebut) {
                                wclong = 0;
                                while (toPars[posDebut + wclong] != (int) motClefDebut[charMcd + 2]) {
                                    if ((charMcd + 1 + wclong) < sizeToPars || wclong > 500) {
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
                    if (posDebut >= sizeToPars)
                        break;
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
                                pos = posFin - 0;

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
    /*public Document convertXmlToDocument(String parse) {
        Document doc = new Document();
        return convertXmlToDocumentInt(parse, doc);
    }*/
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
    /*private Document convertXmlToDocumentInt(String topars, Document doc) {
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
                                Document docTmpOut = new Document();
                                Document docTmpIn = new Document();
                                if (toparserTmp.length() >= 7) {
                                    docTmpIn = convertXmlToDocumentInt(toparserTmp, docTmpOut);
                                    if (docTmpIn.size() == 0) {
                                        doc = doc.append(keywordBegin, dataType(toparserTmp));
                                    } else {
                                        doc = doc.append(keywordBegin, docTmpIn);
                                    }
                                } else {
                                    doc = doc.append(keywordBegin, dataType(toparserTmp));
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
        return doc;
    }*/
    private Object dataType(String keyword) {
        Object ob = null;

        // Identify double
        Pattern pattern = Pattern.compile("(^[+-]?(-?\\d+\\.)?\\d+)$");
        Matcher matcher = pattern.matcher(keyword);
        if (matcher.find()) {
            ob = (Double) parseDouble(keyword);
        } else {
            // Identify Date UTC
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
    /*public List<Document> xmlToDoc(String mcNbItem, String domain, String mediaInfo) {
        Document doc = new Document();
        List<String> vid = findInfoByDomain(mcNbItem, domain, mediaInfo);
        List<Document> listF = new ArrayList<>();
        if (vid != null) {
            List<String> list0 = vid;
            for (int i = 1; i <= list0.size(); i++) {
                listF.add(convertXmlToDocument(list0.get(i - 1)));
            }
            return listF;// If there's one or some track(s)
        } else {
            listF.add(doc);
        }
        return listF;//If there's no track
    }*/
    //
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
}
