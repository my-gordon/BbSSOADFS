package polyu.learn.bbssoadfs.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

public class LoggerUtils {
	public static void configLog4j() {
		final String AT_CHAR = "@";
		RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
		String PID = rt.getName();
		if(StringUtils.contains(PID, AT_CHAR)){
			PID = StringUtils.split(PID, AT_CHAR)[0];
		}
		MDC.put("PID", PID);
	}
}
