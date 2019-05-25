package fr.gdvd.media_manager.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping(value = "information")
public class ErrorController {

    @GetMapping(value= {"/404", "/404.html", "/error", "/error.html"})
    public String errorpage(){
        return "oneerror";
    }

}
