package com.trc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="clientats")
public class ClientATsEntity 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long recordid;
	
	@Column(name="clientid")
	private String clientId;
	
	@Column(name="contract")
	private String contract;
	
	@Column(name="trainingnum")
	private String trainingNum;
	
	@Column(name="completed")
	private String completed;
	
	@Column(name="datecompletion")
	private String dateCompletion;
	
	@Column(name="realdatecompletion")
	private String realDateCompletion;
	
	@Column(name="certreceived")
	private String certReceived;
	
	@Column(name="datecertrec")
	private String dateCertRec;
	
	@Column(name="realdatecertrec")
	private String realDateCertRec;
	
	@Column(name="buffername")
	private String bufferName;
	
	@Column(name="priznakadd")
	private String priznakAdd;
	
	//Constructors
	
	@Override
	public String toString()
	{
		return "ClientATsEntity[recordid="+ recordid +",clientId="+ clientId +",contract="+ contract +",trainingNum="+ trainingNum +",dateCompletion="+ dateCompletion +",realDateCompletion="+ realDateCompletion +",certReceived="+ certReceived +",dateCertRec="+ dateCertRec +",realDateCertRec="+ realDateCertRec +",bufferName="+ bufferName +",completed="+ completed +",priznakAdd="+ priznakAdd +"]";				
			
	}

	public Long getRecordid() 
	{
		return recordid;
	}

	public void setRecordid(Long recordid) 
	{
		this.recordid = recordid;
	}

	public String getClientId() 
	{
		return clientId;
	}

	public void setClientId(String clientId) 
	{
		this.clientId = clientId;
	}

	public String getContract() 
	{
		return contract;
	}

	public void setContract(String contract) 
	{
		this.contract = contract;
	}

	public String getTrainingNum() 
	{
		return trainingNum;
	}

	public void setTrainingNum(String trainingNum) 
	{
		this.trainingNum = trainingNum;
	}

	public String getDateCompletion() 
	{
		return dateCompletion;
	}

	public void setDateCompletion(String dateCompletion) 
	{
		this.dateCompletion = dateCompletion;
	}

	public String getRealDateCompletion() 
	{
		return realDateCompletion;
	}

	public void setRealDateCompletion(String realDateCompletion) 
	{
		this.realDateCompletion = realDateCompletion;
	}

	public String getCertReceived() 
	{
		return certReceived;
	}

	public void setCertReceived(String certReceived) 
	{
		this.certReceived = certReceived;
	}

	public String getDateCertRec() 
	{
		return dateCertRec;
	}

	public void setDateCertRec(String dateCertRec) 
	{
		this.dateCertRec = dateCertRec;
	}

	public String getRealDateCertRec() 
	{
		return realDateCertRec;
	}

	public void setRealDateCertRec(String realDateCertRec) 
	{
		this.realDateCertRec = realDateCertRec;
	}

	public String getBufferName() 
	{
		return bufferName;
	}

	public void setBufferName(String bufferName) 
	{
		this.bufferName = bufferName;
	}

	public String getCompleted() 
	{
		return completed;
	}

	public void setCompleted(String completed) 
	{
		this.completed = completed;
	}

	public String getPriznakAdd() 
	{
		return priznakAdd;
	}

	public void setPriznakAdd(String priznakAdd) 
	{
		this.priznakAdd=priznakAdd;
	}
	
	
	
}
