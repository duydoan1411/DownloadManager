package core.helpper;

import DAO.HibernateUtils;
import core.models.FileInformation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.List;

public class FileInformationHelpper {

    private Session session;

    public FileInformationHelpper() {
        this.session = HibernateUtils.getSessionFactory().openSession();
    }

    public ObservableList<FileInformation> getAll(){
        if (!session.getTransaction().isActive()){
            session.beginTransaction();
        }
        List<FileInformation> q = session.createQuery("from FileInformation", FileInformation.class).list();
        session.getTransaction().commit();
        return FXCollections.observableList(q);
    }

    public int add(FileInformation fileInformation){
        if (!session.getTransaction().isActive()){
            session.beginTransaction();
        }
        int id = (int) session.save(fileInformation);
        session.getTransaction().commit();
        return id;
    }

    public void update(FileInformation fileInformation){
        if (!session.getTransaction().isActive()){
            session.beginTransaction();
        }
        session.update(fileInformation);
        session.getTransaction().commit();
    }

}
