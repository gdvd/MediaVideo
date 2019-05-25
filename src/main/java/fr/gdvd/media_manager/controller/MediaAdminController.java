package fr.gdvd.media_manager.controller;

import fr.gdvd.media_manager.entities.MediaRole;
import fr.gdvd.media_manager.entities.MediaUser;
import fr.gdvd.media_manager.entities.Usr;
import fr.gdvd.media_manager.entities.Usrnewpassword;
import fr.gdvd.media_manager.service.MediaAdminServiceImpl;
import fr.gdvd.media_manager.service.VideoAdminService;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

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
    //////////////////////// GetMapping
    ////// mediaUser

    @GetMapping(value = "/info",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> info(){
//        log.info(request.getHeader("Authorization"));
        /*return new ResponseEntity<>(
                request.getRemoteUser(),
                HttpStatus.ACCEPTED);*/
        return ResponseEntity.status(HttpStatus.OK)
                .body(request.getRemoteUser());
    }

    @GetMapping(value = "/findAllUser",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public List<MediaUser> getAllUser(){
        return mediaAdminService.getAllUser();
    }

    @GetMapping(value = "/findAllUserActive",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public List<MediaUser> getAllUserActive(){
        return mediaAdminService.getAllUserActive();
    }

    @GetMapping(value = "/getOne",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MediaUser getOne(String login){
        return mediaAdminService.getOne(login);
    }

    @GetMapping(value = "/getOneById",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MediaUser getOneById(String id){
        return mediaAdminService.getOneById(id);
    }

    @GetMapping(value = "/udateUserToActive/{login}",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MediaUser udateUserToActive(@PathVariable String login){
        return mediaAdminService.udateUserToActive(login);
    }

    @GetMapping(value = "/udateUserToInactive/{login}",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MediaUser udateUserToInactive(@PathVariable String login){
        return mediaAdminService.udateUserToInactive(login);
    }
    ////// mediaRole
    @GetMapping(value = "/findAllRoles",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public List<MediaRole> findAllRoles(){
        return mediaAdminService.findAllRoles();
    }
    @GetMapping(value = "/findOneRole/{role}",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MediaRole findOneRole(@PathVariable String role){
        return mediaAdminService.findOneRole(role);
    }

    @GetMapping(value = "/changestatus/{id}")
    public void changestatus(@PathVariable String id){
        mediaAdminService.changestatus(id);
    }

    ///////////////////////////////////// PostMapping
    ////// mediaUser

    @PostMapping(value = "/updateUser")
    public MediaUser updateUser(@RequestBody MediaUser mediaUser){
        return mediaAdminService.updateUser(mediaUser);
    }

    @PostMapping(value="/saveUser",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MediaUser saveUser(@RequestBody MediaUser mediaUser,
                              @RequestBody String[] roles){
        return mediaAdminService.saveNewUser(mediaUser, roles);
    }
    ////// mediaRole
    @PostMapping(value="/saveRole",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MediaRole saveRole(@RequestBody MediaRole mediaRole){
        return mediaAdminService.saveRole(mediaRole);
    }

    @PostMapping(value="/addnewrole",
            produces={MediaType.APPLICATION_JSON_VALUE})
    public MediaRole addnewrole(@RequestBody String role){
        return mediaAdminService.addnewrole(role);
    }

    @PostMapping("/uploadFile")
    public void uploadfile(@RequestBody MultipartFile[] uploads){
        int i = 0;
        for(MultipartFile mp: uploads){
            log.info("File received(uploadfile" + ++i + ") "+mp.getSize()+" name : "+mp.getName());
        }
    }
    @PostMapping("/uploadFileWithName")
    public int uploadfileWithName(@RequestBody MultipartFile[] uploads, @RequestParam String names){
        /*int i = 0;
        for(MultipartFile mp: uploads){
            log.info("File received(uploadfile" + ++i + ") "+mp.getSize()+" name : "+mp.getName());
        }*/
        return videoAdminService.saveData(names, uploads);
    }
    @PostMapping("/savenewuser")
    public MediaUser savenewuser(@RequestBody Usr user){
        return mediaAdminService.saveNewUser(user);
    }

    @PostMapping("/changepassworduser")
    public MediaUser updateuserandpassword(@RequestBody Usrnewpassword user){
        return mediaAdminService.updateuserandpassword(user);
    }
    @PostMapping("/changedatauser")
    public MediaUser updateuser(@RequestBody Usr user){
        return mediaAdminService.updateuser(user);
    }

}
