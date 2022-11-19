package com.trc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="trainings")
public class TrainingsEntity 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long trainingid;
	
	@Column(name="tnumber")
	private String tnumber;
	
	@Column(name="tname")
	private String tname;
	
	@Column(name="projecttype")
	private String projectType;
	
	@Column(name="projecttypedef")
	private String projectTypeDef;
	
	@Column(name="active")
	private String active;
	
	@Override
	public String toString()
	{
		return "TrainingsEntity[trainingid="+ trainingid +",tnumber="+ tnumber +",tname="+ tname +",projectType="+ projectType +",active="+ active +",projectTypeDef="+ projectTypeDef +"]";				
		
	}

	public Long getTrainingid() 
	{
		return trainingid;
	}

	public void setTrainingid(Long trainingid) 
	{
		this.trainingid=trainingid;
	}

	public String getTname() 
	{
		return tname;
	}

	public void setTname(String tname) 
	{
		this.tname=tname;
	}

	public String getProjectType() 
	{
		return projectType;
	}

	public void setProjectType(String projectType) 
	{
		this.projectType=projectType;
	}

	public String getTnumber() 
	{
		return tnumber;
	}

	public void setTnumber(String tnumber) 
	{
		this.tnumber=tnumber;
	}

	public String getActive() 
	{
		return active;
	}

	public void setActive(String active) 
	{
		this.active=active;
	}

	public String getProjectTypeDef() 
	{
		return projectTypeDef;
	}

	public void setProjectTypeDef(String projectTypeDef) 
	{
		this.projectTypeDef=projectTypeDef;
	}
	
	
	
}
