package polyu.learn.bbssoadfs.operation.datasetstatus;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import polyu.learn.bbssoadfs.operation.sis.SISClientContext;
import polyu.learn.bbssoadfs.operation.sis.SISEndpointOperationHandler;
import polyu.learn.bbssoadfs.operation.sis.SISException;
import polyu.learn.bbssoadfs.operation.sis.SISIntgrLog;
import polyu.learn.bbssoadfs.util.ConfigUtils;
import polyu.learn.bbssoadfs.util.HibernateUtils;

public class DataSetStatusSISOperationHandler extends SISEndpointOperationHandler {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private String dataSetUid;
	private Session session;
	private SISIntgrLog currentSISIntgrLog;

	@Override
	public void run() throws Exception {
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		session = sessionFactory.openSession();
		@SuppressWarnings("unchecked")
		List<SISIntgrLog> sisIntgrLogList = (List<SISIntgrLog>) session.createCriteria(SISIntgrLog.class)
				.add(Restrictions.or(Restrictions.isNull("dataSetStatusQueuedCount"),
						Restrictions.gt("dataSetStatusQueuedCount", new Long(0))))
				.add(Restrictions.isNotNull("refCode")).add(Restrictions.isNull("errorMsg")).list();

		int sisIntgrLogListSize = sisIntgrLogList.size();
		if (sisIntgrLogListSize == 0) {
			LOGGER.info("No datasetstatus records to be processed");
		}

		LOGGER.info("{} datasetstatus records to be processed", sisIntgrLogListSize);

		session.beginTransaction();
		for (SISIntgrLog sisIntgrLog : sisIntgrLogList) {
			currentSISIntgrLog = sisIntgrLog;
			dataSetUid = sisIntgrLog.getRefCode();
			LOGGER.info("queering endpoint with refcode: {}", dataSetUid);
			try {
				queryEndpoint();
			} catch (SISException ex) {
				LOGGER.error("Error occurred when querying the endpoint: {}", dataSetUid, ex);
				currentSISIntgrLog.setErrorMsg(ex.getMessage());
				session.save(currentSISIntgrLog);
			}
		}
		session.getTransaction().commit();
		session.close();
		HibernateUtils.closeAllResources();
	}

	// ADFS User main functions @2018-05-18
	@Override
	public void check() throws Exception {
	}

	// ADFS User main functions @2018-05-18
	@Override
	public void update() throws Exception {
	}

	@Override
	public void callback(Object response) throws SISException {
		String responseStr = (String) response;
		LOGGER.info("http response: \n{} ", responseStr);
		currentSISIntgrLog.setDataSetStatusRaw(responseStr);
		DataSetStatus dataSetStatus = unmarshallerDataSetStatusXML(responseStr);
		currentSISIntgrLog.setDataSetStatusCompletedCount(dataSetStatus.getCompletedCount());
		currentSISIntgrLog.setDataSetStatusDataIntegrationId(dataSetStatus.getDataIntegrationId());
		currentSISIntgrLog.setDataSetStatusDataSetUid(dataSetStatus.getDataSetUid());
		currentSISIntgrLog.setDataSetStatusErrorCount(dataSetStatus.getErrorCount());
		currentSISIntgrLog.setDataSetStatusLastEntryDate(dataSetStatus.getLastEntryDate());
		currentSISIntgrLog.setDataSetStatusQueuedCount(dataSetStatus.getQueuedCount());
		currentSISIntgrLog.setDataSetStatusStartDate(dataSetStatus.getStartDate());
		currentSISIntgrLog.setDataSetStatusWarningCount(dataSetStatus.getWarningCount());
		session.save(currentSISIntgrLog);
	}

	private DataSetStatus unmarshallerDataSetStatusXML(String xml) throws SISException {

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(DataSetStatus.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(xml);
			DataSetStatus dataSetStatus = (DataSetStatus) unmarshaller.unmarshal(reader);
			if (dataSetStatus == null) {
				throw new SISException("Could not unmarshall DataSetStatus xml");
			}
			return dataSetStatus;
		} catch (JAXBException e) {
			throw new SISException("Could not unmarshall DataSetStatus xml", e);
		}

	}

	@Override
	protected SISClientContext getSISClientContext() throws SISException {
		Properties endpointProp = null;
		try {
			endpointProp = ConfigUtils.getPropertiesByName("endpoint");
		} catch (IOException ex) {
			throw new SISException("Failed to load endpoint properties", ex);
		}
		String endpoint = endpointProp.getProperty("sis.endpoint");
		String operation = endpointProp.getProperty("sis.endpoint.operation.data.set.status");
		operation = operation.replace("[dataSetUid]", dataSetUid);
		String username = endpointProp.getProperty("sis.integration.sisgeneral.username");
		String password = endpointProp.getProperty("sis.integration.sisgeneral.password");
		SISClientContext sisClientContext = new SISClientContext();
		sisClientContext.setEndpoint(endpoint + operation);
		sisClientContext.setUsername(username);
		sisClientContext.setPassword(password);
		return sisClientContext;
	}

}
