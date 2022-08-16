package com.trc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="divisions")
public class DivisionsEntity 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long divisionid;
	
	@Column(name="dnumber")
	private String dnumber;
	
	@Column(name="dname")
	private String dname;
	
	@Column(name="active")
	private String active;
	
	@Column(name="priznakapp")
	private String priznakApp;
	
	@Override
	public String toString()
	{
		return "DivisionsEntity[divisionid="+ divisionid +",dnumber="+ dnumber +",dname="+ dname +",active="+ active +",priznakApp="+ priznakApp +"]";				
		
	}

	public Long getDivisionid() 
	{
		return divisionid;
	}

	public void setDivisionid(Long divisionid) 
	{
		this.divisionid=divisionid;
	}

	public String getDnumber() 
	{
		return dnumber;
	}

	public void setDnumber(String dnumber) 
	{
		this.dnumber=dnumber;
	}

	public String getDname() 
	{
		return dname;
	}

	public void setDname(String dname) 
	{
		this.dname=dname;
	}

	public String getActive() 
	{
		return active;
	}

	public void setActive(String active) 
	{
		this.active=active;
	}

	public String getPriznakApp() 
	{
		return priznakApp;
	}

	public void setPriznakApp(String priznakApp) 
	{
		this.priznakApp=priznakApp;
	}
	
	
}
