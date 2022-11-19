package com.trc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.entities.ContractATsEntity;
import com.trc.repositories.ContractATsRepository;

@Service
public class ContractATsService 
{
	@Autowired
	ContractATsRepository repository;
	
	public List<ContractATsEntity> getATbyContract(String contract)
	{
		List<ContractATsEntity> result=(List<ContractATsEntity>) repository.getAssignated(contract);
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<ContractATsEntity>();
		
	}
	
	public void deleteProject(Long id) throws RecordNotFoundException
	{
		Optional<ContractATsEntity> contract=repository.findById(id);
		
		if(contract.isPresent())
			repository.deleteById(id);
		else
			throw new RecordNotFoundException("No Assigned Training record exist for given id");
		
		
	}
	
	public ContractATsEntity createContract(ContractATsEntity entity)
	{
		
		entity=repository.save(entity);
			
		return entity;
		
	}
	
	public ContractATsEntity getContractATbyId(Long Id) throws RecordNotFoundException
	{
		
		Optional<ContractATsEntity> contract=repository.findById(Id);
			
		if(contract.isPresent())
			return contract.get();
		else
			throw new RecordNotFoundException("No record exist for given id");
		
	}
	
	public int findDuplicates(String projectNum,String contract)
	{
		int priznakDuplicate=0;
		
		priznakDuplicate=repository.findAssignationDuplicity(projectNum,contract);
		
		
		return priznakDuplicate;
	}

}
