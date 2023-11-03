package polyu.learn.bbssoadfs.operation.datasetstatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dataSetStatus")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataSetStatus {
	
	@XmlElement(name = "completedCount")
	private Long completedCount;
	
	@XmlElement(name = "dataIntegrationId")
	private String dataIntegrationId;
	
	@XmlElement(name = "dataSetUid")
	private String dataSetUid;
	
	@XmlElement(name = "errorCount")
	private Long errorCount;
	
	@XmlElement(name = "lastEntryDate")
	private String lastEntryDate;
	
	@XmlElement(name = "queuedCount")
	private Long queuedCount;
	
	@XmlElement(name = "startDate")
	private String startDate;
	
	@XmlElement(name = "warningCount")
	private Long warningCount;

	public Long getCompletedCount() {
		return completedCount;
	}

	public void setCompletedCount(Long completedCount) {
		this.completedCount = completedCount;
	}

	public String getDataIntegrationId() {
		return dataIntegrationId;
	}

	public void setDataIntegrationId(String dataIntegrationId) {
		this.dataIntegrationId = dataIntegrationId;
	}

	public String getDataSetUid() {
		return dataSetUid;
	}

	public void setDataSetUid(String dataSetUid) {
		this.dataSetUid = dataSetUid;
	}

	public Long getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Long errorCount) {
		this.errorCount = errorCount;
	}

	public String getLastEntryDate() {
		return lastEntryDate;
	}

	public void setLastEntryDate(String lastEntryDate) {
		this.lastEntryDate = lastEntryDate;
	}

	public Long getQueuedCount() {
		return queuedCount;
	}

	public void setQueuedCount(Long queuedCount) {
		this.queuedCount = queuedCount;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public Long getWarningCount() {
		return warningCount;
	}

	public void setWarningCount(Long warningCount) {
		this.warningCount = warningCount;
	}
	
}
