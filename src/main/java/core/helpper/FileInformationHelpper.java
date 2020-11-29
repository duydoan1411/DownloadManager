package core.helpper;

import DAO.HibernateUtils;
import core.models.FileInformation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;

import java.util.List;

public class FileInformationHelpper {

    private Session session;

    public FileInformationHelpper() {
        this.session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
    }

    public ObservableList<FileInformation> getAll(){
        if (!session.getTransaction().isActive()){
            session.beginTransaction();
        }
        List<FileInformation> q = session.createQuery("from FileInformation", FileInformation.class).list();
        session.getTransaction().commit();
        System.out.println(q.toString());
        return FXCollections.observableList(q);
    }
    public ObservableList<FileInformation> getAll(String option){
        if (!session.getTransaction().isActive()){
            session.beginTransaction();
        }
        List<FileInformation> q = null;
        if (option.equals("success"))
            q = session.createQuery("from FileInformation where status = 100", FileInformation.class).list();
        else
            if (option.equals("unsuccess"))
                q = session.createQuery("from FileInformation where status < 100", FileInformation.class).list();
            else
                q = session.createQuery("from FileInformation", FileInformation.class).list();
        session.getTransaction().commit();
        System.out.println(q.toString());
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

    public void delete(FileInformation fileInformation){
        if (!session.getTransaction().isActive()){
            session.beginTransaction();
        }
        session.delete(fileInformation);
        session.getTransaction().commit();
    }

}
