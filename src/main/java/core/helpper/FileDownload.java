package core.helpper;

import core.models.FileInformation;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileDownload {

    private FileInformation fileInformation;

    public FileDownload(String url1) {
        try {
            URL url = new URL(url1);
            getFileInformation(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileInformation getFileInformation() {
        return fileInformation;
    }

    private void getFileInformation(URL url) throws IOException {

        //default fileName
        String raftName[] = url.getFile().split("/");
        String fileName = raftName[raftName.length-1];

        //default localPath
        String home = System.getProperty("user.home");
        String localPath = home+"/Downloads/";

        //urlPath
        String urlPath = url.toString();

        //default numThreads
        int numThreads = 1;

        //default size
        long size = 0;

        //datetime
        Date date = new Date();

        if (url.getProtocol().equals("https")) {

            HttpsURLConnection conn = null;
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new SecureRandom());
                conn = (HttpsURLConnection) url.openConnection();
                conn.setSSLSocketFactory(sc.getSocketFactory());

                conn.addRequestProperty("User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                conn.setRequestMethod("GET");
                conn.getInputStream();
                Pattern p = Pattern.compile(".+filename=\"(.+?)\".*");
                String contentDisposition = conn.getHeaderField("Content-Disposition");
                if(contentDisposition != null) {
                    Matcher m = p.matcher(contentDisposition);
                    if(m.find()) {
                        fileName = m.group(1);
                    }
                }
                size = conn.getContentLengthLong();
            } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
                throw new RuntimeException(e);
            } finally {
                assert conn != null;
                conn.disconnect();
            }
        } else {
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.addRequestProperty("User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                ((HttpURLConnection) conn).setRequestMethod("GET");
                conn.getInputStream();
                Pattern p = Pattern.compile(".+filename=\"(.+?)\".*");
                String contentDisposition = conn.getHeaderField("Content-Disposition");
                if(contentDisposition != null) {
                    Matcher m = p.matcher(contentDisposition);
                    if(m.find()) {
                        fileName = m.group(1);
                    }
                }
                size = conn.getContentLengthLong();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                assert conn != null;
                conn.disconnect();
            }
        }

        fileInformation = new FileInformation(fileName, localPath, urlPath, numThreads, size, date);
    }
//
//    public static FileInformation getFileInformation(URL url){
//
//    }
}
