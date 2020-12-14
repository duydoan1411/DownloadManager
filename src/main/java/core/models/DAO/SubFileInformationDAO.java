package core.models.DAO;

import core.models.DTO.FileInformation;
import core.models.DTO.SubFileInformation;
import org.hibernate.Session;

import java.util.List;

public class SubFileInformationDAO {
    private Session session;

    public SubFileInformationDAO() {

    }

    public int add(SubFileInformation subFileInformation){
        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        int id = (int) session.save(subFileInformation);
        session.getTransaction().commit();
        return id;
    }

    public void update(SubFileInformation subFileInformation){
        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(subFileInformation);
        session.getTransaction().commit();
        session.close();
    }

    public SubFileInformation get(int id){
        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        String query = "from SubFileInformation where idSubFile = :id";
        List<SubFileInformation> q = session.createQuery(query, SubFileInformation.class).setParameter("id", id).list();
        session.close();
        return !q.isEmpty() ? q.get(0) : null;
    }
}
