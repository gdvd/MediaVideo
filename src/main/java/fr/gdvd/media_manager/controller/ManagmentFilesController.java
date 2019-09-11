package fr.gdvd.media_manager.controller;

import fr.gdvd.media_manager.daoMysql.PreferencesRepository;
import fr.gdvd.media_manager.entitiesMysql.*;
import fr.gdvd.media_manager.entitiesNoDb.ScanMessage;
import fr.gdvd.media_manager.service.ManagmentFilesImpl;
import fr.gdvd.media_manager.service.RequestWeb;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.List;
import java.util.Map;


//@Secured({"ROLE_ADMIN", "ROLE_USER"})
//@PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
//@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})

@Log4j2
@RestController
@RequestMapping(value = "managment")
public class ManagmentFilesController {

    // Example : request.getHeader("origin")        -> http://localhost:4200
    // Example : request.getHeader("referer")       -> http://localhost:4200/path
    // Example : request.getHeader("user-agent")    -> navigator used
    // Example : request.getRemoteUser()            -> admin
    // Example : request.getUserPrincipal().getName()-> admin
    // Example : request.getServletPath()           -> /path/infos
    // Example : request.getHeader("authorization") -> token ("Baerer xxx…")

    @Autowired
    private ManagmentFilesImpl managmentFiles;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PreferencesRepository preferencesRepository;
    @Autowired
    private RequestWeb requestWeb;


