package polyu.learn.bbssoadfs.operation.cadn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import polyu.learn.bbssoadfs.operation.Callbackable;
import polyu.learn.bbssoadfs.operation.sis.SISClientContext;
import polyu.learn.bbssoadfs.operation.sis.SISException;
import polyu.learn.bbssoadfs.operation.sis.SISObjectMappingFileEndpointConfigProvider;
import polyu.learn.bbssoadfs.operation.ssoadfs.ADFSUser;
import polyu.learn.bbssoadfs.util.ConfigUtils;
import polyu.learn.bbssoadfs.util.HibernateUtils;
import polyu.learn.bbssoadfs.util.SISFileUtils;

public class CADNUpdatePersonConfigProvider extends SISObjectMappingFileEndpointConfigProvider implements Callbackable {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private final char FEED_FILE_SEPARATOR = '|';
	private final String NEW_LINE = System.lineSeparator();

	@Override
	public List<String> getFeedFileHeader() {
		List<String> feedFileheader = new ArrayList<String>();
		feedFileheader.add("EXTERNAL_PERSON_KEY");
		feedFileheader.add("USER_ID");
		feedFileheader.add("FIRSTNAME");
		feedFileheader.add("LASTNAME");
		feedFileheader.add("PASSWD");
		feedFileheader.add("EMAIL");
		feedFileheader.add("STUDENT_ID");
		feedFileheader.add("DEPARTMENT");
		feedFileheader.add("INSTITUTION_ROLE");
		feedFileheader.add("AVAILABLE_IND");// New Added @2018-05-18
		feedFileheader.add("DATA_SOURCE_KEY");
		feedFileheader.add("NEW_DATA_SOURCE_KEY");// New Added @2018-05-18

		return feedFileheader;
	}

	@Override
	public List<Object> getFeedFileBody() {
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		@SuppressWarnings("unchecked")
		List<CADN> cadnList = (List<CADN>) session.createCriteria(CADN.class).list();
		List<Object> feedFileBodyList = new ArrayList<Object>();
		for (CADN cadn : cadnList) {
			feedFileBodyList.add(new CADNSISWrapper(cadn));
		}
		session.close();
		return feedFileBodyList;
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
		removePasswordFromFeedFile();
	}

