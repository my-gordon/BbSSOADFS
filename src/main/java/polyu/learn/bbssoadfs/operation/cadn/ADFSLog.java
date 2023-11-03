package polyu.learn.bbssoadfs.operation.cadn;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ADFSLog")
public class ADFSLog {
	@Id
	@Column(name="net_id")
	private String netId;
	
	@Column(name="OriginalFirstName")
	private String OriginalFirstName;
	
	@Column(name="OriginalLastName")
	private String OriginalLastName;
	
	@Column(name="OriginalEmail")
	private String OriginalEmail;

	@Column(name="UpdatedFirst_Name")
	private String UpdatedFirstName;
	
	@Column(name="UpdatedLast_Name")
	private String UpdatedLastName;
	
	@Column(name="UpdatedEmail")
	private String UpdatedEmail;
	
	@Column(name="UpdatedRole")
	private String UpdatedRole;
	
	@Column(name="Action")
	private String Action;
	
	@Column(name="ModifiedDate")
	private Date ModifiedDate;

	public String getNetId() {
		return netId;
	}

	public void setNetId(String netId) {
		this.netId = netId;
	}

	public String getOriginalFirstName() {
		return OriginalFirstName;
	}

	public void setOriginalFirstName(String OriginalFirstName) {
		this.OriginalFirstName = OriginalFirstName;
	}
	
	public String getOriginalLastName() {
		return OriginalLastName;
	}

	public void setOriginalLastName(String OriginalLastName) {
		this.OriginalLastName = OriginalLastName;
	}

	public String getOriginalEmail() {
		return OriginalEmail;
	}

	public void setOriginalEmail(String OriginalEmail) {
		this.OriginalEmail = OriginalEmail;
	}

	public String getUpdatedLastName() {
		return UpdatedLastName;
	}

	public void setUpdatedLastName(String UpdatedLastName) {
		this.UpdatedLastName = UpdatedLastName;
	}

	public String getUpdatedFirstName() {
		return UpdatedFirstName;
	}

	public void setUpdatedFirstName(String UpdatedFirstName) {
		this.UpdatedFirstName = UpdatedFirstName;
	}

	public String getUpdatedEmail() {
		return UpdatedEmail;
	}

	public void setUpdatedEmail(String UpdatedEmail) {
		this.UpdatedEmail = UpdatedEmail;
	}

	public String getUpdatedRole() {
		return UpdatedRole;
	}

	public void setUpdatedRole(String UpdatedRole) {
		this.UpdatedRole = UpdatedRole;
	}

	public String getAction() {
		return Action;
	}

	public void setAction(String Action) {
		this.Action = Action;
	}

	public Date getModifiedDate() {
		return ModifiedDate;
	}

	public void setModifiedDate(Date ModifiedDate) {
		this.ModifiedDate = ModifiedDate;
	}
	
	public String toString(){
		return  "net_id:" + getNetId() + ", OriginalFirstName:" + getOriginalFirstName() + ", OriginalLastName:" + getOriginalLastName()  + 
				", OriginalEmail:" + getOriginalEmail() + ", UpdatedFirstName:" + getUpdatedFirstName() + 
				", UpdatedLastName:" +  getUpdatedLastName() + ", UpdatedEmail:" + getUpdatedEmail() + ", UpdatedRole:" + getUpdatedRole() +
				", Action:" + getAction() + ", ModifiedDate:" + getModifiedDate();
	}
	
}
