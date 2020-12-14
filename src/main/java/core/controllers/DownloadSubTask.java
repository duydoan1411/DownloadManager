package core.controllers;

import controllers.ItemDownloadMonitorController;
import controllers.MainController;
import core.models.DAO.SubFileInformationDAO;
import core.models.DTO.FileInformation;
import core.models.DTO.SubFileInformation;
import javafx.application.Platform;
import javafx.concurrent.Task;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class DownloadSubTask  extends Task<Long> {

    private static final int BUFFER_SIZE = 4096;

    private SubFileInformation subFileInformation;
    private ItemDownloadMonitorController controller;
    private FileInformation fileInformation;
    private long startPosition;
    private long downloaded;
    private long endPosition;
    private HttpURLConnection httpConn;
    private HttpsURLConnection httpsConn;
    private boolean isHTTP = true;
    private boolean stop = false;
    private long totalBytesRead;
    private int bytesRead;

    private final Object object = new Object();

    public DownloadSubTask(SubFileInformation subFileInformation, ItemDownloadMonitorController controller) {
        this.subFileInformation = subFileInformation;
        this.controller = controller;
        this.fileInformation = subFileInformation.getFileInformation();
        this.startPosition = subFileInformation.getStartPosition();
        this.endPosition = subFileInformation.getEndPosition();
        this.downloaded = subFileInformation.getDownloaded();

        try {
            updateMessage("Dang Ket Noi...");
            URL url = new URL(fileInformation.getUrlPath());
            if (url.getProtocol().equals("http")) {
                httpConn = (HttpURLConnection) url.openConnection(); // Open Connection
                httpConn.addRequestProperty("User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                httpConn.setRequestMethod("GET");
                httpConn.setRequestProperty("Range", String.format("bytes=%d-%d", startPosition + downloaded, endPosition));

            } else {
                isHTTP = false;
                TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }};
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new SecureRandom());
                httpsConn = (HttpsURLConnection) url.openConnection();
                httpsConn.setSSLSocketFactory(sc.getSocketFactory());
                httpsConn.addRequestProperty("User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                httpsConn.setRequestMethod("GET");
                httpsConn.setRequestProperty("Range", String.format("bytes=%d-%d", startPosition + downloaded, endPosition));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Long call() throws Exception{
        try
        {
            totalBytesRead = subFileInformation.getDownloaded();
            updateTitle(ItemDownloadMonitorController.getReadableSize(totalBytesRead));

            InputStream inputStream = isHTTP ? httpConn.getInputStream() : httpsConn.getInputStream();

            String home = System.getProperty("user.home");
            File file = new File(home+"/Downloads/"+"Temporary-"+fileInformation.getIdFile()+"-"+fileInformation.getFileName());
            if (!file.exists()){
                file.mkdir();
            }
            String localPath = file.getAbsolutePath()+"/"+fileInformation.getId() + subFileInformation.getIdSubFile();

            FileOutputStream fileOutputStream = new FileOutputStream(localPath, subFileInformation.getDownloaded() > 0);

            byte[] buffer = new byte[BUFFER_SIZE];

            long fileSize = endPosition - startPosition;
            updateMessage("Dang tai xuong...");
            synchronized (object)
            {
                while ((bytesRead = inputStream.read(buffer)) != - 1)
                {
                    if (stop) {
                        break;
                    }
                    else
                    {
                        fileOutputStream.write(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;
                        subFileInformation.setDownloaded(totalBytesRead);
                        updateProgress(totalBytesRead, endPosition - startPosition);
                        updateTitle(ItemDownloadMonitorController.getReadableSize(totalBytesRead));
                        //System.out.println("Sub: " +totalBytesRead/(float)(endPosition - startPosition));
                    }
                }
            }

            fileOutputStream.close();
            inputStream.close();

            if (isHTTP){
                httpConn.disconnect();
            }else {
                httpsConn.disconnect();
            }

            return (long) subFileInformation.getDownloaded();

        }
        catch (IOException e)
        {
            //updateMessage("Khong The Tai File");
        }

        return (long) subFileInformation.getDownloaded();
    }

    @Override
    protected void succeeded() // Calls When Task Finished Successfully
    {
        SubFileInformationDAO subFileInformationDAO = new SubFileInformationDAO();

        subFileInformationDAO.update(subFileInformation);
        if (subFileInformation.getDownloaded() == (endPosition - startPosition + 1)){
            updateProgress(100, 100);
            updateMessage("Da Xong.");
        } else {
            updateMessage("Da dung.");
        }

        //MainController.updateFileList();
    }

    public SubFileInformation getSubFileInformation() {
        return subFileInformation;
    }

    public boolean isStop() {
        return stop;
    }

    public void stop(){
        stop = true;
    }

    public float getProgressDownload() {
        return totalBytesRead/(float)(endPosition - startPosition);
    }

    public long getDownloaded(){
        return totalBytesRead;
    }

    public int getBytesRead() {
        return bytesRead;
    }
}