	// Remove the password field from the feed file
	public void removePasswordFromFeedFile() throws Exception {
		File feedFile = SISFileUtils.getFeedFileByName(getFeedFileName());
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(feedFile)));
		int row = 0;
		String line = null;
		int passwordIndex = -1;

		StringBuilder outputString = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			String[] lineArray = line.split("\\" + FEED_FILE_SEPARATOR);

			if (lineArray != null) {
				if (row == 0) {

					for (int i = 0; i < lineArray.length; i++) {
						if ("PASSWD".equals(lineArray[i])) {
							passwordIndex = i;
						}
					}

					if (passwordIndex == -1)
						throw new SISException("Could not find the header 'PASSWD' while removing");

					outputString.append(line);
					row++;
					continue;
				}

				// clear the password field
				lineArray[passwordIndex] = "";
				String currentLine = StringUtils.join(lineArray, FEED_FILE_SEPARATOR);
				outputString.append(NEW_LINE + currentLine);
			}
			row++;
		}
		reader.close();
		FileUtils.write(feedFile, outputString.toString(), CharEncoding.UTF_8, false);
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
		try {
			SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
			Session session = sessionFactory.openSession();
			@SuppressWarnings("unchecked")
			List<ADFSLogUpdateWrapper> adfslogListUpdateView = (List<ADFSLogUpdateWrapper>) session
					.createCriteria(ADFSLogUpdateWrapper.class).list();
			LOGGER.info("Total ADFS Log Update size: {}", adfslogListUpdateView.size());
			session.close();
			UpdateADFSLog(adfslogListUpdateView);

		} catch (Exception ex) {
			throw new SISException("writeOrUpdateADFSLog: Retrieve ADFS Log from view table function has errors: ", ex);
		}
	}

	public void UpdateADFSLog(List<ADFSLogUpdateWrapper> adfslogListUpdateView) throws SISException {
		try {
			for (int i = 0; i < adfslogListUpdateView.size(); i++) {
				ADFSLog adfslog = new ADFSLog();
				SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
				Session session = sessionFactory.openSession();
				adfslog.setNetId(adfslogListUpdateView.get(i).getNetId());
				adfslog.setOriginalFirstName(adfslogListUpdateView.get(i).getOriginalFirstName());
				adfslog.setOriginalLastName(adfslogListUpdateView.get(i).getOriginalLastName());
				adfslog.setOriginalEmail(adfslogListUpdateView.get(i).getOriginalEmail());
				adfslog.setUpdatedFirstName(getFirstName(adfslogListUpdateView.get(i).getUpdatedFirstName()));
				adfslog.setUpdatedLastName(getLastName(adfslogListUpdateView.get(i).getUpdatedLastName()));
				adfslog.setUpdatedRole(adfslogListUpdateView.get(i).getUpdatedRole());
				//adfslog.setUpdatedEmail(adfslogListUpdateView.get(i).getUpdatedEmail());
				adfslog.setUpdatedEmail(getEmail(adfslogListUpdateView.get(i).getUpdatedRole(),adfslogListUpdateView.get(i).getNetId(),adfslogListUpdateView.get(i).getUpdatedEmail()));
				adfslog.setAction(adfslogListUpdateView.get(i).getAction());
				adfslog.setModifiedDate(new Date());
				session.beginTransaction();
				session.save(adfslog);
				session.getTransaction().commit();
				session.close();
			}
		} catch (Exception ex) {
			throw new SISException("Insert Updat ADFS Log data into table function has errors: ", ex);
		}
	}

	@Override
	public void CheckADFSLog() throws SISException {
		try {
			SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
			Session session = sessionFactory.openSession();
			@SuppressWarnings("unchecked")
			List<ADFSLogDeleteWrapper> adfslogListDeleteView = (List<ADFSLogDeleteWrapper>) session
					.createCriteria(ADFSLogDeleteWrapper.class).list();
			LOGGER.info("Total ADFS Log Delete size: {}", adfslogListDeleteView.size());
			session.close();
			DeleteADFSLog(adfslogListDeleteView);

		} catch (Exception ex) {
			throw new SISException("CheckADFSLog: Retrieve ADFS Log from view table function has errors: ", ex);
		}

	}

	public void DeleteADFSLog(List<ADFSLogDeleteWrapper> adfslogListDeleteView) throws SISException {
		try {
			for (int i = 0; i < adfslogListDeleteView.size(); i++) {
				ADFSLog adfslog = new ADFSLog();
				SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
				Session session = sessionFactory.openSession();
				adfslog.setNetId(adfslogListDeleteView.get(i).getNetId());
				adfslog.setOriginalFirstName(adfslogListDeleteView.get(i).getOriginalFirstName());
				adfslog.setOriginalLastName(adfslogListDeleteView.get(i).getOriginalLastName());
				adfslog.setOriginalEmail(adfslogListDeleteView.get(i).getOriginalEmail());
				adfslog.setUpdatedFirstName(getFirstName(adfslogListDeleteView.get(i).getUpdatedFirstName()));
				adfslog.setUpdatedLastName(getLastName(adfslogListDeleteView.get(i).getUpdatedLastName()));
				adfslog.setUpdatedRole(adfslogListDeleteView.get(i).getUpdatedRole());
				//adfslog.setUpdatedEmail(adfslogListDeleteView.get(i).getUpdatedEmail());
				adfslog.setUpdatedEmail(getEmail(adfslogListDeleteView.get(i).getUpdatedRole(),adfslogListDeleteView.get(i).getNetId(),adfslogListDeleteView.get(i).getUpdatedEmail()));
				adfslog.setAction(adfslogListDeleteView.get(i).getAction());
				adfslog.setModifiedDate(new Date());
				session.beginTransaction();
				session.save(adfslog);
				session.getTransaction().commit();
				session.close();
			}
		} catch (Exception ex) {
			throw new SISException("Insert Delete ADFS Log data into table function has errors: ", ex);
		}
	}
	
	
	// Alan's Logic on splitting names
		public String getFirstName(String PuUserFullName) {
			String firstName = "";
			String fullName = StringUtils.stripToEmpty(PuUserFullName);
			String[] temp = fullName.split(" ");
			for (int x = 0; x < temp.length; x++) {
				if(temp[x].length() == 0){
					continue;
				}else if (temp[x].length() >= 2) {
					char ch = temp[x].charAt(1);
					if (!Character.isUpperCase(ch)) {
						firstName = firstName + " " + temp[x];
					}
				} else {
					char ch = temp[x].charAt(0);
					if (!Character.isUpperCase(ch)) {
						firstName = firstName + " " + temp[x];
					}
				}
			}
			if (firstName.isEmpty()) {
				firstName = getLastName(PuUserFullName);
			}
			return StringUtils.stripToEmpty(firstName);

		}

		// Alan's Logic on splitting names
		public String getLastName(String PuUserFullName) {
			String lastName = "";
			String fullName = StringUtils.stripToEmpty(PuUserFullName);
			String[] temp = fullName.split(" ");
			for (int x = 0; x < temp.length; x++) {
				if(temp[x].length() == 0){
					continue;
				}else if (temp[x].length() >= 2) {
					char ch = temp[x].charAt(1);
					if (Character.isUpperCase(ch)) {
						lastName = lastName + " " + temp[x];
					}
				} else {
					char ch = temp[x].charAt(0);
					if (Character.isUpperCase(ch)) {
						lastName = lastName + " " + temp[x];
					}
				}
			}
			if (lastName.isEmpty()) {
				lastName = getFirstName(PuUserFullName);
			}
			return StringUtils.stripToEmpty(lastName);
		}
		
		public String getEmail(String EmployeeType,String NetId,String Email) {
			final String STUDENT_EMAIL_SUFFIX = "@connect.polyu.hk";
			final String STAFF_EMAIL_SUFFIX = "@polyu.edu.hk";
			 String employeeType = EmployeeType;
			 String netId = NetId;
			 String email = "";
			 
			 if("O".equals(employeeType)){
				 email = Email;
			 }
			 else if("S".equals(employeeType)){
				 email = netId + STUDENT_EMAIL_SUFFIX;
			 }
			 else if("T".equals(employeeType) || "F".equals(employeeType)){
				 email = netId + STAFF_EMAIL_SUFFIX;
			 }
			 return email;
		}

}
