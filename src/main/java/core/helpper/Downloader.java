/*
 *  In The Name of ALLAH
 *
 *  Written by: Mortaza Seydi - Zanjan University - Spring 2018
 *
 *  Internet Download Manager (IDM)
 *
 *  Github : https://github.com/Mortaza-Seydi
 *
 */

package core.helpper;

import controllers.ItemDownloadMonitorController;
import controllers.MainController;
import core.models.FileInformation;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.Stage;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.SQLException;

public class Downloader extends Task<Long>
{
    private static final int BUFFER_SIZE = 4096;

    private HttpURLConnection httpConn;
    private HttpsURLConnection httpsConn;
    private FileInformation download;
    private long startPostion;
    private boolean isHTTP = true;

    private final Object object = new Object(); // Monitor
    private boolean pause = false;
    private boolean stop = false;
    private ItemDownloadMonitorController controller;
    private FileInformationHelpper fileInformationHelpper;

    public Downloader(FileInformation downloadedFile, long startPosition, ItemDownloadMonitorController controller)
    {
        this.controller = controller;
        this.startPostion = startPosition;
        this.download = downloadedFile;
        this.fileInformationHelpper = new FileInformationHelpper();
        try
        {
            updateMessage("Dang Ket Noi...");
            URL url = new URL(downloadedFile.getUrlPath());
            if (url.getProtocol().equals("http")) {
                httpConn = (HttpURLConnection) url.openConnection(); // Open Connection
                httpConn.addRequestProperty("User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                httpConn.setRequestMethod("GET");
                httpConn.setRequestProperty("Range", String.format("bytes=%d-", startPosition));

            }else {
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
                httpsConn.setRequestProperty("Range", String.format("bytes=%d-", startPosition));
            }
        }
        catch (IOException | KeyManagementException | NoSuchAlgorithmException e)
        {
            updateMessage("Da xay ra loi ket noi.");
        }
    }

    @Override
    protected Long call()
    {
        try
        {

            InputStream inputStream = isHTTP ? httpConn.getInputStream() : httpsConn.getInputStream();

            FileOutputStream fileOutputStream = new FileOutputStream(download.getLocalPath()+download.getFileName());

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            long totalBytesRead = 0;
            int fileSize = isHTTP ? httpConn.getContentLength() : httpsConn.getContentLength();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    controller.labelFileSize.setText(ItemDownloadMonitorController.getReadableSize(fileSize));
                }
            });

            updateMessage("Dang Tai Xuong...");
            String downloadSpeed, timeRemain;
            long startTime = System.currentTimeMillis();

            synchronized (object)
            {
                while ((bytesRead = inputStream.read(buffer)) != - 1) // Read The Bytes
                {
                    if (pause) {
                        if (stop) {
                            break;
                        }
                        object.wait(); // Wait Thread
                    }
                    else
                    {
                        fileOutputStream.write(buffer, 0, bytesRead); // Write To File
                        totalBytesRead += bytesRead;
                        download.setDownloaded(totalBytesRead);
                        long finalTotalBytesRead = totalBytesRead;
                        download.setStatus((totalBytesRead * 100) / fileSize); // Progress
                        updateProgress(download.getStatus(), 100);
                        downloadSpeed = String.format("%.2f MB/s", (float) (((float)totalBytesRead / 1024) / 1024) / (float) ((System.currentTimeMillis() - startTime) / 1000));
                        timeRemain = ItemDownloadMonitorController.getTimeRemain((int) (((float)(fileSize - totalBytesRead)) / ((float)(totalBytesRead / ((float)(System.currentTimeMillis() - startTime) / 1000)))));
                        String finalTimeRemain = timeRemain;
                        String finalDownloadSpeed = downloadSpeed;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                controller.labelDownloaded.setText(ItemDownloadMonitorController.getReadableSize(finalTotalBytesRead));
                                controller.labelRemainTime.setText(finalTimeRemain);
                                controller.labelSpeed.setText(finalDownloadSpeed);
                            }
                        });
                    }
                }
            }

            //updateMessage("Hoan Tat");
            /*
             * Close Streams And Connections
             */

            fileOutputStream.close();
            inputStream.close();

            if (isHTTP){
                httpConn.disconnect();
            }else {
                httpsConn.disconnect();
            }

            return (long) download.getDownloaded();

        }
        catch (IOException e)
        {
            updateMessage("Khong The Tai File");
        }
        catch (InterruptedException e)
        {
            updateMessage("Khong The Tam Dung Hoac Tiep Tuc");
        }

        return (long) download.getDownloaded();
    }

    @Override
    protected void succeeded() // Calls When Task Finished Successfully
    {
        fileInformationHelpper.update(download);
        updateMessage("Hoan Tat");
        MainController.updateFileList();
        controller.getStage().close();

    }

    public void pause()
    {
        pause = true; // The Thread States Flag

        fileInformationHelpper.update(download);
        MainController.updateFileList();
    }

    public void resume()
    {
        pause = false; // The Thread States Flag

        synchronized (object)
        {
            object.notify(); // Resume Thread
        }
    }

    public boolean isPause()
    {
        return pause;
    }

    public boolean isStop() {
        return stop;
    }

    public void stop(){
        pause();
        stop = true;
    }
}