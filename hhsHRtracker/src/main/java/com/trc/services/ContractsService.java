package com.trc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.entities.ContractsEntity;
import com.trc.repositories.ContractsRepository;

@Service
public class ContractsService 
{
	@Autowired
	ContractsRepository repository;
	
	public List<ContractsEntity> getAll()
	{
		List<ContractsEntity> result=(List<ContractsEntity>) repository.findAll();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<ContractsEntity>();
		
	}
	
	public List<ContractsEntity> getActives()
	{
		List<ContractsEntity> result=(List<ContractsEntity>) repository.getAll();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<ContractsEntity>();
		
	}
	
	public ContractsEntity getContractById(Long id) throws RecordNotFoundException
	{
		Optional<ContractsEntity> contract=repository.findById(id);
		
		if(contract.isPresent())
			return contract.get();
		else
			throw new RecordNotFoundException("No record exist for given id");
	}
	
	public ContractsEntity createOrUpdate(ContractsEntity entity)
	{
		if(entity.getContractid()==null)
		{
			entity.setInitialDate(entity.getInitialDate());
			entity.setRealInitialDate(entity.getInitialDate());
			
			entity.setFinalDate(entity.getFinalDate());
			entity.setRealFinalDate(entity.getFinalDate());
			
			entity=repository.save(entity);
			
			return entity;
		}
		else
		{
			Optional<ContractsEntity> contract=repository.findById(entity.getContractid());
			
			if(contract.isPresent())
			{
				
				ContractsEntity newEntity=contract.get();
				
				newEntity.setContract(entity.getContract());
				newEntity.setProjectNumber(entity.getProjectNumber());
				
				newEntity.setProject(entity.getProject());
				newEntity.setType(entity.getType());
				
				newEntity.setStatus(entity.getStatus());
				newEntity.setNotes(entity.getNotes());
				
				newEntity.setInitialDate(entity.getInitialDate());
				newEntity.setRealInitialDate(entity.getInitialDate());
				
				newEntity.setFinalDate(entity.getFinalDate());
				newEntity.setRealFinalDate(entity.getFinalDate());
				
				newEntity.setTypeProject(entity.getTypeProject());
					
				newEntity=repository.save(newEntity);
				
				return newEntity;
				
			}
			else
			{
				entity.setInitialDate(entity.getInitialDate());
				entity.setRealInitialDate(entity.getInitialDate());
				
				entity.setFinalDate(entity.getFinalDate());
				entity.setRealFinalDate(entity.getFinalDate());
				
				entity=repository.save(entity);
				
				return entity;
				
			}
			
		}
				
		
	}
	
	public void deleteContractById(Long id) throws RecordNotFoundException
	{
		Optional<ContractsEntity> contract=repository.findById(id);
		
		if(contract.isPresent())
			repository.deleteById(id);
		else
			throw new RecordNotFoundException("No contract definition exists for given id");
			
	}
		
	
	public int findDuplicates(String contract)
	{
		int priznakDuplicate=0;
		
		priznakDuplicate=repository.findContractDuplicity(contract);
		
		
		return priznakDuplicate;
	}

	
}
