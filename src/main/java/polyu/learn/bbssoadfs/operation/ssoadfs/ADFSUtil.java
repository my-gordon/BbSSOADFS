package polyu.learn.bbssoadfs.operation.ssoadfs;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import polyu.learn.bbssoadfs.operation.sis.SISException;
import polyu.learn.bbssoadfs.util.ConfigUtils;
import polyu.learn.bbssoadfs.util.EmailContext;
import polyu.learn.bbssoadfs.util.EmailUtils;
import polyu.learn.bbssoadfs.util.SISFileUtils;

public class ADFSUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ADFSUtil.class);
	private final static String FEED_FILE_COLUMN_SEPARATOR = "\\t";

	public static List<ADFSUserWrapper> getADFSUserMapping() throws SISException {
		List<ADFSUserWrapper> adfsuserList = new ArrayList<ADFSUserWrapper>();

		Properties endpointProp = null;
		try {
			endpointProp = ConfigUtils.getPropertiesByName("endpoint");
		} catch (IOException ex) {
			throw new SISException("Failed to load endpoint properties", ex);
		}

		String sourceFileStr = endpointProp.getProperty("adfs.data.set.source.file");
		File sourceFile = new File(sourceFileStr);

		if (!sourceFile.exists())
			throw new SISException("Could not find the source file: " + sourceFile.getPath());

		try {
			// When import ADFS User CSV file skip the header line @2018-05-18
			if (SISFileUtils.countLines(sourceFile) == 1) {
				moveSourceFileToLogDir();
				throw new SISException("The source file " + sourceFile.getPath() + " is empty");
			}
		} catch (IOException ex) {
			throw new SISException("Failed to count lines of the source file: " + sourceFile.getPath(), ex);
		}
		try {
			List<String> sourceFileLines = FileUtils.readLines(sourceFile, Charset.forName("UTF-8"));
			// Skip the Header line of the CSV @2018-05-18
			sourceFileLines.remove(0);
			int rowNumbers = 1;
			for (String sourceFileLine : sourceFileLines) {
				rowNumbers++;
				ADFSUserWrapper adfsUserWrapper = new ADFSUserWrapper(null);
				try {
					String[] tokens = sourceFileLine.split(FEED_FILE_COLUMN_SEPARATOR);
					adfsUserWrapper.setUserName(tokens[0].trim());
					adfsUserWrapper.setFirstName(tokens[1].trim());
					adfsUserWrapper.setLastName(tokens[2].trim());
					adfsUserWrapper.setEmail(tokens[3].trim());
					adfsUserWrapper.setDSK(tokens[4].trim());
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date CreateDate = null;
					try {
						CreateDate = formatter.parse(tokens[5].trim());
					} catch (ParseException e) {
						throw new SISException("Failed to read CreateDate of the source file: " + sourceFile.getPath(),
								e);
					}
					adfsUserWrapper.setCreateDate(CreateDate);
				} catch (Exception ex) {
					throw new SISException("***Read row number:  " + rowNumbers + " have error(s) *** ", ex);
				}
				
				adfsuserList.add(adfsUserWrapper);
			}

		} catch (IOException ex) {
			throw new SISException("Failed to read lines of the source file: " + sourceFile.getPath(), ex);
		}

		return adfsuserList;
	}

	public static List<ADFSUser> getADFSCSVList() throws SISException {
		List<ADFSUserWrapper> adfsList = getADFSUserMapping();
		List<ADFSUser> ADFSList = new ArrayList<ADFSUser>();
		for (ADFSUserWrapper adfsuser : adfsList) {
			ADFSUser user = new ADFSUser();
			user.setUserName(adfsuser.getUserName());
			user.setFirstName(adfsuser.getFirstName());
			user.setLastName(adfsuser.getLastName());
			user.setEmail(adfsuser.getEmail());
			user.setDSK(adfsuser.getDSK());
			user.setCreateDate(adfsuser.getCreateDate());
			ADFSList.add(user);
		}
		return ADFSList;
	}

	public static void moveSourceFileToLogDir() throws SISException, IOException {
		Properties endpointProp = null;
		try {
			endpointProp = ConfigUtils.getPropertiesByName("endpoint");
		} catch (IOException ex) {
			throw new SISException("Failed to load endpoint properties", ex);
		}

		String sourceFileStr = endpointProp.getProperty("sis.ssoadfs.adfs.source.file");
		String logDirStr = endpointProp.getProperty("sis.ssoadfs.adfs.log.dir");
		File sourceFile = new File(sourceFileStr);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_");
		Calendar calendar = Calendar.getInstance();
		String prefix = sdf.format(calendar.getTime());

		String destFileName = sourceFile.getName();
		File destFile = new File(logDirStr + prefix + destFileName);
		LOGGER.info("Moving the source file from {} to {}", sourceFile.getPath(), destFile.getPath());
		FileUtils.moveFile(sourceFile, destFile);
	}

	public static List<Object> getReenrollmentList() throws SISException {
		List<Object> hseoList = new ArrayList<Object>();

		Properties endpointProp = null;
		try {
			endpointProp = ConfigUtils.getPropertiesByName("endpoint");
		} catch (IOException ex) {
			throw new SISException("Failed to load endpoint properties", ex);
		}

		String sourceFileStr = endpointProp.getProperty("sis.integration.hseo.reenrollment.file");
		File sourceFile = new File(sourceFileStr);

		if (!sourceFile.exists())
			throw new SISException("Could not find the source file: " + sourceFile.getPath());

		try {
			if (SISFileUtils.countLines(sourceFile) == 1) {
				moveReenrollmentSourceFileToLogDir();
				throw new SISException("The source file " + sourceFile.getPath() + " is empty");
			}
		} catch (IOException ex) {
			throw new SISException("Failed to count lines of the source file: " + sourceFile.getPath(), ex);
		}

		try {
			List<String> sourceFileLines = FileUtils.readLines(sourceFile, Charset.forName("UTF-8"));
			int i = 0;
			for (String sourceFileLine : sourceFileLines) {
				if (i == 0) {
					i++;
					continue;
				}

				String userBatchUid = extractUserBatchUid(sourceFileLine);
				if (userBatchUid != null) {
					// ADFSUserWrapper adfsuserSISWrapper = new ADFSUserWrapper();
					// adfsuserSISWrapper.setUsername(userBatchUid);
					// hseoList.add(hseoSISWrapper);
				}
				i++;
			}
			sendReenrollmentEmail(sourceFileLines);

		} catch (IOException ex) {
			throw new SISException("Failed to read lines of the source file: " + sourceFile.getPath(), ex);
		}

		return hseoList;
	}

	private static String extractUserBatchUid(String line) {
		Pattern pattern = Pattern.compile(".*Reason: The user with UserBatchUid '(.*)' can not be found\\..*");
		Matcher matcher = pattern.matcher(line);
		if (matcher.matches()) {
			return matcher.group(1);
		}
		return null;
	}

	private static void sendReenrollmentEmail(List<String> sourceFileLines) throws SISException {
		Properties endpointProp = null;
		try {
			endpointProp = ConfigUtils.getPropertiesByName("email");
		} catch (IOException ex) {
			throw new SISException("Failed to load endpoint properties", ex);
		}

		String host = endpointProp.getProperty("sis.hseo.reenrollment.host");
		String sender = endpointProp.getProperty("sis.hseo.reenrollment.sender");
		String receivers = endpointProp.getProperty("sis.hseo.reenrollment.receiver");
		String subject = endpointProp.getProperty("sis.hseo.reenrollment.subject");

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
		emailContext.setSubject(subject);
		final String HTML_BR = "<br />";

		StringBuilder sb = new StringBuilder();
		for (String line : sourceFileLines) {
			sb.append(line + HTML_BR);
		}
		emailContext.setMessageBody(sb.toString());
		EmailUtils.send(emailContext);

	}

	public static void moveReenrollmentSourceFileToLogDir() throws SISException, IOException {
		Properties endpointProp = null;
		try {
			endpointProp = ConfigUtils.getPropertiesByName("endpoint");
		} catch (IOException ex) {
			throw new SISException("Failed to load endpoint properties", ex);
		}

		String sourceFileStr = endpointProp.getProperty("sis.integration.hseo.reenrollment.file");
		String logDirStr = endpointProp.getProperty("sis.integration.hseo.reenrollment.log.dir");
		File sourceFile = new File(sourceFileStr);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_");
		Calendar calendar = Calendar.getInstance();
		String prefix = sdf.format(calendar.getTime());

		String destFileName = sourceFile.getName();
		File destFile = new File(logDirStr + prefix + destFileName);
		LOGGER.info("Moving the source file from {} to {}", sourceFile.getPath(), destFile.getPath());
		FileUtils.moveFile(sourceFile, destFile);
	}
}
