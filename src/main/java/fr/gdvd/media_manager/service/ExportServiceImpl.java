package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.daoMysql.*;
import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesMysql.MyUser;
import fr.gdvd.media_manager.entitiesMysql.VideoNameExport;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;
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
    @Autowired
    private VideoSupportPathRepository videoSupportPathRepository;
    @Autowired
    private UserToNameExportRepository userToNameExportRepository;


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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public void deleteExport(Long id) {
        VideoNameExport vne = videoNameExportRepository.findByIdVideoNameExport(id);
        if(vne != null){
            log.warn("===> Delete vne n° "+id);
            videoSupportPathRepository.deleteWithIdVne(id);
            vne = videoNameExportRepository.findByIdVideoNameExport(id);
            vne.setComplete(false);
            vne.setActive(false);
            vne.setDateModifNameExport(new Date());
            vne.setNameExport(vne.getNameExport() + "-" +
                    new Random().nextInt(100000));
            userToNameExportRepository.deleteWithIdvne(id);
        }
    }

}
