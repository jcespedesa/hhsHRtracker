package com.trc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sites")
public class SitesEntity 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long siteid;
	
	@Column(name="sitenumber")
	private String siteNumber;
	
	@Column(name="sname")
	private String sname;
	
	@Column(name="address")
	private String address;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="provider1")
	private String provider1;
	
	@Column(name="account1")
	private String account1;
	
	@Column(name="provider2")
	private String provider2;
	
	@Column(name="account2")
	private String account2;
	
	@Column(name="provider3")
	private String provider3;
	
	@Column(name="account3")
	private String account3;
	
	@Column(name="provider4")
	private String provider4;
	
	@Column(name="account4")
	private String account4;
	
	@Column(name="active")
	private String active;
	
	@Column(name="division")
	private String division;
	
	@Override
	public String toString()
	{
		return "SitesEntity[siteid="+ siteid +",siteNumber="+ siteNumber +",sname="+ sname +",address="+ address +",phone="+ phone +",provider1="+ provider1 +",provider2="+ provider2 +",provider3="+ provider3 +",provider4="+ provider4 +",active="+ active +",account1="+ account1 +",account2="+ account2 +",account3="+ account3 +",account4="+ account4 +",division="+ division +"]";				
		
	}

	public Long getSiteid() 
	{
		return siteid;
	}

	public void setSiteid(Long siteid) 
	{
		this.siteid=siteid;
	}

	public String getSname() 
	{
		return sname;
	}

	public void setSname(String sname) 
	{
		this.sname=sname;
	}

	public String getAddress() 
	{
		return address;
	}

	public void setAddress(String address) 
	{
		this.address=address;
	}

	public String getProvider1() 
	{
		return provider1;
	}

	public void setProvider1(String provider1) 
	{
		this.provider1=provider1;
	}

	public String getProvider2() 
	{
		return provider2;
	}

	public void setProvider2(String provider2) 
	{
		this.provider2=provider2;
	}

	public String getProvider3() 
	{
		return provider3;
	}

	public void setProvider3(String provider3) 
	{
		this.provider3=provider3;
	}

	public String getProvider4() 
	{
		return provider4;
	}

	public void setProvider4(String provider4) 
	{
		this.provider4=provider4;
	}

	public String getActive() 
	{
		return active;
	}

	public void setActive(String active) 
	{
		this.active=active;
	}

	public String getPhone() 
	{
		return phone;
	}

	public void setPhone(String phone) 
	{
		this.phone=phone;
	}

	public String getSiteNumber() 
	{
		return siteNumber;
	}

	public void setSiteNumber(String siteNumber) 
	{
		this.siteNumber=siteNumber;
	}

	public String getAccount1() 
	{
		return account1;
	}

	public void setAccount1(String account1) 
	{
		this.account1=account1;
	}

	public String getAccount2() 
	{
		return account2;
	}

	public void setAccount2(String account2) 
	{
		this.account2=account2;
	}

	public String getAccount3() 
	{
		return account3;
	}

	public void setAccount3(String account3) 
	{
		this.account3=account3;
	}

	public String getAccount4() 
	{
		return account4;
	}

	public void setAccount4(String account4) 
	{
		this.account4=account4;
	}

	public String getDivision() 
	{
		return division;
	}

	public void setDivision(String division) 
	{
		this.division=division;
	}
}
