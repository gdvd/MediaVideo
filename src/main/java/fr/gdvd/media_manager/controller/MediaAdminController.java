package fr.gdvd.media_manager.controller;

import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesMysql.Preferences;
import fr.gdvd.media_manager.entitiesNoDb.StateImport;
import fr.gdvd.media_manager.entitiesNoDb.Usr;
import fr.gdvd.media_manager.entitiesNoDb.Usrnewpassword;
import fr.gdvd.media_manager.entitiesMysql.MyRole;
import fr.gdvd.media_manager.entitiesMysql.MyUser;
import fr.gdvd.media_manager.service.AdminPreferences;
import fr.gdvd.media_manager.service.MediaAdminServiceImpl;
import fr.gdvd.media_manager.service.VideoAdminService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.List;

//@Secured("ADMIN")
@Log4j2
@RestController
@RequestMapping(value = "admin")
public class MediaAdminController {

    @Autowired
    private MediaAdminServiceImpl mediaAdminService;
    @Autowired
    private VideoAdminService videoAdminService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private AdminPreferences adminPreferences;


/*    @GetMapping(value = "/info2",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> info2(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(request.getRemoteUser());
    }*/

    @GetMapping(value = "/findAllUser",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public List<MyUser> getAllUser(){
        log.info("FindAllUser");
        return mediaAdminService.getAllUser();
    }

    @GetMapping(value = "/findAllUserActive",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public List<MyUser> getAllUserActive(){
        log.info("getAllUserActive");
        return mediaAdminService.getAllUserActive();
    }

    @GetMapping(value = "/getOne",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MyUser getOne(String login){
        log.info("GetOne by : "+login);
        return mediaAdminService.getOne(login);
    }

    @GetMapping(value = "/getOneById",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MyUser getOneById(String id){
        log.info("GetOneById id : "+id);
        return mediaAdminService.getOneById(id);
    }

    @GetMapping(value = "/udateUserToActive/{login}",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MyUser udateUserToActive(@PathVariable String login){
        log.info("UdateUserToActive by : "+login);
        return mediaAdminService.udateUserToActive(login);
    }

    @GetMapping(value = "/udateUserToInactive/{login}",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MyUser udateUserToInactive(@PathVariable String login){
        log.info("udateUserToInactive by : "+login);
        return mediaAdminService.udateUserToInactive(login);
    }
    ////// MyRole
    @GetMapping(value = "/findAllRoles",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public List<MyRole> findAllRoles(){
        log.info("FindAllRoles");
        return mediaAdminService.findAllRoles();
    }

    @GetMapping(value = "/findOneRole/{role}",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MyRole findOneRole(@PathVariable String role){
        log.info("FindOneRole : "+role);
        return mediaAdminService.findOneRole(role);
    }

    @GetMapping(value = "/changestatus/{login}")
    public MyUser changestatus(@PathVariable String login){
        log.info("Change Status by : "+login);
        return mediaAdminService.changestatus(login);
    }

    @GetMapping(value = "/getpref")
    public Preferences getpref(){
        log.info("GetPref");
        return adminPreferences.getpref();
    }


    @GetMapping(value = "/getpreftitle")
    public Preferences getpreftitle(){
        log.info("GetPrefTitle");
        return adminPreferences.getpreftitle();
    }


    @GetMapping(value = "/getprefbackupMo")
    public Preferences getprefbackupMo(){
        log.info("GetPrefbackupMo");
        return adminPreferences.getprefbackupMo();
    }


    @GetMapping(value = "/getprefbackupSc")
    public Preferences getprefbackupSc(){
        log.info("GetPrefbackupSc");
        return adminPreferences.getprefbackupSc();
    }

    @PostMapping(value = "/updateUser")
    public MyUser updateUser(@RequestBody MyUser myUser){
        log.info("udateUser by : "+myUser.getLogin());
        return mediaAdminService.updateUser(myUser);
    }

    @PostMapping(value="/saveUser",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MyUser saveUser(@RequestBody MyUser myUser,
                              @RequestBody String[] roles){
        log.info("SaveRole by : "+myUser.getLogin());
        return mediaAdminService.saveNewUser(myUser, roles);
    }
    ////// MyRole
    @PostMapping(value="/saveRole",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MyRole saveRole(@RequestBody MyRole myRole){
        log.info("SaveRole : "+myRole.getRole());
        return mediaAdminService.saveRole(myRole);
    }

    @PostMapping(value="/addnewrole",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MyRole addnewrole(@RequestBody String role){
        log.info("AddNewRole : "+role);
        return mediaAdminService.addnewrole(role);
    }

    @PostMapping("/savenewuser")
    public MyUser savenewuser(@RequestBody Usr user){
        log.info("SaveNewUser by : "+user.getLogin());
        return mediaAdminService.saveNewUser(user);
    }

    @PostMapping("/changepassworduser")
    public MyUser updateuserandpassword(@RequestBody Usrnewpassword user){
        log.info("ChangePasswordUser by : "+user.getLogin());
        return mediaAdminService.updateuserandpassword(user);
    }

    @PostMapping("/changedatauser")
    public MyUser updateuser(@RequestBody Usr user){
        log.info("ChangeDataUser by : "+user.getLogin());
        return mediaAdminService.updateuser(user);
    }

    @GetMapping(value="/toggleactiveidMmi/{idMmi}",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MyMediaInfo toggleactiveidMmi(@PathVariable String idMmi){
        String login = request.getRemoteUser();
        if(! login.equals("admin")) throw new RuntimeException("You must be logged in admin");
        log.info(login + " ===> toggleactiveidMmi idMmi : "+idMmi);
        return videoAdminService.toggleactiveidMmi(idMmi);
    }

    @GetMapping(value = "/getValuesScheduledTask",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public Preferences getValuesScheduledTask(){
        return adminPreferences.getValuesScheduledTask();
    }

    @GetMapping(value = "/postnewfrequencymovie/{frequency}",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public Preferences postnewfrequency(@PathVariable int frequency){
        return adminPreferences.postnewfrequency(frequency);
    }

    @GetMapping(value = "/postnewfrequencyscore/{frequency}",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public Preferences postnewfrequencyscore(@PathVariable int frequency){
        return adminPreferences.postnewfrequencyscore(frequency);
    }

    @GetMapping(value = "/getPrefSubscribe",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public Preferences getPrefSubscribe(){
        return null;
    }


    @GetMapping(value = "/getlisttoexport",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public List<String> getlisttoexport(){
        return adminPreferences.getlisttoexport();
    }

    @PostMapping(value = "/executeexport",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public int executeexport(@RequestBody String nameExport){
        return adminPreferences.executeexport(nameExport);
    }



}

