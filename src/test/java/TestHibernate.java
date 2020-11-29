import DAO.HibernateUtils;
import core.helpper.FileInformationHelpper;
import core.models.FileInformation;
import org.hibernate.Session;

import java.util.Date;

public class TestHibernate {
    public static void main(String[] args) {
//        try (Session session = HibernateUtils.getSessionFactory().openSession()){
//            session.beginTransaction();
//            FileInformation fileInformation = new FileInformation("123", "fasdfasdf",
//                    "dsafsdfasd", 1, 1, new Date());
//            int id = (int) session.save(fileInformation);
//
//            System.out.println(id);
//            session.getTransaction().commit();
//        }

        FileInformationHelpper fileInformationHelpper = new FileInformationHelpper();
        FileInformation fileInformation = new FileInformation("123", "fasdfasdf",
                "dsafsdfasd", 1, 1, new Date());
        fileInformationHelpper.add(fileInformation);

        System.out.println("lan 1" + fileInformationHelpper.getAll().toString());
        fileInformation.setNumThreads(2);
        fileInformationHelpper.update(fileInformation);
        System.out.println("lan 2" + fileInformationHelpper.getAll().toString());

    }
}
