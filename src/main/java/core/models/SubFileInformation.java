package core.models;

import javax.persistence.*;

@Entity
@Table(name = "sub_file_info")
public class SubFileInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSubFile;

    @Column
    private long startPosition;

    @Column
    private long downloaded;

    @Column
    private long endPosition;

    @ManyToOne
    @JoinColumn(name = "idFile")
    private FileInformation fileInformation;

    public SubFileInformation(int idSubFile, long startPosition, long downloaded, long endPosition, FileInformation fileInformation) {
        this.idSubFile = idSubFile;
        this.startPosition = startPosition;
        this.downloaded = downloaded;
        this.endPosition = endPosition;
        this.fileInformation = fileInformation;
    }

    public SubFileInformation(long startPosition, long downloaded, long endPosition, FileInformation fileInformation) {
        this.startPosition = startPosition;
        this.downloaded = downloaded;
        this.endPosition = endPosition;
        this.fileInformation = fileInformation;
    }

    public SubFileInformation(long startPosition, long downloaded, long endPosition) {
        this.startPosition = startPosition;
        this.downloaded = downloaded;
        this.endPosition = endPosition;
    }

    public SubFileInformation() {
    }

    public int getIdSubFile() {
        return idSubFile;
    }

    public void setIdSubFile(int idSubFile) {
        this.idSubFile = idSubFile;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(long startPosition) {
        this.startPosition = startPosition;
    }

    public long getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(long downloaded) {
        this.downloaded = downloaded;
    }

    public long getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(long endPosition) {
        this.endPosition = endPosition;
    }

    public FileInformation getFileInformation() {
        return fileInformation;
    }

    public void setFileInformation(FileInformation fileInformation) {
        this.fileInformation = fileInformation;
    }

    @Override
    public String toString() {
        return "SubFileInformation{" +
                "idSubFile=" + idSubFile +
                ", startPosition=" + startPosition +
                ", downloaded=" + downloaded +
                ", endPosition=" + endPosition +
                ", fileInformation=" + fileInformation +
                '}';
    }
}
