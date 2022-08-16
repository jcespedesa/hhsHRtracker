package com.trc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.entities.CertificatesEntity;
import com.trc.repositories.CertificatesRepository;


@Service
public class CertificatesService 
{
	@Autowired
	CertificatesRepository repository;
	
	public List<CertificatesEntity> getAllCerts()
	{
		List<CertificatesEntity> result=(List<CertificatesEntity>) repository.findAll();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<CertificatesEntity>();
		
	}
	
	public List<CertificatesEntity> getActives()
	{
		List<CertificatesEntity> result=(List<CertificatesEntity>) repository.getActives();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<CertificatesEntity>();
		
	}
	
	public CertificatesEntity getCertById(Long id) throws RecordNotFoundException
	{
		Optional<CertificatesEntity> cert=repository.findById(id);
		
		if(cert.isPresent())
			return cert.get();
		else
			throw new RecordNotFoundException("No record exist for given id");
	}
	
	public CertificatesEntity createOrUpdate(CertificatesEntity entity)
	{
		if(entity.getCertid()==null)
		{
			entity=repository.save(entity);
			
			return entity;
		}
		else
		{
			Optional<CertificatesEntity> cert=repository.findById(entity.getCertid());
			
			if(cert.isPresent())
			{
				
				CertificatesEntity newEntity=cert.get();
				
				newEntity.setCertNumber(entity.getCertNumber());
				newEntity.setCertName(entity.getCertName());
				
				newEntity.setNotes(entity.getNotes());
				newEntity.setActive(entity.getActive());
					
				newEntity=repository.save(newEntity);
				
				return newEntity;
				
			}
			else
			{
				entity=repository.save(entity);
				
				return entity;
				
			}
			
		}
				
		
	}
	
	public void deleteCertById(Long id) throws RecordNotFoundException
	{
		Optional<CertificatesEntity> cert=repository.findById(id);
		
		if(cert.isPresent())
			repository.deleteById(id);
		else
			throw new RecordNotFoundException("No cert record exist for given id");
			
	}
		
	
	public int findDuplicates(String certNumber)
	{
		int priznakDuplicate=0;
		
		priznakDuplicate=repository.findCertDuplicity(certNumber);
		
		
		return priznakDuplicate;
	}

	public String getCertName(String certNumber) 
	{
		String certName=null;
		
		certName=repository.getCertName(certNumber);
		
		return certName;
	}
}
