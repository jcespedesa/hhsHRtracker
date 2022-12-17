package com.trc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="clients")
public class ClientsEntity 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long clientid;
	
	@Column(name="cname")
	private String cname;
	
	@Column(name="datehire")
	private String dateHire;
	
	@Column(name="realdatehire")
	private String realDateHire;
	
	@Column(name="project")
	private String project;
	
	@Column(name="projectname")
	private String projectName;
	
	@Column(name="contract")
	private String contract;
	
	@Column(name="title")
	private String title;
	
	@Column(name="status")
	private String status;
	
	
	@Column(name="licensure")
	private String licensure;
	
	@Column(name="antibullrec")
	private String antiBullRec;
	
	
	@Column(name="education")
	private String education;
	
	@Column(name="period")
	private String period;
	
	@Column(name="active")
	private String active;
	
	@Column(name="program")
	private String program;
	
	@Column(name="notes")
	private String notes;
	
	@Column(name="expiredcert")
	private String expiredCert;
	
	@Column(name="employeenum")
	private String employeeNum;
	
	@Column(name="email")
	private String email;
	
	@Column(name="majorhumanservices")
	private String majorHumanServices;
	
	@Column(name="datecreation",updatable=false, insertable=false)
	private String dateCreation;

	//Constructors
	@Override
	public String toString()
	{
		return "ClientsEntity[clientid="+ clientid +",cname="+ cname +",dateHire="+ dateHire +",realDateHire="+ realDateHire +",project="+ project +","
				+" projectName="+ projectName +",contract="+ contract +",title="+ title +",status="+ status +"email="+ email +","
				+" antiBullRec="+ antiBullRec +",education="+ education +",period="+ period +",majorHumanServices="+ majorHumanServices +","
				+ "active="+ active +",program="+ program +",notes="+ notes +",expiredCert="+ expiredCert +",dateCreation="+ dateCreation +",employeeNum="+ employeeNum +"]"; 				
		
	}
	
	
	//Getters and setters
	
	public String getNotes() 
	{
		return notes;
	}


	public void setNotes(String notes) 
	{
		this.notes=notes;
	}


	public String getProgram() 
	{
		return program;
	}


	public void setProgram(String program) 
	{
		this.program=program;
	}


	public Long getClientid() 
	{
		return clientid;
	}

	public void setClientid(Long clientid) 
	{
		this.clientid=clientid;
	}

	public String getCname() 
	{
		return cname;
	}

	public void setCname(String cname) 
	{
		this.cname=cname;
	}

	public String getDateHire() 
	{
		return dateHire;
	}

	
	public void setDateHire(String dateHire) 
	{
		this.dateHire=dateHire;
	}

	public String getRealDateHire() 
	{
		return realDateHire;
	}

	public void setRealDateHire(String realDateHire) 
	{
		this.realDateHire=realDateHire;
	}

	public String getProject() 
	{
		return project;
	}

	public void setProject(String project) 
	{
		this.project=project;
	}

	public String getProjectName() 
	{
		return projectName;
	}

	public void setProjectName(String projectName) 
	{
		this.projectName=projectName;
	}

	public String getContract() 
	{
		return contract;
	}

	public void setContract(String contract) 
	{
		this.contract=contract;
	}

	public String getTitle() 
	{
		return title;
	}

	public void setTitle(String title) 
	{
		this.title=title;
	}

	public String getStatus() 
	{
		return status;
	}

	public void setStatus(String status) 
	{
		this.status=status;
	}

	
	public String getLicensure() 
	{
		return licensure;
	}

	public void setLicensure(String licensure) 
	{
		this.licensure=licensure;
	}

	public String getAntiBullRec() 
	{
		return antiBullRec;
	}

	public void setAntiBullRec(String antiBullRec) 
	{
		this.antiBullRec=antiBullRec;
	}

	public String getEducation() 
	{
		return education;
	}

	public void setEducation(String education) 
	{
		this.education=education;
	}

	public String getPeriod() 
	{
		return period;
	}

	public void setPeriod(String period) 
	{
		this.period=period;
	}

	public String getActive() 
	{
		return active;
	}

	public void setActive(String active) 
	{
		this.active=active;
	}

	public String getDateCreation() 
	{
		return dateCreation;
	}

	public void setDateCreation(String dateCreation) 
	{
		this.dateCreation=dateCreation;
	}


	public String getExpiredCert() 
	{
		return expiredCert;
	}


	public void setExpiredCert(String expiredCert) 
	{
		this.expiredCert=expiredCert;
	}


	public String getEmployeeNum() 
	{
		return employeeNum;
	}


	public void setEmployeeNum(String employeeNum) 
	{
		this.employeeNum=employeeNum;
	}


	public String getEmail() 
	{
		return email;
	}


	public void setEmail(String email) 
	{
		this.email=email;
	}


	public String getMajorHumanServices() 
	{
		return majorHumanServices;
	}


	public void setMajorHumanServices(String majorHumanServices) 
	{
		this.majorHumanServices=majorHumanServices;
	}

	
	
	
}
