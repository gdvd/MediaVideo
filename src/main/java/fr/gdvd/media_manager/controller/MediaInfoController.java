package fr.gdvd.media_manager.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import fr.gdvd.media_manager.entitiesNoDb.Msg;
import fr.gdvd.media_manager.entitiesNoDb.Usr;
import fr.gdvd.media_manager.sec.SecurityParams;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@RequestMapping("")
@Log4j2
@RestController
public class MediaInfoController {

    @Autowired
    private HttpServletRequest request;

    @GetMapping(value={"/infos", "/infos/"}, produces={MediaType.APPLICATION_JSON_VALUE})
    public Msg getInfos(){

        //@Autowired
        //private HttpServletRequest request;
        // Example : request.getHeader("origin")        -> http://localhost:4200
        // Example : request.getHeader("referer")       -> http://localhost:4200/path
        // Example : request.getHeader("user-agent")    -> navigator used
        // Example : request.getRemoteUser()            -> admin
        // Example : request.getUserPrincipal().getName()-> admin
        // Example : request.getServletPath()           -> /path/infos
        // Example : request.getHeader("authorization") -> token ("Baerer xxx…")

        String tk = request.getHeader(SecurityParams.JWT_HEADER);

//        if (tk== null)throw new UsernameNotFoundException("invalid token is null");
//        if (tk.equals(""))throw new UsernameNotFoundException("invalid token equal empty");


        List<String> roles = new ArrayList<>();
        String username = "";
        Date d = null;

        Msg msg = new Msg();
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SecurityParams.PRIVATE_SECRET)).build();
            DecodedJWT decodeJWT = verifier.verify(tk.substring(SecurityParams.TOKEN_PREFIX.length()));
            username = decodeJWT.getSubject();
            roles = decodeJWT.getClaims().get("roles").asList(String.class);
            d = decodeJWT.getExpiresAt();
            msg.setName(username);
            msg.setState("Connected");
            msg.setUrl(request.getHeader("referer"));
            msg.setTokenLimit("The token expire : " + d.toString());
            msg.setNavigator(request.getHeader("user-agent"));
        } catch (Exception e) {
            log.info(e.getMessage());
            if (e.getMessage().contains("The Token has expired on")) {
                log.info("JWTerror : "+e.getMessage());
                msg.setState("Disconnected");
                msg.setUrl(request.getHeader("referer"));
                msg.setInfo(e.getMessage());
                msg.setNavigator(request.getHeader("user-agent"));
            }
        }
        return msg;
    }

}
