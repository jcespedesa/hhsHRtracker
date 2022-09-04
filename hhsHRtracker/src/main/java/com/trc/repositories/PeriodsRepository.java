package com.trc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trc.entities.PeriodsEntity;


@Repository
public interface PeriodsRepository extends CrudRepository<PeriodsEntity,Long> 
{
	@Query("Select u from PeriodsEntity u Where u.active='Yes' Order by u.udelnyBes Desc")
	List<PeriodsEntity>  getActivesByUdelnyBes();
	
	@Query("Select u from PeriodsEntity u Order by u.udelnyBes")
	List<PeriodsEntity>  getPeriodsByUdelnyBes();
	
	@Query("Select u.udelnyBes from PeriodsEntity u Order by udelnyBes")
	List<Integer>  getOriginalListUB();
	
	@Query("Select count(u) from PeriodsEntity u where u.period=?1")
	int findPeriodDuplicity(String period);
}
