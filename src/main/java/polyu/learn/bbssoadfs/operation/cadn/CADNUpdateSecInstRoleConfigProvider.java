package polyu.learn.bbssoadfs.operation.cadn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import polyu.learn.bbssoadfs.operation.sis.SISClientContext;
import polyu.learn.bbssoadfs.operation.sis.SISException;
import polyu.learn.bbssoadfs.operation.sis.SISObjectMappingFileEndpointConfigProvider;
import polyu.learn.bbssoadfs.operation.ssoadfs.ADFSUser;
import polyu.learn.bbssoadfs.util.ConfigUtils;
import polyu.learn.bbssoadfs.util.HibernateUtils;

public class CADNUpdateSecInstRoleConfigProvider extends SISObjectMappingFileEndpointConfigProvider {

	@Override
	public List<String> getFeedFileHeader() {
		List<String> feedFileheader = new ArrayList<String>();
		feedFileheader.add("EXTERNAL_PERSON_KEY");
		feedFileheader.add("ROLE_ID");
		feedFileheader.add("DATA_SOURCE_KEY");
		
		return feedFileheader;
	}

	@Override
	public List<Object> getFeedFileBody() {
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		@SuppressWarnings("unchecked")
		List<CADN> cadnList = (List<CADN>)session.createCriteria(CADN.class).add(
			Restrictions.or(
				Restrictions.eq("employeeType", "T"), 
				Restrictions.eq("employeeType", "F")
			)
		).list();
		
		List<Object> feedFileBodyList = new ArrayList<Object>();
		for(CADN cadn : cadnList){
			feedFileBodyList.add(new CADNSISSecInstRoleWrapper(cadn)); //New Added @2018-07-11
		}
		session.close();
		return feedFileBodyList;
	}

	@Override
	public String getFeedFileName() {
		return "CADNInsertSecInstRole.txt";
	}
	
	@Override
	public SISClientContext getSISClientContext() throws SISException {
		Properties endpointProp = null;
		try{
			endpointProp = ConfigUtils.getPropertiesByName("endpoint");
		}catch(IOException ex){
			throw new SISException("Failed to load endpoint properties", ex);
		}
		String endpoint = endpointProp.getProperty("sis.endpoint");
		String operation = endpointProp.getProperty("sis.endpoint.operation.secondaryinstitutionrole.store");
		String username =  endpointProp.getProperty("sis.ssoadfs.cadnupdateperson.username");
		String password = endpointProp.getProperty("sis.ssoadfs.cadnupdateperson.password");
		SISClientContext sisClientContext = new SISClientContext();
		sisClientContext.setEndpoint(endpoint + operation);
		sisClientContext.setUsername(username);
		sisClientContext.setPassword(password);
		return sisClientContext;
	}

	@Override
	public List<ADFSUser> insertADFSUserFromCSVFeedFile() throws SISException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteADFSUser() throws SISException {
		// TODO Auto-generated method stub
		
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
