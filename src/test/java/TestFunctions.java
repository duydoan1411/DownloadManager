import core.controllers.DownloadManagerTask;
import core.controllers.DownloadSubTask;
import core.models.DAO.FileInformationDAO;
import core.models.DAO.SubFileInformationDAO;
import core.models.DTO.FileInformation;
import core.models.DTO.SubFileInformation;

public class TestFunctions{

    public static void main(String[] args) {
        FileInformationDAO fileInformationDAO =  new FileInformationDAO();
        FileInformation fileInformation = fileInformationDAO.get(117);


        DownloadManagerTask downloadManagerTask = new DownloadManagerTask(fileInformation, null);
        Thread thread = new Thread(downloadManagerTask);
        thread.setDaemon(true);
        thread.start();
    }
}
