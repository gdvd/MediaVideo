package fr.gdvd.media_manager.service;

import fr.gdvd.media_manager.entities.FileVideo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
@Service
public class ManagmentFilesImpl implements ManagmentFiles {

    //    @Async
    @Override
    public List<FileVideo> scanFolder(String urlFolder) {
        List<FileVideo> fileVideo = new ArrayList<>();
//        DirectoryChooser directoryChooser = new DirectoryChooser();
//        directoryChooser.setTitle("Choose Directory");
//        File fileDir = directoryChooser.showDialog(null);
        if (urlFolder != null) {
//            String path = urlFolder.getPath();
            List<String> extensions = Arrays.asList("mp4", "avi", "mov", "mkv", "webm");
            List<String> files = listAllDirectories(urlFolder, extensions);
            for (String f : files) {
                FileVideo video = new FileVideo(/*f, true*/);
                video.setPath(f);
                video.setActivate(true);
                if (video.isActivate()) {
                    video.setId(setMd5(f));
                    fileVideo.add(video);
                }
            }
        }
        return fileVideo;
    }

    private String setMd5(String file) {
        String strMd5 = "";
        File f = new File(file);

        long lenghtFile = f.length();
        if (lenghtFile != 0) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(f);
                strMd5 = DigestUtils.md5Hex(fis);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            log.info("***** The file is empty (" + file + ")");
        }
        return strMd5;
    }

    private List<String> listAllDirectories(String dir, List<String> extensions) {
        List<String> files = new ArrayList<>();
        listDirectory(dir, extensions, files);
        return files;
    }

    private void listDirectory(String dir, List<String> extensions, List<String> files) {
        File file = new File(dir);
        File[] fs = file.listFiles();
        if (fs != null) {
            for (File f : fs) {
                if (f.length() > 5000000) {
                    String ext = FilenameUtils.getExtension(f.getName());
                    if (extensions.contains(ext.toLowerCase())) {
                        files.add(f.getAbsolutePath());
                    } else {
                        log.info("***** Extension " + ext + " non lue !");
                    }
                }
                if (f.isDirectory()) {
                    String newDir = f.getAbsolutePath();
                    listDirectory(newDir, extensions, files);
                }
            }
        }
    }
}
