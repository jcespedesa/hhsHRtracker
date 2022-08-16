package com.trc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trc.entities.HhsDivisionsEntity;

@Repository
public interface HhsDivisionsRepository extends CrudRepository<HhsDivisionsEntity,Long> 
{
	@Query("Select u.division from HhsDivisionsEntity u where u.divisionNumber=?1")
	String getDivisionName(String dnumber);
	
	@Query("Select u from HhsDivisionsEntity u Order by division")
	List<HhsDivisionsEntity>  getAllByName();
}
