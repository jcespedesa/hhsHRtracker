package com.trc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trc.entities.ProjectTypesEntity;


@Repository
public interface ProjectTypesRepository extends CrudRepository<ProjectTypesEntity,Long> 
{
	@Query("Select u from ProjectTypesEntity u Order by u.type")
	List<ProjectTypesEntity>  getActives();
		
	@Query("Select count(u) from ProjectTypesEntity u where u.typeNumber=?1")
	int findTypesDuplicity(String typeNumber);
	
	@Query("Select u.type from ProjectTypesEntity u where u.typeNumber=?1")
	String  getType(String typeNumber);
}
