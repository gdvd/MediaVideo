package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.daoMysql.MyMediaInfoRepository;
import fr.gdvd.media_manager.daoMysql.MyUserRepository;
import fr.gdvd.media_manager.daoMysql.VideoNameExportRepository;
import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesMysql.MyUser;
import fr.gdvd.media_manager.entitiesMysql.VideoNameExport;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    private VideoNameExportRepository videoNameExportRepository;
    @Autowired
    private MyMediaInfoRepository myMediaInfoRepository;
    @Autowired
    private MyUserRepository myUserRepository;

/*
    @Override
    public List<VideoNameExport> getAllVideoNameExport() {
        return videoNameExportRepository.findAll();
    }

*/
    @Override
    public void toggleActivationExport(Long id) {
        VideoNameExport vne = videoNameExportRepository.findByIdVideoNameExport(id);
        if(vne != null){
            vne.setActive(!vne.isActive());
            videoNameExportRepository.save(vne);
        }
    }

    @Override
    public MyMediaInfo getOneMmi(String idmmi) {
        return myMediaInfoRepository.findById(idmmi).orElse(null);
    }

    @Override
    public List<String> getListUsers() {
        List<MyUser> lmu = myUserRepository.findAll();
        List<String> llogin = new ArrayList<>();
        for(MyUser user: lmu){
            llogin.add(user.getLogin());
        }
        return llogin;
    }

}