    @PostMapping(value = "/scanFolderWithPathdir",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public ScanMessage scanFolderWithPathdir(@RequestBody String pathToScan){
        // Is it in local ?
        String test = request.getHeader("origin");
        Map<String, String> mp = preferencesRepository.findByIdPreferences("01").getPrefmap();
        boolean ctrl = false;
        for (Map.Entry<String, String> dn : mp.entrySet()) {
            if(dn.getKey().startsWith("domain")){
                String str = dn.getValue();
                if(test.contains(str)){
                    ctrl=true;
                    break;
                }
            }
        }
         if(!ctrl)return new ScanMessage("Error, You must be in local.","",
                    0,null, null, null);
//        String pathToScandecode = new String(Base64.getDecoder().decode(pathToScan));
        return managmentFiles.scanFolderWithPathdir(pathToScan);
    }
    // AddNewExtention
    @PostMapping(value = "/addnewext")
    public void addnewext(@RequestBody String ext){
        managmentFiles.addnewext(ext);
    }

    // AddNewCountry for titles
    @PostMapping(value = "/addcountry")
    public void addcountry(@RequestBody String country){
        managmentFiles.addcountry(country);
    }

    // Delete Country for titles
    @PostMapping(value = "/deletecountry")
    public void deletecountry(@RequestBody String country){
        managmentFiles.deletecountry(country);
    }

    // ifNameExportExist
    @PostMapping(value = "/ifNameExportExist",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public VideoNameExport ifNameExportExist(@RequestBody String nameExport){
        return managmentFiles.ifNameExportExist(nameExport);
    }
    // createNameExport
    @PostMapping(value = "/createNameExport",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public VideoNameExport createNameExport(@RequestBody String nameExport){
        String login = request.getRemoteUser();
        return managmentFiles.createNameExport(nameExport, login);
    }
    // updateNameExport
    @GetMapping(value = "/updateNameExport/{idNameExport}/{active}/{complete}",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public VideoNameExport updateNameExport(
            @PathVariable Long idNameExport,
            @PathVariable int active,
            @PathVariable int complete){
        String login = request.getRemoteUser();
        return managmentFiles.updateNameExport(idNameExport, login, active, complete);
    }
    // createMmiAndGetMd5
    @PostMapping(value = "/createMmiAndGetMd5",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MyMediaInfo createMmiAndGetMd5(@RequestBody String pathGeneral){
        return managmentFiles.createMmiAndGetMd5(pathGeneral);
    }
    // createVSP
    @PostMapping(value = "/createVSP/{idNameExportStr}/{md5str}",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public VideoSupportPath createVSP(
            @PathVariable String idNameExportStr,
            @PathVariable String  md5str,
            @RequestBody String pathGeneral
    ){
        Long idNameExport = Long.parseLong(idNameExportStr);
        String md5 = md5str;
        return managmentFiles.createVSP(idNameExport, md5, pathGeneral);
    }

    @PostMapping(value = "/senddata")
    public void senddata(@RequestBody String url){
        managmentFiles.senddata(url);
    }

    @PostMapping(value = "/deleteOldVsp/{idNameExport}")
    public void deleteOldVsp(@PathVariable Long idNameExport,
                             @RequestBody List<String> oldPath){
        managmentFiles.deleteOldVsp(oldPath, idNameExport);
    }

    @PostMapping(value = "/getAllMmi")
    public List<MyMediaInfo> getAllMmi(@RequestBody List<String> listIdMmi){
        return managmentFiles.getAllMmi(listIdMmi);
    }

    @PostMapping(value = "/senddatas/{nameExport}")
    public void senddatas(@PathVariable String nameExport,
                          @RequestBody List<String> urls){
        String nameExportDecode = new String(Base64.getDecoder()
                .decode(nameExport));
        String login = request.getRemoteUser();
        managmentFiles.senddatas(urls, nameExportDecode, login);

    }
    @GetMapping(value = "/getAllPath",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public List<VideoNameExport> getAllPath(){
        String login = request.getRemoteUser();
        return managmentFiles.getAllPath(login);
    }

    @GetMapping(value = "/getAllPathRemote",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public List<VideoNameExport> getAllPathRemote(){
        String login = request.getRemoteUser();
        return managmentFiles.getAllPathRemote(login);
    }

    @PostMapping(value = "/getLisOldVsp",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public List<VideoSupportPath> getLisOldVsp(@RequestBody String oldVsp){
        return managmentFiles.getLisOldVsp(oldVsp);
    }

    @PostMapping(value = "/toggleActivationLogintonameexport/{idvne}/{oldstate}")
    public List<VideoNameExport> toggleActivationLogin(@PathVariable Long idvne,
                                                           @PathVariable String oldstate,
                                                           @RequestBody String logintoapply){
        String login = request.getRemoteUser();
        return managmentFiles.toggleActivationLogintonameexport(idvne, oldstate, logintoapply, login);
    }

    @PostMapping(value = "/storemmi/{idvne}/{idVneRemote}/{withUpdate}")
    public VideoNameExport storemmi(@RequestBody List<MyMediaInfo> lmmi,
                                    @PathVariable int idvne,
                                    @PathVariable int idVneRemote,
                                    @PathVariable int withUpdate){
        return managmentFiles.storemmi(lmmi, idvne, idVneRemote, withUpdate);
    }

    @GetMapping(value = "/GetAllVideoByUser")
    public List<MyMediaInfo> getAllVideoByUser(){
        return managmentFiles.getAllVideoByUser();
    }

    @GetMapping(value = "/GetAllVideoByUserAndByPage/")
    public Page getAllVideoByUserAndByPage(@RequestParam(defaultValue = "0") int pos,
                                           @RequestParam(defaultValue = "5") int page,
                                           @RequestParam(defaultValue = "10") int size){
        String login = request.getRemoteUser();
        return managmentFiles.getAllVideoByUserAndByPage(login, size, page, pos);
    }

    @GetMapping(value = "/GetAllVideoByUserAndByPage2/")
    public Page getAllVideoByUserAndByPage2(@RequestParam(defaultValue = "0") int pos,
                                           @RequestParam(defaultValue = "5") int page,
                                           @RequestParam(defaultValue = "10") int size){
        String login = request.getRemoteUser();
        return managmentFiles.getAllVideoByUserAndByPage2(login, size, page, pos);
    }

    @GetMapping(value = "/getObject")
    public List<Object> getObject(){
        return managmentFiles.getObject();
    }
    @PostMapping(value = "/getVsp/")
    public List<VideoSupportPath> getVsp(@RequestBody String path){
        return managmentFiles.getVsp(path);
    }

    @PostMapping(value = "/getVspPP/")
    public Page<VideoSupportPath> getVspPP(@RequestBody String path){
        return managmentFiles.getVspPP(path);
    }

    @PostMapping(value = "/getVnePP/")
    public Page<VideoNameExport> getVnePP(@RequestBody String path){
        return managmentFiles.getVnePP(path);
    }

    @PostMapping(value = "/getVne/")
    public List<VideoNameExport> getVne(@RequestBody String path){
        return managmentFiles.getVne(path);
    }

    @PostMapping(value = "/vspActiveCompleteForLogin/")
    public List<VideoSupportPath> vspActiveCompleteForLogin(@RequestBody String filtre){
        String login = request.getRemoteUser();
        return managmentFiles.getVneToAllPath(filtre, login);
    }

    @PostMapping(value = "/listVspForLoginWithFilter")
    public List<VideoSupportPath> listVspForLoginWithFilter(@RequestBody String filter){
        String login = request.getRemoteUser();
        return managmentFiles.listVspForLoginWithFilter(login, filter );
    }

    @GetMapping(value = "/listVspForLogin")
    public List<VideoSupportPath> listVspForLogin(){
        String login = request.getRemoteUser();
        return managmentFiles.listVspForLogin(login);
    }

    @PostMapping(value = "/listVspForLoginPP")
    public Page<VideoSupportPath> listVspForLoginPP(@RequestParam int page,
                                                    @RequestParam int size,
                                                    @RequestParam(defaultValue = "") String toSort,
                                                    @RequestBody String filter){
        String login = request.getRemoteUser();
        return managmentFiles.listVspForLoginPP(login, page, size, toSort, filter);
    }

/*    //copy to videouser
    @PostMapping(value = "/listMmiForLoginPP")
    public Page<MyMediaInfo> listMmiForLoginPP(@RequestParam int page,
                                                    @RequestParam int size,
                                                    @RequestParam(defaultValue = "") String toSort,
                                                    @RequestBody String filter,
                                               @RequestParam(defaultValue = "") String vneName){
        String login = request.getRemoteUser();
        Page<MyMediaInfo> pmmi = managmentFiles.listMmiForLoginPP(login, page, size, toSort, filter, vneName);
        return pmmi;
//        return managmentFiles.listMmiForLoginPP(login, page, size, toSort, filter);
    }

    //copy to videouser
    @PostMapping(value = "/listMmiForLoginWithNamePP")
    public Page<MyMediaInfo> listMmiForLoginWithNamePP(@RequestParam int page,
                                                    @RequestParam int size,
                                                    @RequestParam(defaultValue = "") String toSort,
                                                    @RequestBody String filter){
        String login = request.getRemoteUser();
        Page<MyMediaInfo> pmmi = managmentFiles.listMmiForLoginWithNamePP(login, page, size, toSort, filter);
        return pmmi;
//        return managmentFiles.listMmiForLoginPP(login, page, size, toSort, filter);
    }*/

    @GetMapping(value = "/researchByName/{nm}", produces =
            {MediaType.APPLICATION_JSON_VALUE})
    public Page<MyMediaInfo> researchByName(@PathVariable String nm){
        String login = request.getRemoteUser();
        log.info("ResearchByName nm : "+ nm + " By : "+login);
        return managmentFiles.researchByName(nm, login);
    }

/*    //copy to videouser
    @GetMapping(value = "/lVneIdToName",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public List<VNELight> lVneIdToName(){
        String login = request.getRemoteUser();
        return managmentFiles.lVneIdToName(login);
    }*/

    @PostMapping(value = "/getVideoFilm/{idMyMediaInfo}",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public VideoFilm getVideoFilm(@RequestBody String mylink,
                                    @PathVariable String idMyMediaInfo){
        log.info("GetVideoFilm mylink : "+ mylink + " idMyMediaInfo : "+idMyMediaInfo);
        return requestWeb.getOneVideoFilm(mylink, idMyMediaInfo);
    }

    @GetMapping(value = "/updatealltitles")
    public void updatealltitles(){
        log.info("Updatealltitles");
        managmentFiles.updatealltitles();
    }
}
