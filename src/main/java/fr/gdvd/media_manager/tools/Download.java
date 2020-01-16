package fr.gdvd.media_manager.tools;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
@Log4j2
public class Download {

    public boolean downloadNow(URL url, File localFilename) throws IOException {
        boolean test = false;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            URLConnection urlConn = url.openConnection();
            urlConn.setRequestProperty("User-Agent", "Mozilla/5.0");

            is = urlConn.getInputStream();
            urlConn.setConnectTimeout(10000);
            urlConn.setReadTimeout(10000);
            fos = new FileOutputStream(localFilename);
            byte[] buffer = new byte[4096];
            int len;

            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            test=true;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }
        return test;
    }


    public void downloadImage(String stringUrl, String stringFile) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            URL url = new URL(stringUrl);
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            inputStream = con.getInputStream();
            outputStream = new FileOutputStream(stringFile);
            byte[] buffer = new byte[2048];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        } catch (Exception ex) {
        }
        if(outputStream!=null){
            outputStream.close();
        }
        if(outputStream!=null){
            inputStream.close();
        }

    }


    public String download2String(URL url) throws IOException {
        // Realise la connection a l'URL
        HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
        urlconn.setRequestProperty("User-Agent", "Mozilla/5.0");
        urlconn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        urlconn.setConnectTimeout(10000);
        urlconn.setReadTimeout(10000);
        urlconn.setRequestMethod("GET");
        urlconn.setDoOutput(true);
        urlconn.connect();

        int response = urlconn.getResponseCode();
        BufferedWriter bw = null;
        BufferedReader buf = null;
        String res = "";
        if (response == 200) { // Ecrit le fichier
            buf = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
            String restmp = "";
            while ((restmp = buf.readLine()) != null) {
                res = res + restmp;
            }
        }
        if (buf != null)
            buf.close();
        if (bw != null)
            bw.close();
        return res;
    }

    public int download2File(URL url, File fileOut) throws IOException {
        // Realise la connection a l'URL
        HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
        urlconn.setRequestProperty("User-Agent", "Mozilla/5.0");
        urlconn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        urlconn.setConnectTimeout(10000);
        urlconn.setReadTimeout(10000);
        urlconn.setRequestMethod("GET");
        urlconn.setDoOutput(true);
        urlconn.connect();

        int response = urlconn.getResponseCode();
        BufferedWriter bw = null;
        BufferedReader buf = null;
        if (response == 200) { // Ecrit le fichier
            buf = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
            fileOut.createNewFile();
            bw = new BufferedWriter(new FileWriter(fileOut));
            String line;
            while ((line = buf.readLine()) != null) {
                bw.write(line + "\n");
            }
        }
        if (buf != null)
            buf.close();
        if (bw != null)
            bw.close();
        return response;
    }
    public String readFile(File f)
    {
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(f.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(encoded, StandardCharsets.UTF_8);
    }
}
