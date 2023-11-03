package polyu.learn.bbssoadfs.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import polyu.learn.bbssoadfs.operation.sis.SISIntgrLog;

public class SISIntgrLogUtils {
	private final static Logger LOGGER = LoggerFactory.getLogger(SISIntgrLogUtils.class);
	
	public static void writeLog(SISIntgrLog sisIntgrLog){
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.save(sisIntgrLog);
			session.getTransaction().commit();
		}
		catch (HibernateException e) {
			LOGGER.error("Could not save SISIntgrLog", e);
			session.getTransaction().rollback();
		}
		session.close();
	}
}
