package polyu.learn.bbssoadfs.operation.sis;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import polyu.learn.bbssoadfs.operation.Callbackable;
import polyu.learn.bbssoadfs.operation.ssoadfs.ADFSUser;
import polyu.learn.bbssoadfs.util.CCEmail;
import polyu.learn.bbssoadfs.util.ConfigUtils;
import polyu.learn.bbssoadfs.util.EmailContext;
import polyu.learn.bbssoadfs.util.EmailUtils;
import polyu.learn.bbssoadfs.util.HibernateUtils;
import polyu.learn.bbssoadfs.util.SISFileUtils;
import polyu.learn.bbssoadfs.util.SISIntgrLogUtils;

public class SISFileEndpointOperationHandler extends SISEndpointOperationHandler {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private File feedFile;
	private SISFileEndpointConfigProvider configProvider;

	public SISFileEndpointOperationHandler(SISFileEndpointConfigProvider configProvider) {
		this.configProvider = configProvider;
	}

	public File getFeedFile() {
		return feedFile;
	}

	public void setFeedFile(File feedFile) {
		this.feedFile = feedFile;
	}

	// ADFS User main functions @2018-05-18
	@Override
	public void run() throws SISException, IOException {
		SISIntgrLog sisIntgrLog = SISIntgrLogFactory.getInstance();
		String operationType = "InsertSSOADFS";// Determine the feed file source path
		try {
			LOGGER.info("generating feed file");
			ImportFeedFile(operationType);
			LOGGER.info("moving feed file to completed dir");
			moveFeedFile(operationType);
			sisIntgrLog.setStatus(SISStatus.SUCCESS.name());
		} catch (Exception ex) {
			sisIntgrLog.setStatus(SISStatus.FAILED.name());
			sisIntgrLog.setErrorMsg(ex.getClass().getName() + ": " + ex.getMessage());
			sendErrorEmail(sisIntgrLog);
			if (feedFile != null && feedFile.exists()) {
				LOGGER.info("moving feed file to error dir");
				SISFileUtils.moveFeedFileToErrorDir(feedFile);
			}
			throw new SISException("Error occurred when running SISFileEndpointOperationHandler", ex);

		} finally {
			sisIntgrLog.setLogDateTime(new Date());
			SISIntgrLogUtils.writeLog(sisIntgrLog);
			// HibernateUtils.closeAllResources();
		}
	}

	private void ImportFeedFile(String operationType) throws SISException {
		File feedFile = GetFeedFile(operationType);
		if (feedFile == null) {
			throw new IllegalArgumentException("The feed file is null");
		}
		this.setFeedFile(feedFile);

		try {
			// Truncate and Insert ADFS Users into table
			List<ADFSUser> AdfsUser = configProvider.insertADFSUserFromCSVFeedFile();
			LOGGER.info("***Total ADFS Users:  " + AdfsUser.size() + " Row(s) *** ");
		} catch (Exception ex) {
			throw new SISException("Insert ADFS Users into table has errors: ", ex);
		}
	}

	// ADFS User main functions @2018-05-18
	@Override
	public void check() throws SISException, IOException {
		SISIntgrLog sisIntgrLog = SISIntgrLogFactory.getInstance();
		String operationType = "PostSSOADFS";// Determine the feed file source path
		try {
			LOGGER.info("generating feed file");
			generateFeedFile();
			LOGGER.info("querying endpoint");
			queryEndpoint();
			LOGGER.info("moving feed file to completed dir");
			moveFeedFile(operationType);
			LOGGER.info("write to log table");
			LOGGER.info("*******************write to log table started*******************");
			WriteToLogTable();// Update ADFS Log table data @2018-05-18
			LOGGER.info("*******************write to log table ended*******************");
			LOGGER.info("check to log table");
			LOGGER.info("*******************check to log table started*******************");
			CheckToLogTable();// Delete ADFS Log table data @2018-05-18
			LOGGER.info("*******************check to log table ended*******************");
			sisIntgrLog.setStatus(SISStatus.SUCCESS.name());
		} catch (Exception ex) {
			sisIntgrLog.setStatus(SISStatus.FAILED.name());
			sisIntgrLog.setErrorMsg(ex.getClass().getName() + ": " + ex.getMessage());
			sendErrorEmail(sisIntgrLog);
			if (feedFile != null && feedFile.exists()) {
				SISFileUtils.moveFeedFileToErrorDir(feedFile);
			}
			throw new SISException("Error occurred when running SISFileEndpointOperationHandler", ex);
		} finally {
			sisIntgrLog.setLogDateTime(new Date());
			SISIntgrLogUtils.writeLog(sisIntgrLog);
			// HibernateUtils.closeAllResources();
		}
	}

	// ADFS User main functions @2018-05-18
	@Override
	public void update() throws Exception {
		SISIntgrLog sisIntgrLog = SISIntgrLogFactory.getInstance();
		String operationType = "UpdateSSOADFSLOG";// Determine the feed file source path
		try {
			LOGGER.info("generating Update/Delete SIS feed file");
			generateFeedFile();
			LOGGER.info("querying Update/Delete SIS endpoint");
			queryEndpoint();
			LOGGER.info("moving Update/Delete feed file to completed dir");
			moveFeedFile(operationType);
			LOGGER.info("check to log table");
			LOGGER.info("*******************check to log table started*******************");
			CheckToLogTable();// Delete ADFS Log table data @2018-05-18
			LOGGER.info("*******************check to log table ended*******************");
			sisIntgrLog.setStatus(SISStatus.SUCCESS.name());
		} catch (Exception ex) {
			sisIntgrLog.setStatus(SISStatus.FAILED.name());
			sisIntgrLog.setErrorMsg(ex.getClass().getName() + ": " + ex.getMessage());
			sendErrorEmail(sisIntgrLog);
			if (feedFile != null && feedFile.exists()) {
				SISFileUtils.moveFeedFileToErrorDir(feedFile);
			}
			throw new SISException("Error occurred when running SISFileEndpointOperationHandler", ex);
		} finally {
			sisIntgrLog.setLogDateTime(new Date());
			SISIntgrLogUtils.writeLog(sisIntgrLog);
			HibernateUtils.closeAllResources();
		}
	}

