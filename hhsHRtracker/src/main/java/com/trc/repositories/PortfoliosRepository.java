package com.trc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trc.entities.PortfoliosEntity;

@Repository
public interface PortfoliosRepository extends CrudRepository<PortfoliosEntity,Long> 
{
	@Query("Select u.pname from PortfoliosEntity u where u.pnumber=?1")
	String getPortfolioName(String pnumber);
	
	@Query("Select u from PortfoliosEntity u Order by pname")
	List<PortfoliosEntity>  getAllByName();
	
	@Query("Select count(u) from PortfoliosEntity u where u.pnumber=?1")
	int findPortfolioDuplicity(String pnumber);
}
