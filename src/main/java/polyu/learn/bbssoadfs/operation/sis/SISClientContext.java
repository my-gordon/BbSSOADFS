package polyu.learn.bbssoadfs.operation.sis;

import java.io.File;

public class SISClientContext {
	private String endpoint;
	private String username;
	private String password;
	private File feedFile;
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public File getFeedFile() {
		return feedFile;
	}
	public void setFeedFile(File feedFile) {
		this.feedFile = feedFile;
	}
	public String toString(){
		return "endpoint: " + endpoint +
				"\nusername: " + username +
				"\npassword: " + password +
				"\nfeedFile: " + ((feedFile==null)?"None":feedFile.getPath());
	}

}
