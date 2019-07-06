package fr.gdvd.media_manager.controller;

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

@Secured("ADMIN")
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
    //////////////////////// GetMapping
    ////// MyUser

    @GetMapping(value = "/info2",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> info2(){
//        log.info(request.getHeader("Authorization"));
        /*return new ResponseEntity<>(
                request.getRemoteUser(),
                HttpStatus.ACCEPTED);*/
        return ResponseEntity.status(HttpStatus.OK)
                .body(request.getRemoteUser());
    }

    @GetMapping(value = "/findAllUser",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public List<MyUser> getAllUser(){
        return mediaAdminService.getAllUser();
    }

    @GetMapping(value = "/findAllUserActive",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public List<MyUser> getAllUserActive(){
        return mediaAdminService.getAllUserActive();
    }

    @GetMapping(value = "/getOne",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MyUser getOne(String login){
        return mediaAdminService.getOne(login);
    }

    @GetMapping(value = "/getOneById",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MyUser getOneById(String id){
        return mediaAdminService.getOneById(id);
    }

    @GetMapping(value = "/udateUserToActive/{login}",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MyUser udateUserToActive(@PathVariable String login){
        return mediaAdminService.udateUserToActive(login);
    }

    @GetMapping(value = "/udateUserToInactive/{login}",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MyUser udateUserToInactive(@PathVariable String login){
        return mediaAdminService.udateUserToInactive(login);
    }
    ////// MyRole
    @GetMapping(value = "/findAllRoles",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public List<MyRole> findAllRoles(){
        return mediaAdminService.findAllRoles();
    }
    @GetMapping(value = "/findOneRole/{role}",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MyRole findOneRole(@PathVariable String role){
        return mediaAdminService.findOneRole(role);
    }

    @GetMapping(value = "/changestatus/{login}")
    public MyUser changestatus(@PathVariable String login){
        return mediaAdminService.changestatus(login);
    }

    @GetMapping(value = "/getpref")
    public Preferences getpref(){
        return adminPreferences.getpref();
    }

    ///////////////////////////////////// PostMapping
    ////// MyUser

    @PostMapping(value = "/updateUser")
    public MyUser updateUser(@RequestBody MyUser MyUser){
        return mediaAdminService.updateUser(MyUser);
    }

    @PostMapping(value="/saveUser",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MyUser saveUser(@RequestBody MyUser MyUser,
                              @RequestBody String[] roles){
        return mediaAdminService.saveNewUser(MyUser, roles);
    }
    ////// MyRole
    @PostMapping(value="/saveRole",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MyRole saveRole(@RequestBody MyRole MyRole){
        return mediaAdminService.saveRole(MyRole);
    }

    @PostMapping(value="/addnewrole",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MyRole addnewrole(@RequestBody String role){
        return mediaAdminService.addnewrole(role);
    }

    @PostMapping(value="/uploadFile",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public void uploadfile(@RequestBody MultipartFile[] uploads){
        int i = 0;
        for(MultipartFile mp: uploads){
            log.info("File received(uploadfile" + ++i + ") "+mp.getSize()+" name : "+mp.getName());
        }
    }
    /*@PostMapping("/uploadFileWithPathdir/{pahtdirids}")
    public StateImport uploadfileWithName(@RequestBody MultipartFile[] uploads, @PathVariable String pahtdirids){
        String pahtdiridsdecode = new String(Base64.getDecoder().decode(pahtdirids));
        return videoAdminService.saveData(pahtdiridsdecode, uploads, request.getRemoteUser());
    }*/
    @PostMapping("/savenewuser")
    public MyUser savenewuser(@RequestBody Usr user){
        return mediaAdminService.saveNewUser(user);
    }

    @PostMapping("/changepassworduser")
    public MyUser updateuserandpassword(@RequestBody Usrnewpassword user){
        return mediaAdminService.updateuserandpassword(user);
    }
    @PostMapping("/changedatauser")
    public MyUser updateuser(@RequestBody Usr user){
        return mediaAdminService.updateuser(user);
    }

}
