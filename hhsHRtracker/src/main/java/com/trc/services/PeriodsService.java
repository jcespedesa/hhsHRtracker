package com.trc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.entities.PeriodsEntity;
import com.trc.repositories.PeriodsRepository;

@Service
public class PeriodsService 
{
	@Autowired
	PeriodsRepository repository;
	
	public List<PeriodsEntity> getAllPeriods()
	{
		List<PeriodsEntity> result=(List<PeriodsEntity>) repository.findAll();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<PeriodsEntity>();
		
	}
	
	public List<PeriodsEntity> getActivesByUdelnyBes()
	{
		List<PeriodsEntity> result=(List<PeriodsEntity>) repository.getActivesByUdelnyBes();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<PeriodsEntity>();
		
	}
	
	public PeriodsEntity getPeriodById(Long id) throws RecordNotFoundException
	{
		Optional<PeriodsEntity> period=repository.findById(id);
		
		if(period.isPresent())
			return period.get();
		else
			throw new RecordNotFoundException("No record exist for given id");
	}
	
	public PeriodsEntity createPeriod(PeriodsEntity entity)
	{
		return entity=repository.save(entity);
			
	}
	
	public void deletePeriodById(Long id) throws RecordNotFoundException
	{
		Optional<PeriodsEntity> period=repository.findById(id);
		
		if(period.isPresent())
			repository.deleteById(id);
		else
			throw new RecordNotFoundException("No Period record exist for given id");
			
	}
	
	public List<Integer> findNonRepeatedUB()
	{
		int start=1;
		int end=100;
		
				
		//Retrieving list of existent udelny beses
		List<Integer> originalList=repository.getOriginalListUB();
		
		//Generating all possible udelny beses to 100
		List<Integer> listUB=IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
		
		//Obtaining the list of UB with no similar in the table
		listUB.removeAll(originalList);
		
		return listUB;
		
	}
	
	public int findDuplicates(String period)
	{
		int priznakDuplicate=0;
		
		priznakDuplicate=repository.findPeriodDuplicity(period);
		
		
		return priznakDuplicate;
	}
}
