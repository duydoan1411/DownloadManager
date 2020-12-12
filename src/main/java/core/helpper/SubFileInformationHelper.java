package core.helpper;

import DAO.HibernateUtils;
import core.models.FileInformation;
import core.models.SubFileInformation;
import org.hibernate.Session;

public class SubFileInformationHelper {
    private Session session;

    public SubFileInformationHelper() {
        this.session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
    }

    public int add(SubFileInformation subFileInformation){
        if (!session.getTransaction().isActive()){
            session.beginTransaction();
        }
        int id = (int) session.save(subFileInformation);
        session.getTransaction().commit();
        return id;
    }
}
