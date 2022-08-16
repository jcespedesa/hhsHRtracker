package com.trc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="buffertime")
public class BufferTimeEntity 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long recordid;
	
	@Column(name="username")
	private String username;
	
	@Column(name="todaydate",insertable=false)
	private String todayDate;
	
	@Column(name="datemonthago",updatable=false, insertable=false)
	private String dateMonthAgo;
	
	@Column(name="dateyearago",updatable=false, insertable=false)
	private String dateYearAgo;
	
	@Column(name="dateyearfromnow",updatable=false, insertable=false)
	private String dateYearFromNow;
	
	@Column(name="datemonthfromnow",updatable=false, insertable=false)
	private String dateMonthFromNow;
	
	@Column(name="yesterdaydate",updatable=false, insertable=false)
	private String yesterdayDate;
	
	@Column(name="datefiveyearsago",nullable=false, insertable=false)
	private String dateFiveYearsAgo;
	
	@Column(name="strobe")
	private String strobe;
	
	@Override
	public String toString()
	{
		return "BufferTimeEntity[recordid="+ recordid +",username="+ username +",todayDate="+ todayDate +",dateMonthAgo="+ dateMonthAgo +",dateYearFromNow="+ dateYearFromNow +",dateMonthFromNow="+ dateMonthFromNow +",yesterdayDate="+ yesterdayDate +",dateFiveYearsAgo="+ dateFiveYearsAgo +",strobe="+ strobe +"]";				
		
	}

	public String getStrobe() 
	{
		return strobe;
	}

	public void setStrobe(String strobe) 
	{
		this.strobe=strobe;
	}

	public Long getRecordid() 
	{
		return recordid;
	}

	public void setRecordid(Long recordid) 
	{
		this.recordid=recordid;
	}

	public String getUsername() 
	{
		return username;
	}

	public void setUsername(String username) 
	{
		this.username=username;
	}

	
	public String getDateMonthAgo() 
	{
		return dateMonthAgo;
	}

	public void setDateMonthAgo(String dateMonthAgo) 
	{
		this.dateMonthAgo=dateMonthAgo;
	}

	public String getDateYearAgo() 
	{
		return dateYearAgo;
	}

	public void setDateYearAgo(String dateYearAgo) 
	{
		this.dateYearAgo=dateYearAgo;
	}

	public String getDateYearFromNow() 
	{
		return dateYearFromNow;
	}

	public void setDateYearFromNow(String dateYearFromNow) 
	{
		this.dateYearFromNow=dateYearFromNow;
	}

	public String getDateMonthFromNow() 
	{
		return dateMonthFromNow;
	}

	public void setDateMonthFromNow(String dateMonthFromNow) 
	{
		this.dateMonthFromNow=dateMonthFromNow;
	}

	public String getYesterdayDate() 
	{
		return yesterdayDate;
	}

	public void setYesterdayDate(String yesterdayDate) 
	{
		this.yesterdayDate=yesterdayDate;
	}

	public String getDateFiveYearsAgo() 
	{
		return dateFiveYearsAgo;
	}

	public void setDateFiveYearsAgo(String dateFiveYearsAgo) 
	{
		this.dateFiveYearsAgo=dateFiveYearsAgo;
	}

	public String getTodayDate() 
	{
		return todayDate;
	}

	public void setTodayDate(String todayDate) 
	{
		this.todayDate=todayDate;
	}

	
}