	private void generateFeedFile() throws SISException {
		File feedFile = configProvider.getFeedFile();

		if (feedFile == null) {
			throw new IllegalArgumentException("The feed file is null");
		}

		String feedFileName = feedFile.getName();

		LOGGER.info("feed file generated: {}", feedFileName);
		try {
			int numOfLine = SISFileUtils.countLines(feedFileName);
			// remove the header line
			long feedFileContentSize = numOfLine - 1;
			SISIntgrLogFactory.getInstance().setFeedfileNumOfLine(feedFileContentSize);
			LOGGER.info("feed file content size: {}", feedFileContentSize);

		} catch (IOException ex) {
			throw new SISException("Could not count line number of the feed file: " + feedFileName, ex);
		}
		this.setFeedFile(feedFile);
	}

	protected SISClientContext getSISClientContext() throws SISException {
		SISClientContext sisClientContext = configProvider.getSISClientContext();
		File feedFile = this.getFeedFile();
		if (feedFile == null)
			throw new SISException("Could not find the feed file");
		sisClientContext.setFeedFile(feedFile);
		return sisClientContext;
	}

	@Override
	public void callback(Object response) throws SISException {
		String responseStr = (String) response;
		LOGGER.info("http response: " + responseStr);
		SISIntgrLog sisIntgrLog = SISIntgrLogFactory.getInstance();
		sisIntgrLog.setHttpResponse(responseStr);
		if (responseStr.contains("Success")) {
			sisIntgrLog.setRefCode(extractRefCode(responseStr));
		} else {
			throw new SISException("The http response does not contain Success");
		}

		LOGGER.info("Invoking callback of the class: " + configProvider.getClass());
		// if the configProvider has a callback method, then callback
		if (configProvider instanceof Callbackable) {
			try {
				((Callbackable) configProvider).callback(response);
			} catch (Exception ex) {
				throw new SISException("Failed to invoke callback of configProvider", ex);
			}
		}

	}

	public File GetFeedFile(String operationType) throws SISException {
		Properties endpointProp = null;
		try {
			endpointProp = ConfigUtils.getPropertiesByName("endpoint");
		} catch (IOException ex) {
			throw new SISException("Failed to load endpoint properties", ex);
		}
		File sourceFile = null;
		if (operationType.equals("InsertSSOADFS")) {
			String sourceFileStr = endpointProp.getProperty("adfs.data.set.source.file");
			sourceFile = new File(sourceFileStr);
		}
		if (operationType.equals("PostSSOADFS")) {
			sourceFile = configProvider.getFeedFile();
		}
		if (operationType.equals("UpdateSSOADFSLOG")) {
			sourceFile = configProvider.getFeedFile();
		}
		if (!sourceFile.exists())
			throw new SISException("Could not find the source file: " + sourceFile.getPath());

		return sourceFile;
	}

	private void moveFeedFile(String operationType) throws SISException {
		String feedFileName = this.GetFeedFile(operationType).getName();
		try {
			SISFileUtils.moveFeedFile(this.GetFeedFile(operationType), operationType);
		} catch (IOException ex) {
			throw new SISException("Could not move the feed file: " + feedFileName, ex);
		}
	}

	public void WriteToLogTable() throws SISException {
		configProvider.writeOrUpdateADFSLog();// Update ADFS Log table data @2018-05-18
	}

	public void CheckToLogTable() throws SISException {
		configProvider.CheckADFSLog();// Delete ADFS Log table data @2018-05-18
	}

	private String extractRefCode(String response) throws SISException {
		String regex = ".*Success: Feed File Uploaded.  Use the reference code (.*?) to track these records in the logs.";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(response);
		m.find();
		String refCode = m.group(1);
		return refCode;
	}

	private void sendErrorEmail(SISIntgrLog sisIntgrLog) throws SISException {
		Properties prop = null;
		try {
			prop = ConfigUtils.getPropertiesByName("email");
		} catch (IOException ex) {
			throw new SISException("Error on loading properties: email", ex);
		}
		String host = prop.getProperty("sis.error.mail.host");
		String sender = prop.getProperty("sis.error.mail.sender");
		String receivers = prop.getProperty("sis.error.mail.receiver");
		String subject = prop.getProperty("sis.error.mail.subject");

		String[] receiverArray = receivers.split(",");
		Set<String> receiverSet = new HashSet<String>();

		for (String receiver : receiverArray) {
			receiver = StringUtils.trim(receiver);
			if (!StringUtils.isEmpty(receiver)) {
				receiverSet.add(receiver);
			}
		}

		EmailContext emailContext = new EmailContext();
		emailContext.setHost(host);
		emailContext.setFromAddress(sender);
		emailContext.setToAddress(receiverSet);

		if (configProvider instanceof CCEmail) {
			Set<String> ccAddress = ((CCEmail) configProvider).getCCEmailSet();
			emailContext.setCcAddress(ccAddress);
		}

		emailContext.setSubject(subject);
		final String HTML_BR = "<br />";
		String errorMessage = sisIntgrLog.toString();
		errorMessage = errorMessage.replace("\n", HTML_BR);
		emailContext.setMessageBody(errorMessage);
		EmailUtils.send(emailContext);
	}

}
