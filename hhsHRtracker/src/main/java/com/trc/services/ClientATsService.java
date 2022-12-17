package com.trc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.entities.ClientATsEntity;
import com.trc.repositories.ClientATsRepository;


@Service
public class ClientATsService 
{
	@Autowired
	ClientATsRepository repository;
	
	public List<ClientATsEntity> getATbyClient(String clientId)
	{
		List<ClientATsEntity> result=(List<ClientATsEntity>) repository.getAssignated(clientId);
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<ClientATsEntity>();
		
	}
	
	public void deleteTraining(Long id) throws RecordNotFoundException
	{
		Optional<ClientATsEntity> training=repository.findById(id);
		
		if(training.isPresent())
			repository.deleteById(id);
		else
			throw new RecordNotFoundException("No Assigned Training record exist for given client");
		
		
	}
	/*
	public ClientATsEntity createTraining(ClientATsEntity entity)
	{
		
		entity=repository.save(entity);
			
		return entity;
		
	}
	*/
	
	public ClientATsEntity createOrUpdate(ClientATsEntity entity)
	{
		if(entity.getRecordid()==null)
		{
			entity=repository.save(entity);
				
						
			return entity;
		}
		else
		{
			Optional<ClientATsEntity> training=repository.findById(entity.getRecordid());
			
			if(training.isPresent())
			{
				
				ClientATsEntity newEntity=training.get();
				
				newEntity.setContract(entity.getContract());
				newEntity.setTrainingNum(entity.getTrainingNum());
				newEntity.setDateCompletion(entity.getDateCompletion());
				newEntity.setRealDateCompletion(entity.getDateCompletion());
				
				newEntity.setCertReceived(entity.getCertReceived());
				newEntity.setCompleted(entity.getCompleted());
				
				newEntity.setDateCertRec(entity.getDateCertRec());
				newEntity.setRealDateCertRec(entity.getDateCertRec());
				
				newEntity.setPriznakAdd(entity.getPriznakAdd());
												
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
	
	
	public ClientATsEntity getClientATbyId(Long Id) throws RecordNotFoundException
	{
		
		Optional<ClientATsEntity> training=repository.findById(Id);
			
		if(training.isPresent())
			return training.get();
		else
			throw new RecordNotFoundException("No record exist for given id");
		
	}
	
	public int findDuplicates(String trainingNum,String clientId)
	{
		int priznakDuplicate=0;
		
		priznakDuplicate=repository.findAssignationDuplicity(trainingNum,clientId);
		
		
		return priznakDuplicate;
	}
	
	

}
