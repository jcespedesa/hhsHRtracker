package com.trc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="contracts")
public class ContractsEntity 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long contractid;
	
	@Column(name="contract")
	private String contract;
	
	@Column(name="projectnumber")
	private String projectNumber;
	
	@Column(name="project")
	private String project;
	
	@Column(name="type")
	private String type;
	
	@Column(name="status")
	private String status;
	
	@Column(name="notes")
	private String notes;
	
	@Column(name="initialdate")
	private String initialDate;
	
	@Column(name="realinitialdate")
	private String realInitialDate;
	
	@Column(name="finaldate")
	private String finalDate;
	
	@Column(name="realfinaldate")
	private String realFinalDate;
	
	@Column(name="typeproject")
	private String typeProject;
	
	
	@Override
	public String toString()
	{
		return "ContractsEntity[contractid="+ contractid +",contract="+ contract +",projectNumber="+ projectNumber +",project="+ project +",type="+ type +",status="+ status +",notes="+ notes +",initialDate="+ initialDate +",realInitialDate="+ realInitialDate +",finalDate="+ finalDate +",realFinalDate="+ realFinalDate +",typeProject="+ typeProject +"]";				
		
	}


	public Long getContractid() 
	{
		return contractid;
	}


	public void setContractid(Long contractid) 
	{
		this.contractid=contractid;
	}


	public String getContract() 
	{
		return contract;
	}


	public void setContract(String contract) 
	{
		this.contract=contract;
	}


	public String getProjectNumber() 
	{
		return projectNumber;
	}


	public void setProjectNumber(String projectNumber) 
	{
		this.projectNumber=projectNumber;
	}


	public String getProject() 
	{
		return project;
	}


	public void setProject(String project) 
	{
		this.project=project;
	}


	public String getType() 
	{
		return type;
	}


	public void setType(String type) 
	{
		this.type=type;
	}


	public String getStatus() 
	{
		return status;
	}


	public void setStatus(String status) 
	{
		this.status=status;
	}


	public String getNotes() 
	{
		return notes;
	}


	public void setNotes(String notes) 
	{
		this.notes=notes;
	}


	public String getInitialDate() 
	{
		return initialDate;
	}


	public void setInitialDate(String initialDate) 
	{
		this.initialDate=initialDate;
	}


	public String getRealInitialDate() 
	{
		return realInitialDate;
	}


	public void setRealInitialDate(String realInitialDate) 
	{
		this.realInitialDate=realInitialDate;
	}


	public String getFinalDate() 
	{
		return finalDate;
	}


	public void setFinalDate(String finalDate) 
	{
		this.finalDate=finalDate;
	}


	public String getRealFinalDate() 
	{
		return realFinalDate;
	}


	public void setRealFinalDate(String realFinalDate) 
	{
		this.realFinalDate=realFinalDate;
	}


	public String getTypeProject() 
	{
		return typeProject;
	}


	public void setTypeProject(String typeProject) 
	{
		this.typeProject=typeProject;
	}
	
	
}
