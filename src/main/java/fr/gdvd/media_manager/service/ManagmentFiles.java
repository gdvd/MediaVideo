package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesMysql.VideoNameExport;
import fr.gdvd.media_manager.entitiesMysql.VideoSupportPath;
import fr.gdvd.media_manager.entitiesNoDb.FileVideo;
import fr.gdvd.media_manager.entitiesNoDb.MediaInfoLight;
import fr.gdvd.media_manager.entitiesNoDb.ScanMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ManagmentFiles {

//    public List<FileVideo> scanFolder(String urlFolder);
    public ScanMessage scanFolderWithPathdir(String path);
    public void senddatas(List<String> urls, String nameExportDecode, String login);
    public MyMediaInfo senddata(String url);
    VideoNameExport ifNameExportExist(String nameExport);
    VideoNameExport createNameExport(String nameExport, String login);
    MyMediaInfo createMmiAndGetMd5(String pathGeneraldecode);
    VideoSupportPath createVSP(Long idNameExport, String md5, String pathGeneral);
    VideoNameExport updateNameExport(Long idNameExport, String login, int active, int complete);
    void deleteOldVsp(List<String> oldPath, Long idNameExport);
    List<VideoNameExport> getAllPath(String login);
    List<MyMediaInfo> getAllMmi(List<String> listIdMmi);
    List<VideoSupportPath> getLisOldVsp(String oldVsp);

    List<VideoNameExport> toggleActivationLogintonameexport(Long idvne,
                                                            String oldstate,
                                                            String logintoapply,
                                                            String login);
//    List<MediaInfoLight> findAllMediaInfoLight();

    List<VideoNameExport> getAllPathRemote(String login);
    VideoNameExport storemmi(List<MyMediaInfo> lmmi, int idvne, int idVneRemote);

    List<MyMediaInfo> getAllVideoByUser();

    Page getAllVideoByUserAndByPage(String login, int size, int page, int pos);
    Page getAllVideoByUserAndByPage2(String login, int size, int page, int pos);

    List<Object> getObject();

    List<VideoSupportPath> getVsp(String path);
    Page<VideoSupportPath> getVspPP(String path);

    Page<VideoNameExport> getVnePP(String path);

    List<VideoNameExport> getVne(String path);

    List<VideoSupportPath> getVneToAllPath(String filtre, String login);

    List<VideoSupportPath> listVspForLoginWithFilter(String filtre, String login);

    List<VideoSupportPath> listVspForLogin(String login);

    Page<VideoSupportPath> listVspForLoginPP(String login, int page, int size, String toSort, String filter);
    Page<MyMediaInfo> listMmiForLoginPP(String login, int page, int size, String toSort, String filter);

    void addnewext(String ext);
}
