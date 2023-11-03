package polyu.learn.bbssoadfs.operation.sis;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import polyu.learn.bbssoadfs.operation.sis.annotation.SISColumn;
import polyu.learn.bbssoadfs.operation.sis.annotation.SISWrapper;
import polyu.learn.bbssoadfs.util.SISFileUtils;

public abstract class SISObjectMappingFileEndpointConfigProvider implements SISFileEndpointConfigProvider{
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private final String FEED_FILE_DELIMITER = "|";
	private final String NEW_LINE = System.lineSeparator();
	
	public File getFeedFile() throws SISException{
		List<String> feedFileHeaderList = this.getFeedFileHeader();
		List<Object> feedFileBodyList = this.getFeedFileBody();
		int feedFileBodyListSize = feedFileBodyList.size();
		if(feedFileBodyListSize == 0)
			throw new IllegalArgumentException("The feed file content is empty.");
		
		Class<?> wrapperClass = feedFileBodyList.get(0).getClass();
		LOGGER.info("The wrapper class: {}", wrapperClass);
		
		StringBuilder feedFileString = new StringBuilder();
		
		String feedFileHeader = StringUtils.join(feedFileHeaderList, FEED_FILE_DELIMITER);
		feedFileString.append(feedFileHeader);
		
		if(!wrapperClass.isAnnotationPresent(SISWrapper.class)){
			throw new SISException(wrapperClass.getName() + " is not an SIS warpper class.");
		}

		Method[] methods = wrapperClass.getDeclaredMethods();
		
		//construct a columnMethodMapping containing all column-method mappings of the wrapper
		Map<String, Method> columnMethodMapping = new HashMap<String, Method>();
		for(Method method : methods){
			if(method.isAnnotationPresent(SISColumn.class)){
				SISColumn sisColumn = method.getAnnotation(SISColumn.class);
				String columnName = sisColumn.name();
				columnMethodMapping.put(columnName, method);
			}
		}
		
		for(Object obj : feedFileBodyList){
			List<String> feedFileBodyLine = new ArrayList<String>();
			
			//get method by header in columnMethodMapping
			for(String header : feedFileHeaderList){
				Method method = columnMethodMapping.get(header);
				if(method == null){
					throw new SISException("Could not find " + header + " mapping method.");
				}
				
				try{
					String value = (String)method.invoke(obj);
					feedFileBodyLine.add(value);
				}catch(IllegalAccessException|IllegalArgumentException|InvocationTargetException ex){
					LOGGER.error("Error processing this line : " + obj, ex);
				}
			}
			String feedFileBodyLineStr = StringUtils.join(feedFileBodyLine, FEED_FILE_DELIMITER);
			feedFileString.append(NEW_LINE + feedFileBodyLineStr);
		}
		
		String feedFileName = this.getFeedFileName();
		try {
			File feedFile = SISFileUtils.writeFeedFile(feedFileName, feedFileString.toString());
			return feedFile;
		} catch (IOException ex) {
			throw new SISException("Error generating the feed file: " + feedFileName, ex);
		}
	}
	
	// get the list of feed file header columns
	public abstract List<String> getFeedFileHeader();
	
	// get the list of objects that must be SISWrapper annotated.
	public abstract List<Object> getFeedFileBody() throws SISException;
	
	// get the feed file name
	public abstract String getFeedFileName();

}
