package polyu.learn.bbssoadfs.util;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class HibernateUtils {
 
    private static final SessionFactory sessionFactory;
    private static final StandardServiceRegistry serviceRegistry;
    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateUtils.class);
    
    static {
        try {
        	Properties dbProperties = ConfigUtils.getPropertiesByName("database");
            Configuration configuration = new Configuration();
            configuration.mergeProperties(dbProperties).configure();
 
            serviceRegistry = new StandardServiceRegistryBuilder()
                               .applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
        	LOGGER.error("Cannot initialize HibernateUtils", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
 
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
 
    public static void closeAllResources() {
        sessionFactory.close();
        StandardServiceRegistryBuilder.destroy(serviceRegistry);
    }
}