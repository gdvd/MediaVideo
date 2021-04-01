package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.entitiesMysql.*;
import fr.gdvd.media_manager.entitiesNoDb.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;

public interface ManagmentFiles {

    ScanMessage scanFolderWithPathdir(String path);
    void senddatas(List<String> urls, String nameExportDecode, String login);
    MyMediaInfo senddata(String url);
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
    List<VideoNameExport> getAllPathRemote(String login);
    VideoNameExport storemmi(LinkMmiVf linkMmiVf, int idvne, int idVneRemote, int withUpdate);
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
    Page<MyMediaInfo> listMmiForLoginPP(String login, int page, int size,
                                        String toSort, String filter,
                                        String vneName, boolean filename, String filtertt);
    void addnewext(String ext);
    List<VNELight> lVneIdToName(String login);
    Page<MyMediaInfo> researchByName(String nm, String login);
    Page<MyMediaInfo> listMmiForLoginWithNamePP(String login, int page, int size, String toSort, String filter);
    void addcountry(String country);
    void deletecountry(String country);
    void updatealltitles();
    List<UserLight> listUserWithId();

    List<VideoKind> getAllKinds(String login);
}
