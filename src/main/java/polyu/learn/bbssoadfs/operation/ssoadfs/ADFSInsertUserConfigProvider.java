package polyu.learn.bbssoadfs.operation.ssoadfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import polyu.learn.bbssoadfs.operation.Callbackable;
import polyu.learn.bbssoadfs.operation.cadn.ADFSLog;
import polyu.learn.bbssoadfs.operation.sis.SISClientContext;
import polyu.learn.bbssoadfs.operation.sis.SISException;
import polyu.learn.bbssoadfs.operation.sis.SISObjectMappingFileEndpointConfigProvider;
import polyu.learn.bbssoadfs.util.ConfigUtils;
import polyu.learn.bbssoadfs.util.HibernateUtils;

public class ADFSInsertUserConfigProvider extends SISObjectMappingFileEndpointConfigProvider implements Callbackable {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	@Override
	public List<String> getFeedFileHeader() {
		List<String> feedFileheader = new ArrayList<String>();
		feedFileheader.add("UserName");
		feedFileheader.add("FirstName");
		feedFileheader.add("LastName");
		feedFileheader.add("Email");
		feedFileheader.add("DSK");
		feedFileheader.add("CreateDate");
		return feedFileheader;
	}

	@Override
	public List<Object> getFeedFileBody() {
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		@SuppressWarnings("unchecked")
		List<ADFSUser> adfsuserList = (List<ADFSUser>) session.createCriteria(ADFSUser.class).list();
		List<Object> feedFileBodyList = new ArrayList<Object>();
		for (ADFSUser adfsuser : adfsuserList) {
			feedFileBodyList.add(new ADFSUserWrapper(adfsuser));
		}
		session.close();
		return feedFileBodyList;
	}

	@Override
	public List<ADFSUser> insertADFSUserFromCSVFeedFile() throws SISException {
		List<ADFSUser> ADFSList = new ArrayList<ADFSUser>();
		try {
			ADFSList = ADFSUtil.getADFSCSVList();
		} catch (Exception ex) {
			throw new SISException("Get ADFS CSV List has errors: ", ex);
		}
		try {
			// Truncate ADFS Users table
			deleteADFSUser();
		} catch (Exception ex) {
			throw new SISException("Truncate ADFS Users table has errors: ", ex);
		}

		for (ADFSUser adfs : ADFSList) {
			SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
			Session session = sessionFactory.openSession();
			adfs.setInsertDate(new Date());// Insert Datetime default as current date
			session.beginTransaction();
			session.save(adfs);
			session.getTransaction().commit();
			session.close();
		}
		return ADFSUtil.getADFSCSVList();
	}

	@Override
	public void deleteADFSUser() throws SISException {
		try {
			SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			int rowCount = session.createQuery("DELETE FROM ADFSUser").executeUpdate();
			LOGGER.info(rowCount + " were deleted from table <ADFSUser>");
			session.getTransaction().commit();
			session.close();
		} catch (Exception ex) {
			throw new SISException("Run Insert ADFS Users into table function has errors: ", ex);
		}
	}

	@Override
	public String getFeedFileName() {
		return "CADNUpdatePerson.txt";
	}

	@Override
	public SISClientContext getSISClientContext() throws SISException {
		Properties endpointProp = null;
		try {
			endpointProp = ConfigUtils.getPropertiesByName("endpoint");
		} catch (IOException ex) {
			throw new SISException("Failed to load endpoint properties", ex);
		}
		String endpoint = endpointProp.getProperty("sis.endpoint");
		String operation = endpointProp.getProperty("sis.endpoint.operation.person.store");
		String username = endpointProp.getProperty("sis.ssoadfs.cadnupdateperson.username");
		String password = endpointProp.getProperty("sis.ssoadfs.cadnupdateperson.password");
		SISClientContext sisClientContext = new SISClientContext();
		sisClientContext.setEndpoint(endpoint + operation);
		sisClientContext.setUsername(username);
		sisClientContext.setPassword(password);
		return sisClientContext;
	}

	@Override
	public void callback(Object object) throws Exception {
		LOGGER.info("Callback: Removing the password field from the feed file {}", getFeedFileName());
		// removePasswordFromFeedFile();
	}

	@Override
	public List<ADFSUser> retrieveADFSUserFromTable() throws SISException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeOrUpdateADFSLog() throws SISException {
		// TODO Auto-generated method stub
	}

	@Override
	public void CheckADFSLog() throws SISException {
		// TODO Auto-generated method stub
		
	}

}
