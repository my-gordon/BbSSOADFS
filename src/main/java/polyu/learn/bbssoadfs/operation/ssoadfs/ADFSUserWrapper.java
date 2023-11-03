package polyu.learn.bbssoadfs.operation.ssoadfs;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import polyu.learn.bbssoadfs.operation.ssoadfs.ADFSUser;

@Entity
@Table(name="ADFSUser")
public class ADFSUserWrapper {
	
	public ADFSUserWrapper(ADFSUser adfsuser) {
	}
	
	@Id
	@Column(name="UserName")
	private String UserName;
	
	@Column(name="FirstName")
	private String FirstName;
	
	@Column(name="LastName")
	private String LastName;
	
	@Column(name="Email")
	private String Email;
	
	@Column(name="DSK")
	private String DSK;
	
	@Column(name="CreateDate")
	private Date CreateDate;

	@Column(name="InsertDate")
	private Date InsertDate;
	
	
	public String getUserName() {
		return UserName;
	}

	public void setUserName(String UserName) {
		this.UserName = UserName;
	}
	
	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String FirstName) {
		this.FirstName = FirstName;
	}
	
	public String getLastName() {
		return LastName;
	}

	public void setLastName(String LastName) {
		this.LastName = LastName;
	}
	
	public String getEmail() {
		return Email;
	}

	public void setEmail(String Email) {
		this.Email = Email;
	}

	public String getDSK() {
		return DSK;
	}

	public void setDSK(String DSK) {
		this.DSK = DSK;
	}

	public Date getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(Date CreateDate) {
		this.CreateDate = CreateDate;
	}
	
	public Date getInsertDate() {
		return InsertDate;
	}

	public void setInsertDate(Date InsertDate) {
		this.InsertDate = InsertDate;
	}
	
	
	public String toString(){
		return "UserName:"+getUserName() + ", FirstName:" + getFirstName() + ", LastName:" + getLastName() + ", Email:" + getEmail() + ", DSK:" + getDSK() + ", CreateDate:" + getCreateDate() + ", InsertDate:" + getInsertDate();
	}
	
}
