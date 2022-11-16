package com.trc.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.text.ParseException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.entities.ClientsEntity;
import com.trc.entities.ContractsEntity;
import com.trc.repositories.AssigCertsRepository;
import com.trc.repositories.ClientsRepository;


@Service
public class ClientsService 
{
	@Autowired
	ClientsRepository repository;
	
	@Autowired
	AssigCertsRepository repositoryAssigCerts;
	
	public List<ClientsEntity> getAllByProject(String project) throws ParseException
	{
		Date todayDate=null;
		Date bufferDate=null;
					
			
		List<ClientsEntity> result=(List<ClientsEntity>) repository.getAllByProject(project);
		
		if(result.size() > 0)
			return result;
		else
		{
			//Trying to get today's date
	    	todayDate=new Date();
			
			for(ClientsEntity entity : result)
			{
				//Reviewing expired dates
				//converting strings to dates
				//bufferDate=new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).parse(entity.getRealMpd());
				
				if(bufferDate.compareTo(todayDate) > 0)
				{
					System.out.println("");
				}
			}
			
			return new ArrayList<ClientsEntity>();
			
		}	
		
	}
	
	public List<ClientsEntity> getAllActives() 
	{
	
		List<ClientsEntity> result=(List<ClientsEntity>) repository.getAllActives();
		
		return result;
	}
	
	
	public ClientsEntity save(ClientsEntity entity)
	{
		Optional<ClientsEntity> client=repository.findById(entity.getClientid());
						
		ClientsEntity newEntity=client.get();
				
		newEntity.setCname(entity.getCname());
				
		newEntity.setDateHire(entity.getDateHire());
		newEntity.setRealDateHire(entity.getDateHire());
				
		newEntity.setContract(entity.getContract());
		newEntity.setTitle(entity.getTitle());
		newEntity.setStatus(entity.getStatus());
				
		newEntity.setPeriod(entity.getPeriod());
		newEntity.setActive(entity.getActive());
		
		newEntity.setEducation(entity.getEducation());
		newEntity.setLicensure(entity.getLicensure());
		
		newEntity.setAntiBullRec(entity.getAntiBullRec());
		newEntity.setNotes(entity.getNotes());
		
		newEntity.setEmployeeNum(entity.getEmployeeNum());
		newEntity.setEmail(entity.getEmail());
						
		newEntity=repository.save(newEntity);
				
		return newEntity;
				
	}
	
	
	
	public void deleteClientById(Long id) throws RecordNotFoundException
	{
		Optional<ClientsEntity> client=repository.findById(id);
		
		if(client.isPresent())
			repository.deleteById(id);
		else
			throw new RecordNotFoundException("No Client record exist for given id");
		
		
	}
	
	public ClientsEntity getClientById(Long id) throws RecordNotFoundException
	{
		Optional<ClientsEntity> client=repository.findById(id);
		
		if(client.isPresent())
			return client.get();
		else
			throw new RecordNotFoundException("No record exist for given id");
	}
	
	public String getName(Long id) 
	{
		String cname=repository.getNameById(id);
		
		return cname;
		
	}
	
	public ClientsEntity create(ClientsEntity entity)
	{
		entity=repository.save(entity);
		
		return entity;
	}
	
	public ClientsEntity saveTracker(ClientsEntity entity)
	{
		Optional<ClientsEntity> client=repository.findById(entity.getClientid());
						
		ClientsEntity newEntity=client.get();
								
		newEntity=repository.save(newEntity);
				
		return newEntity;
				
	}

	public String findingNumExpiredCerts(String clientId,String todayDate,String period) 
	{
		int totalRecords=0;
		
		String expiredCert=null;
		
		totalRecords=repositoryAssigCerts.countExpiredRecordsById(clientId,todayDate,period);
		
		//System.out.println("total records found is "+ totalRecords);
		
		if(totalRecords==0)
			expiredCert="No";
		else
			expiredCert="Yes";
		
		//System.out.println("Result of expired certs for "+ clientId +" is "+ expiredCert);
		
		return expiredCert;
	}
	
	
	public void tranferringEmployee(Long clientId,String projectNumber,String projectName)
	{
		repository.tranferEmployee(clientId,projectNumber,projectName);
	
	}
	
	
	public ClientsEntity createOrUpdate(ClientsEntity entity)
	{
		if(entity.getClientid()==null)
		{
						
			entity=repository.save(entity);
			
			return entity;
		}
		else
		{
			Optional<ClientsEntity> client=repository.findById(entity.getClientid());
			
			if(client.isPresent())
			{
				
				ClientsEntity newEntity=client.get();
				
				newEntity.setCname(entity.getCname());
				
				newEntity.setDateHire(entity.getDateHire());
				newEntity.setRealDateHire(entity.getDateHire());
						
				newEntity.setContract(entity.getContract());
				newEntity.setTitle(entity.getTitle());
				newEntity.setStatus(entity.getStatus());
						
				newEntity.setPeriod(entity.getPeriod());
				newEntity.setActive(entity.getActive());
				
				newEntity.setEducation(entity.getEducation());
				newEntity.setLicensure(entity.getLicensure());
				
				newEntity.setAntiBullRec(entity.getAntiBullRec());
				newEntity.setNotes(entity.getNotes());
				
				newEntity.setEmployeeNum(entity.getEmployeeNum());
				newEntity.setEmail(entity.getEmail());
					
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
	
	
	public List<ClientsEntity> searchByName(String stringSearch)
	{
		List<ClientsEntity> result=(List<ClientsEntity>) repository.searchClientsByName(stringSearch);
		
		return result;
				
	}
	
	public List<ClientsEntity> searchBySelection(Long stringSearch)
	{
		List<ClientsEntity> result=(List<ClientsEntity>) repository.searchClientsBySelection(stringSearch);
		
		return result;
				
	}
	
}
