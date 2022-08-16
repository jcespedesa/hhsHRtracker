package com.trc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="certificates")
public class CertificatesEntity 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long certid;
	
	@Column(name="certnumber")
	private String certNumber;
	
	@Column(name="certname")
	private String certName;
	
	@Column(name="active")
	private String active;
	
	@Column(name="notes")
	private String notes;
	
	@Column(name="datecreation",updatable=false, insertable=false)
	private String dateCreation;
	
	@Override
	public String toString()
	{
		return "CertificatesEntity[certid="+ certid +",certNumber="+ certNumber +",certName="+ certName +",dateCreatio="+ dateCreation +",notes="+ notes +",active="+ active +"]";				
		
	}

	public Long getCertid() {
		return certid;
	}

	public void setCertid(Long certid) {
		this.certid = certid;
	}

	public String getCertNumber() {
		return certNumber;
	}

	public void setCertNumber(String certNumber) {
		this.certNumber = certNumber;
	}

	public String getCertName() {
		return certName;
	}

	public void setCertName(String certName) {
		this.certName = certName;
	}

	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(String dateCreation) {
		this.dateCreation = dateCreation;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}
	
	
		
}
