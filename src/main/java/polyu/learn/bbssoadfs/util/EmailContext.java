package polyu.learn.bbssoadfs.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EmailContext {
	private String fromAddress;
	private Set<String> toAddress;
	private Set<String> ccAddress;
	private Set<String> bccAddress;
	private String host;
	private String subject;
	private String messageBody;
	private List<File> fileAttachment;

	public EmailContext() {
		toAddress = new HashSet<String>();
		ccAddress = new HashSet<String>();
		bccAddress = new HashSet<String>();
		fileAttachment = new ArrayList<File>();
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public Set<String> getToAddress() {
		return toAddress;
	}

	public void setToAddress(Set<String> toAddress) {
		this.toAddress = toAddress;
	}

	public void addToAddress(String toAddress) {
		this.toAddress.add(toAddress);
	}

	public void removeToAddress(String toAddress) {
		this.toAddress.remove(toAddress);
	}
	
	public Set<String> getCcAddress() {
		return ccAddress;
	}

	public void setCcAddress(Set<String> ccAddress) {
		this.ccAddress = ccAddress;
	}
	
	public void addCcAddress(String ccAddress) {
		this.ccAddress.add(ccAddress);
	}
	
	public void removeCcAddress(String ccAddress){
		this.ccAddress.remove(ccAddress);
	}

	public Set<String> getBccAddress() {
		return bccAddress;
	}

	public void setBccAddress(Set<String> bccAddress) {
		this.bccAddress = bccAddress;
	}

	public void addBccAddress(String bccAddress) {
		this.bccAddress.add(bccAddress);
	}

	public void removeBccAddress(String bccAddress) {
		this.bccAddress.remove(bccAddress);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public List<File> getFileAttachment() {
		return fileAttachment;
	}

	public void setFileAttachment(List<File> fileAttachment) {
		this.fileAttachment = fileAttachment;
	}

	public void addFileAttachment(File fileAttachment) {
		this.fileAttachment.add(fileAttachment);
	}

	public void removeFileAttachment(File fileAttachment) {
		this.fileAttachment.remove(fileAttachment);
	}

}
