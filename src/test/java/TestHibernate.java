import DAO.HibernateUtils;
import core.helpper.FileInformationHelpper;
import core.helpper.SubFileInformationHelper;
import core.models.FileInformation;
import core.models.SubFileInformation;
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
//        FileInformation fileInformation = new FileInformation("123", "fasdfasdf",
//                "dsafsdfasd", 1, 1, new Date());
//        fileInformationHelpper.add(fileInformation);
//
//        SubFileInformationHelper helper = new SubFileInformationHelper();
//        SubFileInformation subFileInformation = new SubFileInformation(10, 5, 20, fileInformation);
//
//        helper.add(subFileInformation);

//        System.out.println("lan 1" + fileInformationHelpper.getAll().toString());
//        fileInformation.setNumThreads(2);
//        fileInformationHelpper.update(fileInformation);
//        System.out.println("lan 2" + fileInformationHelpper.getAll().toString());

        FileInformation fileInformation1 = fileInformationHelpper.get(132);
        //fileInformation1.getSubFileInformationSet().add(subFileInformation);
        //fileInformationHelpper.update(fileInformation1);
        System.out.println(fileInformation1.getSubFileInformationSet().toArray());

    }
}
