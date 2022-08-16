package com.trc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trc.entities.AssigProjectsEntity;


@Repository
public interface AssigProjectsRepository  extends CrudRepository<AssigProjectsEntity,Long>
{
	@Query("Select u from AssigProjectsEntity u where (u.department=?1) Order by u.projectName")
	List<AssigProjectsEntity>  getAllProjectsByDiv(String department);
	
	@Query("Select u from AssigProjectsEntity u where (u.department=?1 and u.userId=?2) Order by u.projectName")
	List<AssigProjectsEntity>  getAllProjectsByDivId(String department,String userId);
}
