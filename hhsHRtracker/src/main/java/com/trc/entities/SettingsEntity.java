package com.trc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="settings")
public class SettingsEntity 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long settingid;
	
		
	@Column(name="sname")
	private String sname;
	
	@Column(name="path")
	private String path;
	
	@Column(name="param1")
	private String param1;
	
	@Column(name="param2")
	private String param2;
	
	//Constructors	
	
	@Override
	public String toString()
	{
		return "SettingsEntity[settingid="+ settingid +",sname="+ sname +",path="+ path +",param1="+ param1 +",param2="+ param2 +"]";				
		
	}
	
	//Setters and getters

	public Long getSettingid() 
	{
		return settingid;
	}

	public void setSettingid(Long settingid) 
	{
		this.settingid=settingid;
	}

	
	public String getSname() 
	{
		return sname;
	}

	public void setSname(String sname) 
	{
		this.sname=sname;
	}

	public String getPath() 
	{
		return path;
	}

	public void setPath(String path) 
	{
		this.path=path;
	}

	public String getParam1() 
	{
		return param1;
	}

	public void setParam1(String param1) 
	{
		this.param1=param1;
	}

	public String getParam2() 
	{
		return param2;
	}

	public void setParam2(String param2) 
	{
		this.param2=param2;
	}
}
