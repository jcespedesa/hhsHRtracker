package com.trc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.entities.PortfoliosEntity;
import com.trc.repositories.PortfoliosRepository;

@Service
public class PortfoliosService 
{
	@Autowired
	PortfoliosRepository repository;
	
	public List<PortfoliosEntity> getAllPortfolios()
	{
		List<PortfoliosEntity> result=(List<PortfoliosEntity>) repository.findAll();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<PortfoliosEntity>();
		
	}
	
	public List<PortfoliosEntity> getAllByName()
	{
		List<PortfoliosEntity> result=repository.getAllByName();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<PortfoliosEntity>();
		
	}
	
	public PortfoliosEntity getPortfolioById(Long id) throws RecordNotFoundException
	{
		Optional<PortfoliosEntity> portfolio=repository.findById(id);
		
		if(portfolio.isPresent())
			return portfolio.get();
		else
			throw new RecordNotFoundException("No record exist for given id");
	}
	
	public PortfoliosEntity createOrUpdate(PortfoliosEntity entity)
	{
		if(entity.getPid()==null)
		{
			entity=repository.save(entity);
			
			return entity;
		}
		else
		{
			Optional<PortfoliosEntity> portfolio=repository.findById(entity.getPid());
			
			if(portfolio.isPresent())
			{
				
				PortfoliosEntity newEntity=portfolio.get();
				
				newEntity.setPname(entity.getPname());
				newEntity.setPnumber(entity.getPnumber());
				
				newEntity.setDirector(entity.getDirector());
				newEntity.setDirectorId(entity.getDirectorId());
								
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
	
	public void deletePortfolioById(Long id) throws RecordNotFoundException
	{
		Optional<PortfoliosEntity> portfolio=repository.findById(id);
		
		if(portfolio.isPresent())
			repository.deleteById(id);
		else
			throw new RecordNotFoundException("No Portfolio record exist for given id");
		
		
	}
	
	public String getPortfolioByNumber(String pnumber)
	{
		String portfolio=repository.getPortfolioName(pnumber);
		
		if(portfolio==null)
			return "";
		else
			return portfolio;
		
	}
	
	public int findDuplicates(String pnumber)
	{
		int priznakDuplicate=0;
		
		priznakDuplicate=repository.findPortfolioDuplicity(pnumber);
		
		
		return priznakDuplicate;
	}
}
