package core.models.DAO;

import core.models.DTO.FileInformation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;

import java.util.List;

public class FileInformationDAO {

    private Session session;

    public FileInformationDAO() {
    }

    public ObservableList<FileInformation> getAll(){
        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        List<FileInformation> q = session.createQuery("from FileInformation", FileInformation.class).list();
        session.getTransaction().commit();
        System.out.println(q.toString());
        session.close();
        return FXCollections.observableList(q);
    }
    public ObservableList<FileInformation> getAll(String option){
        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
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
        session.close();
        return FXCollections.observableList(q);
    }

    public FileInformation get(int id){
        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        String query = "from FileInformation where idFile = :id";
        List<FileInformation> q = session.createQuery(query, FileInformation.class).setParameter("id", id).list();
        session.close();
        return !q.isEmpty() ? q.get(0) : null;
    }

    public int add(FileInformation fileInformation){
        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        int id = (int) session.save(fileInformation);
        session.getTransaction().commit();
        session.close();
        return id;
    }

    public void update(FileInformation fileInformation){
        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(fileInformation);
        session.getTransaction().commit();
        session.close();
    }

    public void delete(FileInformation fileInformation){
        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(fileInformation);
        session.getTransaction().commit();
        session.close();
    }

}
