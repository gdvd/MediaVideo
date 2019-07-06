package fr.gdvd.media_manager.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
public class MediaPathServiceImpl implements MediaPathService {

    /*@Autowired
    private MediaPathRepository mediaPathRepository;

    @Override
    public MediaPath getOnePath(String path) {
        return mediaPathRepository.getByNameExportAndActive(path);
    }

    @Override
    public List<MediaPath> getAllPath() {
        List<MediaPath> col = new ArrayList<>();
        for (MediaPath t : mediaPathRepository.findAll())
            col.add(t);
        return col;
    }

    @Override
    public List<MediaPath> getAllPathActive() {
        return mediaPathRepository.findAllByActiveTrue();
    }

    @Override
    public int beforetosave(int i, MediaPath mediaPathRemote, String login) {
        // bit signication of i (at the entrance : forced, to the output : suggest

        // 2^0 1    NameExport
        // 2^1 2    PathGeneral
        // 2^2 4    DateModif
        // 2^3 8    Acive ??????
        // 2^4 16   VideoIDS

        //Test if Id exist. If yes quit with i=32 except if i=32
        MediaPath mediaIdLocal = mediaPathRepository.getById(mediaPathRemote.getId().toString());
        int j = 0;
        if(mediaIdLocal!=null){
            if(mediaIdLocal.getNameExport().equals(mediaPathRemote.getNameExport()))j=j|1;
            if(mediaIdLocal.getPathGeneral().equals(mediaPathRemote.getPathGeneral()))j=j|2;
            if(mediaIdLocal.getDateModif().equals(mediaPathRemote.getDateModif()))j=j|4;
            if(mediaIdLocal.isActive() == mediaPathRemote.isActive())j=j|8;
           *//* if(Arrays.equals(mediaIdLocal.getVideoids().toArray(),
                    mediaPathRemote.getVideoids().toArray()))j=j|16;*//*
            if(mediaIdLocal.getId().equals(mediaPathRemote.getId()))j=j|32;

            *//*if((i&1)==1){//must we forced, if one mediaPath.nameExport is same, in fct of i
                if(mediaNameLocal.getPathGeneral().equals(mediaPathRemote.getPathGeneral())){
                    //pathGeneral is same (nameExport too)
                    if((i&2)==2){//must we forced?
                        // dateModif same?
                        if(mediaNameLocal.getDateModif().equals(mediaPathRemote.getDateModif())){

                        }else{

                        }
                        // videoids same?
//                        mediaNameLocal.setActive(false);
//                        mediaNameLocal.setDateModif(new Date());

                    }else{
                        i= i|2;//response: suggest to force
                        return i;
                    }
                }else{//pathGeneral is different

                }
            }else{
                i= i|1;//response: suggest to force
                return i;
            }*//*
            //Test si pathGeneral exist si oui quit avec i=2 ou 3 sauf si i=2 ou 3
            // Si ok i=4, recupperer le nom de login->mediapath.owner

        }else {
            // Create new mediaPath with mediaPathRemote
            List<Map<String, String>> users = new ArrayList<>();
            Map<String, String> user = new HashMap<>();
            user.put(login, "777");
            users.add(user);

            MediaPath mediaPath = mediaPathRemote;
            mediaPath.setId(mediaPathRemote.getId());
            mediaPath.setActive(false);
            mediaPath.setDateModif(new Date());
//            mediaPath.setOwner(login);
            mediaPath.setUsers(users);
            mediaPathRepository.save(mediaPath);
            j=64;
            //save mediapath si ok alors i=8 et fin

        }
        return j;
    }

    public void updatePath(String id, int state){
        MediaPath mediaPath = mediaPathRepository.findById(id).orElse(null);
        if(mediaPath!=null){
            mediaPath.setActive(state!=0);
            mediaPathRepository.save(mediaPath);
        }
    }

    @Override
    public void idDesactivation(String idPath) {
        activ(idPath, false);
    }

    @Override
    public void idActivation(String idPath) {
        activ(idPath, true);
    }

    private void activ(String idPath, boolean state){
        MediaPath mediaPath = mediaPathRepository.getById(idPath);
        if(mediaPath==null)return;
        mediaPath.setActive(state);
        mediaPathRepository.save(mediaPath);
    }*/
}
