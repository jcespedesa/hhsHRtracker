package com.trc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trc.entities.DivisionsEntity;

@Repository
public interface DivisionsRepository extends CrudRepository<DivisionsEntity,Long> 
{
	@Query("Select u.dname from DivisionsEntity u where u.dnumber=?1")
	String getDivisionName(@Param("dnumber") String dnumber);
	
	@Query("Select u from DivisionsEntity u Order by dname")
	List<DivisionsEntity>  getAllByName();
	
	@Query("Select u from DivisionsEntity u Where u.priznakApp='Yes' Order by dname")
	List<DivisionsEntity>  getByPriznakApp();
}
