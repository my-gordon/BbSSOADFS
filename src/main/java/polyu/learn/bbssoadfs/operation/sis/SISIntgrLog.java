package polyu.learn.bbssoadfs.operation.sis;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

enum SISStatus {
    SUCCESS, FAILED
}

@Entity
@Table(name="SIS_INTGR_LOG")
public class SISIntgrLog {
	@Id
	@GenericGenerator(name="generator", strategy="increment")
	@GeneratedValue(generator="generator")
	@Column(name="LOG_ID")
	private Long logId;
	
	@Column(name="LOG_DATETIME")
	private Date logDateTime;
	
	@Column(name="OPERATION")
	private String operation;
	
	@Column(name="SIS_CLIENT")
	private String sisClient;
	
	@Column(name="FEEDFILE_NAME")
	private String feedFileName;
	
	@Column(name="FEEDFILE_NUM_OF_LINE")
	private Long feedfileNumOfLine;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="REF_CODE")
	private String refCode;
	
	@Column(name="HTTP_RESPONSE")
	private String httpResponse;
	
	@Column(name="ERROR_MSG")
	private String errorMsg;
	
	@Column(name="DATASETSTATUS_RAW")
	private String dataSetStatusRaw;
	
	@Column(name="DATASETSTATUS_COMPLETED_COUNT")
	private Long dataSetStatusCompletedCount;
	
	@Column(name="DATASETSTATUS_DATA_INTEGRATION_ID")
	private String dataSetStatusDataIntegrationId;
	
	@Column(name="DATASETSTATUS_DATA_SET_UID")
	private String dataSetStatusDataSetUid;
	
	@Column(name="DATASETSTATUS_ERROR_COUNT")
	private Long dataSetStatusErrorCount;
	
	@Column(name="DATASETSTATUS_LAST_ENTRY_DATE")
	private String dataSetStatusLastEntryDate;
	
	@Column(name="DATASETSTATUS_QUEUED_COUNT")
	private Long dataSetStatusQueuedCount;
	
	@Column(name="DATASETSTATUS_START_DATE")
	private String dataSetStatusStartDate;
	
	@Column(name="DATASETSTATUS_WARNING_COUNT")
	private Long dataSetStatusWarningCount;
	
	public Long getLogId() {
		return logId;
	}
	public void setLogId(Long logId) {
		this.logId = logId;
	}
	public Date getLogDateTime() {
		return logDateTime;
	}
	public void setLogDateTime(Date logDateTime) {
		this.logDateTime = logDateTime;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getSisClient() {
		return sisClient;
	}
	public void setSisClient(String sisClient) {
		this.sisClient = sisClient;
	}
	public String getFeedFileName() {
		return feedFileName;
	}
	public void setFeedFileName(String feedFileName) {
		this.feedFileName = feedFileName;
	}
	public Long getFeedfileNumOfLine() {
		return feedfileNumOfLine;
	}
	public void setFeedfileNumOfLine(Long feedfileNumOfLine) {
		this.feedfileNumOfLine = feedfileNumOfLine;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRefCode() {
		return refCode;
	}
	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}
	public String getHttpResponse() {
		return httpResponse;
	}
	public void setHttpResponse(String httpResponse) {
		this.httpResponse = httpResponse;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getDataSetStatusRaw() {
		return dataSetStatusRaw;
	}
	public void setDataSetStatusRaw(String dataSetStatusRaw) {
		this.dataSetStatusRaw = dataSetStatusRaw;
	}
	public String getDataSetStatusDataIntegrationId() {
		return dataSetStatusDataIntegrationId;
	}
	public void setDataSetStatusDataIntegrationId(
			String dataSetStatusDataIntegrationId) {
		this.dataSetStatusDataIntegrationId = dataSetStatusDataIntegrationId;
	}
	public String getDataSetStatusDataSetUid() {
		return dataSetStatusDataSetUid;
	}
	public void setDataSetStatusDataSetUid(String dataSetStatusDataSetUid) {
		this.dataSetStatusDataSetUid = dataSetStatusDataSetUid;
	}
	public String getDataSetStatusLastEntryDate() {
		return dataSetStatusLastEntryDate;
	}
	public void setDataSetStatusLastEntryDate(String dataSetStatusLastEntryDate) {
		this.dataSetStatusLastEntryDate = dataSetStatusLastEntryDate;
	}
	public String getDataSetStatusStartDate() {
		return dataSetStatusStartDate;
	}
	public void setDataSetStatusStartDate(String dataSetStatusStartDate) {
		this.dataSetStatusStartDate = dataSetStatusStartDate;
	}
	public Long getDataSetStatusCompletedCount() {
		return dataSetStatusCompletedCount;
	}
	public void setDataSetStatusCompletedCount(Long dataSetStatusCompletedCount) {
		this.dataSetStatusCompletedCount = dataSetStatusCompletedCount;
	}
	public Long getDataSetStatusErrorCount() {
		return dataSetStatusErrorCount;
	}
	public void setDataSetStatusErrorCount(Long dataSetStatusErrorCount) {
		this.dataSetStatusErrorCount = dataSetStatusErrorCount;
	}
	public Long getDataSetStatusQueuedCount() {
		return dataSetStatusQueuedCount;
	}
	public void setDataSetStatusQueuedCount(Long dataSetStatusQueuedCount) {
		this.dataSetStatusQueuedCount = dataSetStatusQueuedCount;
	}
	public Long getDataSetStatusWarningCount() {
		return dataSetStatusWarningCount;
	}
	public void setDataSetStatusWarningCount(Long dataSetStatusWarningCount) {
		this.dataSetStatusWarningCount = dataSetStatusWarningCount;
	}
	
	public String toString(){
		return 
		"Operation: " + operation + "\n" +
		"SIS Client: " + sisClient + "\n" +
		"Feed File Name: " + feedFileName + "\n" +
		"Feed File Num Of Line: " + feedfileNumOfLine + "\n" +
		"Status: " + status + "\n" +
		"Ref Code: " + refCode + "\n" +
		"Http Response: " + httpResponse + "\n" +
		"Error Msg: " + errorMsg + "\n";
	}
	
}
