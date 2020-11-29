package core.models;

import controllers.ItemDownloadMonitorController;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "file_info")
public class FileInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String fileName;

    @Column
    private String localPath;

    @Column
    private String urlPath;

    @Column
    private long downloaded;

    @Column
    private int numThreads;

    @Column
    private long size;

    @Column
    private Date dateTime;

    @Column
    private double status;


    public FileInformation() {

    }

    public FileInformation(String fileName, String localPath, String urlPath, int numThreads, long size, Date dateTime) {
        this.fileName = fileName;
        this.localPath = localPath;
        this.urlPath = urlPath;
        this.numThreads = numThreads;
        this.size = size;
        this.dateTime = dateTime;
        downloaded = 0;

    }

    public FileInformation(String fileName, String localPath, String urlPath, int numThreads, long size, Date dateTime, long downloaded) {
        this.fileName = fileName;
        this.localPath = localPath;
        this.urlPath = urlPath;
        this.numThreads = numThreads;
        this.size = size;
        this.dateTime = dateTime;
        this.downloaded = downloaded;

    }

    public FileInformation(int id, String fileName, String localPath, String urlPath, long downloaded, int numThreads, long size, Date dateTime, double status) {
        this.id = id;
        this.fileName = fileName;
        this.localPath = localPath;
        this.urlPath = urlPath;
        this.downloaded = downloaded;
        this.numThreads = numThreads;
        this.size = size;
        this.dateTime = dateTime;
        this.status = status;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(long downloaded) {
        this.downloaded = downloaded;
    }

    public String getSizeCanRead() {
        return ItemDownloadMonitorController.getReadableSize(this.size);
    }

    public String getDownloadedCanRead(){
        return ItemDownloadMonitorController.getReadableSize(this.downloaded);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getStatusCanRead(){
        if (status < 100){
            return "Chua Hoan Thanh";
        }
        return "Hoan Thanh";
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public double getStatus() {
        return status;
    }

    public void setStatus(double status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FileInformation{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", localPath='" + localPath + '\'' +
                ", urlPath='" + urlPath + '\'' +
                ", downloaded=" + downloaded +
                ", numThreads=" + numThreads +
                ", size=" + size +
                ", dateTime=" + dateTime +
                ", status=" + status +
                '}';
    }
}
