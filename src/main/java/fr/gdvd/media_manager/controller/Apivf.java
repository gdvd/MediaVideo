package fr.gdvd.media_manager.controller;

import fr.gdvd.media_manager.entitiesNoDb.Msg;
import fr.gdvd.media_manager.service.ApivfService;
import fr.gdvd.media_manager.service.ApivfServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@RestController
@RequestMapping(value = "apivf")
public class Apivf {

    @Autowired
    private ApivfService apivfService;

    @GetMapping(value = {"/getrss/{apikey}/{quatity}/{loginRequest}"},
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE})
    public String getrss(@PathVariable String apikey,
                         @PathVariable int quatity,
                         @PathVariable String loginRequest) {
        return apivfService.getrss(apikey, quatity, loginRequest);
    }

}
