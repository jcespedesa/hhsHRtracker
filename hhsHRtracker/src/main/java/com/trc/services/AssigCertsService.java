package com.trc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.entities.AssigCertsEntity;
import com.trc.entities.CertificatesEntity;
import com.trc.repositories.AssigCertsRepository;
import com.trc.repositories.CertificatesRepository;


@Service
public class AssigCertsService 
{
	@Autowired
	AssigCertsRepository repository;
	
	@Autowired
	CertificatesRepository repositoryCerts;
	
	public List<AssigCertsEntity> getAllCertsByClientId(String period,String clientId)
	{
		List<AssigCertsEntity> result=(List<AssigCertsEntity>) repository.getAllCertsByClientId(period,clientId);
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<AssigCertsEntity>();
		
	}
	
	public void deleteCert(Long id) throws RecordNotFoundException
	{
		Optional<AssigCertsEntity> cert=repository.findById(id);
		
		if(cert.isPresent())
			repository.deleteById(id);
		else
			throw new RecordNotFoundException("No Assigned Cert record exist for given id");
		
		
	}
	
	public AssigCertsEntity createCert(AssigCertsEntity entity)
	{
		
		entity=repository.save(entity);
			
		return entity;
		
	}
	
	public AssigCertsEntity getCertById(Long id) throws RecordNotFoundException
	{
		Optional<AssigCertsEntity> cert=repository.findById(id);
		
		if(cert.isPresent())
			return cert.get();
		else
			throw new RecordNotFoundException("No record exist for given id");
	}
	
	public String reviewCerts(String clientId,String period)
	{
				
		String result=null;
		
		
		List<CertificatesEntity> listCertificates=(List<CertificatesEntity>) repositoryCerts.getActives();
		
		for(CertificatesEntity cert : listCertificates)
		{
			
			if((repository.checkExistentCert(cert.getCertNumber(),clientId,period)==0))
			{
				//System.out.println("The project number "+ cert.getCertNumber() +" is not assigned yet for user "+ clientId +" for period "+ period);
				
				AssigCertsEntity newRecord=new AssigCertsEntity();
				
				newRecord.setClientId(clientId);
				newRecord.setPeriod(period);
				newRecord.setCertNumber(cert.getCertNumber());
				
				repository.save(newRecord);
				
			}
			
		}
		
		return result;
		
	}
	
	public String getCertName(String certName) throws RecordNotFoundException
	{
		String certNumber=repositoryCerts.getCertName(certName);
		
		return certNumber;
	}

	public void updateCert(Long id, String expirationDate, String notes) 
	{
	
		repository.updateCert(id,expirationDate,notes);
		
	}
	
	public String compareDates(Long recordId, String todayDate) 
	{
		int priznakCompare=0;
		
		String status=null;
		
		priznakCompare=repository.compareDateExp(recordId,todayDate);
		
		if(priznakCompare==1)
			status="Expired";
		else
			status="Current";
		
		return status;
		
	}
	
	public String expireIn15days(Long recordId, String fifteenDaysToDateString) 
	{
		int priznakCompare=0;
		
		String aboutToExpire=null;
		
		priznakCompare=repository.compareSoonExp(recordId,fifteenDaysToDateString);
		
		if(priznakCompare==1)
			aboutToExpire="Yes";
		else
			aboutToExpire="No";
		
		return aboutToExpire;
		
	}
}
