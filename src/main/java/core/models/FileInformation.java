package core.models;

import java.util.Date;

public class FileInformation {
    private String fileName, localPath, urlPath;
    private int numThreads;
    private long size;
    private Date dateTime;
    private double status;

    public FileInformation(String fileName, String localPath, String urlPath, int numThreads, long size, Date dateTime) {
        this.fileName = fileName;
        this.localPath = localPath;
        this.urlPath = urlPath;
        this.numThreads = numThreads;
        this.size = size;
        this.dateTime = dateTime;
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
}
