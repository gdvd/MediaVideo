package fr.gdvd.media_manager.controller;

import fr.gdvd.media_manager.service.VideoAdminService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@Controller
@RequestMapping(value = "home")
public class AdminController {

    /*@Autowired
    private HttpServletRequest request;
    @Autowired
    private VideoAdminService adminService;

    @GetMapping(value = "/indexadmin")
    public String indexadmin(Model model) {
        String urlids = "~/MediaVideo/";
        model.addAttribute("urlids", urlids);
        model.addAttribute("msg", "");
        return isLocalhost()?"indexadmin":"oneerror";
    }

    @GetMapping("/greeting")
    public String greeting( @RequestParam(name = "name",
            required = false,
            defaultValue = "World") String name,
                            Model model) {
        model.addAttribute("name", name);
        return isLocalhost()?"greeting":"oneerror";
    }
    @PostMapping(value = "/upload")
    public String upload(
            @RequestParam(value="pahtdirids", required=true) String pahtdirids,
            @RequestParam("file") MultipartFile[] file,
            Model model) {*/

        /*List<List<Map<String, String>>> llms = new ArrayList<>();
        for(MultipartFile f: file){
            File fi = convert(f);
            String content = "";
            try{
                content = new String ( Files.readAllBytes(Paths.get(fi.getAbsolutePath())) );
            }
            catch (IOException e){
                e.printStackTrace();
            }
            List<String> ls = parser.findAllTagsInString("<item>","</item>", content, false);
            List<Map<String, String>> lms = new ArrayList<>();

            *//*Map<String, String> msfn = new HashMap<>();
            msfn.put("filename",(fi.getName()).replace(".xml", ""));
            lms.add(msfn);*//*

            for(String str: ls){
                Map<String, String> ms = new HashMap<>();
                String id = parser.findTagInString("<id>", "</id>", str, false);
                String path = parser.findTagInString("<path>", "</path>", str, false);
                ms.put("id", id);
                ms.put("path", path);
                lms.add(ms);
            }
            llms.add(lms);
        }
        adminService.saveData(llms, pahtdirids);*/

        /*int llmsSize = adminService.saveData(pahtdirids, file);

        String urlids = "~/MediaVideo/";
        model.addAttribute("urlids", urlids);
        model.addAttribute("msg", llmsSize+ " file(s) insert");
        return "indexadmin";
    }

    //Test if urlRequest is http://localhost/...
    private boolean isLocalhost(){
        return request.getLocalName().equals("localhost");
    }*/

}
