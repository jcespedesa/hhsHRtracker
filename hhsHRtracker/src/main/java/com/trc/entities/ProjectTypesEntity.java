package com.trc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="projecttypes")
public class ProjectTypesEntity 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long typeid;
	
	@Column(name="typenumber")
	private String typeNumber;
	
	@Column(name="type")
	private String type;
	
	@Column(name="active")
	private String active;
	
	
	@Override
	public String toString()
	{
		return "ProjectsEntity[typeid="+ typeid +",typeNumber="+ typeNumber +",type="+ type +",active="+ active +"]";				
		
	}


	public Long getTypeid() 
	{
		return typeid;
	}


	public void setTypeid(Long typeid) 
	{
		this.typeid=typeid;
	}


	public String getTypeNumber() 
	{
		return typeNumber;
	}


	public void setTypeNumber(String typeNumber) 
	{
		this.typeNumber=typeNumber;
	}


	public String getType() 
	{
		return type;
	}


	public void setType(String type) 
	{
		this.type=type;
	}


	public String getActive() 
	{
		return active;
	}


	public void setActive(String active) 
	{
		this.active=active;
	}
	
		
}
