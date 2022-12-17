package com.trc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trc.entities.TrainingsEntity;

@Repository
public interface TrainingsRepository extends CrudRepository<TrainingsEntity,Long> 
{
	@Query("Select u from TrainingsEntity u Order by u.tname")
	List<TrainingsEntity>  getAllByName();
	
	@Query("Select u from TrainingsEntity u Where u.active='Yes' Order by u.tname")
	List<TrainingsEntity>  getActives();
		
	@Query("Select count(u) from TrainingsEntity u where u.tname=?1")
	int findTrainingDuplicity(String tname);
	
	@Query("Select u.tname from TrainingsEntity u where u.tnumber=?1")
	String  getTname(String tnumber);
	
	@Query("Select u.frame from TrainingsEntity u where u.tnumber=?1")
	String  getTframe(String tframe);
	
	@Query("Select u.tnumber from TrainingsEntity u Order by u.tnumber")
	List<Integer>  getOccupied();
}
