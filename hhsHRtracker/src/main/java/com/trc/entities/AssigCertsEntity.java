package com.trc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="assignedcerts")
public class AssigCertsEntity 
{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long recordid;
	
	@Column(name="clientid")
	private String clientId;
	
	@Column(name="period")
	private String period;
	
	@Column(name="certnumber")
	private String certNumber;
	
	@Column(name="certname")
	private String certName;
	
	@Column(name="expirationdate")
	private String expirationDate;
	
	@Column(name="realexpirationdate")
	private String realExpirationDate;
	
	@Column(name="status")
	private String status;
	
	@Column(name="notes")
	private String notes;
	
	//Constructors
	
	@Override
	public String toString()
	{
		return "AssigCertsEntity[recordid="+ recordid +",clientId="+ clientId +",period="+ period +",certNumber="+ certNumber +",certName="+ certName +",expirationDate="+ expirationDate +",realExpirationDate="+ realExpirationDate +",status="+ status +",notes="+ notes +"]";				
				
	}

	public Long getRecordid() {
		return recordid;
	}

	public void setRecordid(Long recordid) {
		this.recordid = recordid;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getCertNumber() {
		return certNumber;
	}

	public void setCertNumber(String certNumber) {
		this.certNumber = certNumber;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getRealExpirationDate() {
		return realExpirationDate;
	}

	public void setRealExpirationDate(String realExpirationDate) {
		this.realExpirationDate = realExpirationDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getCertName() {
		return certName;
	}

	public void setCertName(String certName) {
		this.certName = certName;
	}
	
			
	
}
