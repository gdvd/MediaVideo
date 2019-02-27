package fr.gdvd.media_manager;

import com.sun.codemodel.internal.JForEach;
import fr.gdvd.media_manager.dao.MediaConfigRepository;
import fr.gdvd.media_manager.dao.MediaVideoRepository;
import fr.gdvd.media_manager.entities.MediaConfig;
import fr.gdvd.media_manager.entities.MediaVideo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class MediaManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediaManagerApplication.class, args);
    }

//    @Bean // Test reading db
//    CommandLineRunner start(MediaVideoRepository mediaVideoRepository, MediaConfigRepository mediaConfigRepository) {
//        return args -> {
//            List<MediaConfig> list = mediaConfigRepository.findAll();
//            for (MediaConfig mv: list) {
//                System.out.println(mv.getId());
//                for (Map mp: mv.getPath()){
//                    System.out.println(mp.keySet());
//                    for (Object str: mp.values()){
//                        System.out.println("\t"+str);
//                    }
//                }
//            }
//            List<MediaVideo> mv = mediaVideoRepository.findAll();
//            for (MediaVideo mvo: mv){
//                Map<String, Object> map = mvo.getVideo();
//                for (Map.Entry<String, Object> o : map.entrySet()) {
//                    if(o.getKey().equals("Format")){
//                        System.out.print(o.getValue().toString());
//                    }
//                    if(o.getKey().equals("Sampled_Width")){
//                        System.out.print(" and "+o.getValue().toString());
//                    }
//                    if(o.getKey().equals("Sampled_Height")){
//                        System.out.println(" * "+o.getValue().toString() + o.getValue().getClass());
//                    }
//                }
//            }
//        };
//    }
}
