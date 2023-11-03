package polyu.learn.bbssoadfs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtils {
	private static final String CONFIG_DIR = "config";
	private static final String PROP_EXT = ".properties";
	
	public static Properties getPropertiesByName(String propName) throws IOException{
		String pathname = CONFIG_DIR + File.separator + propName + PROP_EXT;
		return loadProp(pathname);
	}
	
	private static Properties loadProp(final String pathname) throws IOException{
		Properties prop = new Properties();
		InputStream inputStream = new FileInputStream(pathname);
		prop.load(inputStream);
		return prop;
	}

}
