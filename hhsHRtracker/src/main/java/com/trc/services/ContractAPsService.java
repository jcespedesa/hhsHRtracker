package com.trc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.entities.ContractAPsEntity;
import com.trc.repositories.ContractAPsRepository;

@Service
public class ContractAPsService 
{
	@Autowired
	ContractAPsRepository repository;
	
	public List<ContractAPsEntity> getAPbyContract(String contract)
	{
		List<ContractAPsEntity> result=(List<ContractAPsEntity>) repository.getAssignated(contract);
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<ContractAPsEntity>();
		
	}
	
	public void deleteProject(Long id) throws RecordNotFoundException
	{
		Optional<ContractAPsEntity> contract=repository.findById(id);
		
		if(contract.isPresent())
			repository.deleteById(id);
		else
			throw new RecordNotFoundException("No Assigned Contract record exist for given id");
		
		
	}
	
	public ContractAPsEntity createContract(ContractAPsEntity entity)
	{
		
		entity=repository.save(entity);
			
		return entity;
		
	}
	
	public ContractAPsEntity getContractAPById(Long Id) throws RecordNotFoundException
	{
		
		Optional<ContractAPsEntity> contract=repository.findById(Id);
			
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
