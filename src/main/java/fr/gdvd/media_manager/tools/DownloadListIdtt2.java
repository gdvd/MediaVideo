package fr.gdvd.media_manager.tools;

import fr.gdvd.media_manager.service.RequestWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class DownloadListIdtt2 {
    @Autowired
    private RequestWeb requestWeb;
    private List<String> lidttToDo;

    public void run() {
        if (lidttToDo.size() != 0) {
            System.out.println("run with list todo");
            searchAll();
        } else {
            System.out.println("run with list todo size 0");
        }
    }

    private void searchAll() {
        System.out.println("lidttToDo.size() : "+lidttToDo.size());
        for (int i = lidttToDo.size() - 1; i >= 0; i--) {
            int r = new Random().nextInt((2) + 1);
            try {
                Thread.sleep((3 + r) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // EXECUTE
//            System.out.println("Download idTT n# : "+i+" and idTt : "+lidttToDo.get(i));
            requestWeb.getOneVideoFilm(lidttToDo.get(i), "");
        }
    }
}
