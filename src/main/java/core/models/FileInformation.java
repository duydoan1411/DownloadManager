package core.models;

import controllers.ItemDownloadMonitorController;
import core.helpper.SubFileInformationHelper;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "file_info")
public class FileInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idFile;

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

    @OneToMany(mappedBy = "fileInformation", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubFileInformation> subFileInformationSet = new ArrayList<>();


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
        this.idFile = id;
        this.fileName = fileName;
        this.localPath = localPath;
        this.urlPath = urlPath;
        this.downloaded = downloaded;
        this.numThreads = numThreads;
        this.size = size;
        this.dateTime = dateTime;
        this.status = status;

    }

    public void addSubFileInformation(SubFileInformation subFileInformation){
        this.subFileInformationSet.add(subFileInformation);
    }

    public int getIdFile() {
        return idFile;
    }

    public void setIdFile(int idFile) {
        this.idFile = idFile;
    }

    public List<SubFileInformation> getSubFileInformationSet() {
        return subFileInformationSet;
    }

    public void setSubFileInformationSet(List<SubFileInformation> subFileInformationSet) {
        this.subFileInformationSet = subFileInformationSet;
    }

    public int getId() {
        return idFile;
    }

    public void setId(int id) {
        this.idFile = id;
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
                "id=" + idFile +
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
