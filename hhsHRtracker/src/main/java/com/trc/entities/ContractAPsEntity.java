package com.trc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="contractaps")
public class ContractAPsEntity 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long recordid;
	
	@Column(name="contract")
	private String contract;
	
	@Column(name="projectnum")
	private String projectNum;
	
	@Column(name="buffername")
	private String bufferName;
	
	//Constructors
	
	@Override
	public String toString()
	{
		return "ContractAPsEntity[recordid="+ recordid +",contract="+ contract +",projectNum="+ projectNum +",byfferName="+ bufferName +"]";				
		
	}

	public Long getRecordid() 
	{
		return recordid;
	}

	public void setRecordid(Long recordid) 
	{
		this.recordid = recordid;
	}

	public String getContract() 
	{
		return contract;
	}

	public void setContract(String contract) 
	{
		this.contract = contract;
	}

	public String getProjectNum() 
	{
		return projectNum;
	}

	public void setProjectNum(String projectNum) 
	{
		this.projectNum = projectNum;
	}

	public String getBufferName() 
	{
		return bufferName;
	}

	public void setBufferName(String bufferName) 
	{
		this.bufferName = bufferName;
	}
	
	
}
