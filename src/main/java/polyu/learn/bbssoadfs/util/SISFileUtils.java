package polyu.learn.bbssoadfs.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import polyu.learn.bbssoadfs.operation.sis.SISException;
import polyu.learn.bbssoadfs.operation.sis.SISIntgrLogFactory;

public class SISFileUtils {
	public static final String FEED_FILE_DIR = "feedfile";
	public static final String PROCESSING_DIR = "processing";
	public static final String COMPLETED_DIR = "completed";
	public static final String ERROR_DIR = "error";
	public static final String DATE_FORMAT = "yyyy.MM.dd_HH.mm.ss";
	public static final String FILE_DATE_FORMAT = "yyyy_MM_dd";

	private static final Logger LOGGER = LoggerFactory.getLogger(SISFileUtils.class);

	public static File writeFeedFile(String fileName, String feedFileString) throws IOException {
		String pathname = getFeedFileProcessingDir() + File.separator + fileName;
		File feedFile = new File(pathname);
		FileUtils.write(feedFile, feedFileString, CharEncoding.UTF_8);
		return feedFile;
	}

	public static void moveFeedFile(File fileName, String operationType) throws IOException {

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String nowStr = sdf.format(calendar.getTime());

		String srcFileName = getFeedFileProcessingDir() + File.separator + fileName.getName();
		String destFileName = getFeedFileCompletedDir() + File.separator + nowStr + "." + fileName.getName();

		File srcFile = new File(srcFileName);
		File destFile = new File(destFileName);

		if (operationType.equals("InsertSSOADFS")) {
			// Move the ADFS user csv file from download folder to processing folder
			// @2018-05-18
			File downloadFile = fileName;
			FileUtils.moveFile(downloadFile, srcFile);
			LOGGER.info("Moved the feed file from dowanload path {} to processing path {}", downloadFile.getPath(),
					srcFile.getPath());

			FileUtils.moveFile(srcFile, destFile);
			LOGGER.info("Moved the feed file from {} to {}", srcFile.getPath(), destFile.getPath());
			SISIntgrLogFactory.getInstance().setFeedFileName(nowStr + "." + fileName.getName());

		}

		if (operationType.equals("PostSSOADFS")) {
			// Move the matched CADN txt file from processing folder to processing completed folder
			// @2018-05-18
			FileUtils.moveFile(srcFile, destFile);
			LOGGER.info("Moved the feed file from {} to {}", srcFile.getPath(), destFile.getPath());
		}
		
		if (operationType.equals("UpdateSSOADFSLOG")) {
			// Move the CADN matched and ADFS User Create Date large than 7 days txt file from processing folder to processing completed folder
			// @2018-05-18
			FileUtils.moveFile(srcFile, destFile);
			LOGGER.info("Moved the feed file from {} to {}", srcFile.getPath(), destFile.getPath());
		}

	}

	public static String getFeedFileProcessingDir() {
		return FEED_FILE_DIR + File.separator + PROCESSING_DIR;
	}

	public static String getFeedFileCompletedDir() {
		return FEED_FILE_DIR + File.separator + COMPLETED_DIR;
	}

	public static String getFeedFileErrorDir() {
		return FEED_FILE_DIR + File.separator + ERROR_DIR;
	}

	public static int countLines(String fileName) throws IOException {
		String pathname = getFeedFileProcessingDir() + File.separator + fileName;
		return countLines(new File(pathname));
	}

	public static int countLines(File file) throws IOException {
		LineNumberReader reader = new LineNumberReader(new FileReader(file));
		int cnt = 0;
		while ((reader.readLine()) != null)
			;
		cnt = reader.getLineNumber();
		reader.close();
		return cnt;
	}

	public static File copyFeedFileToProcessingDir(File srcFile, String destFileName) throws IOException {
		destFileName = getFeedFileProcessingDir() + File.separator + destFileName;
		File destFile = new File(destFileName);
		FileUtils.copyFile(srcFile, destFile);
		LOGGER.info("Copied the feed file from {} to {}", srcFile.getPath(), destFile.getPath());
		return destFile;
	}

	public static void moveFeedFileToErrorDir(File srcFile) throws IOException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String nowStr = sdf.format(calendar.getTime());

		String destFileName = getFeedFileErrorDir() + File.separator + nowStr + "." + srcFile.getName();

		File destFile = new File(destFileName);
		FileUtils.moveFile(srcFile, destFile);

		LOGGER.info("Moved the feed file from {} to {}", srcFile.getPath(), destFile.getPath());
	}

	public static File getFeedFileByName(String fileName) {
		String pathname = getFeedFileProcessingDir() + File.separator + fileName;
		File feedFile = new File(pathname);
		return feedFile;
	}

	public static void writeToLog(String message) throws IOException, SISException {
		Properties endpointProp = null;
		try {
			endpointProp = ConfigUtils.getPropertiesByName("endpoint");
		} catch (IOException ex) {
			throw new SISException("Failed to load endpoint properties", ex);
		}
		String logDirStr = endpointProp.getProperty("sis.ssoadfs.adfs.log.dir");
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(FILE_DATE_FORMAT);
		String nowStr = sdf.format(calendar.getTime());
		String newFileName = nowStr + ".txt";

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		logDirStr = logDirStr + File.separator + year + File.separator + month + File.separator + day + File.separator;

		File newDirectory = new File(logDirStr);
		File newFile = new File(logDirStr + newFileName);
		if (!newDirectory.exists())
			newDirectory.mkdirs();

		if (!newFile.exists())
			try {
				newFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		String text = message;
		BufferedWriter output = null;
		try {
			output = new BufferedWriter(new FileWriter(newFile, true));
			output.write(text);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

	public static File GetLogFile() throws IOException, SISException {
		Properties endpointProp = null;
		try {
			endpointProp = ConfigUtils.getPropertiesByName("endpoint");
		} catch (IOException ex) {
			throw new SISException("Failed to load endpoint properties", ex);
		}
		String logDirStr = endpointProp.getProperty("sis.ssoadfs.adfs.log.dir");
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(FILE_DATE_FORMAT);
		String nowStr = sdf.format(calendar.getTime());
		String newFileName = nowStr + ".txt";

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		logDirStr = logDirStr + File.separator + year + File.separator + month + File.separator + day + File.separator;

		File newDirectory = new File(logDirStr);
		File newFile = new File(logDirStr + newFileName);
		// Create directory for non existed path.
		if (!newDirectory.exists())
			newDirectory.mkdirs();

		if (!newFile.exists())
			try {
				newFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return newFile;
	}

}
