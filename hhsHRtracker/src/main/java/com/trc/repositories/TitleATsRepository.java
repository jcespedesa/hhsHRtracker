package com.trc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trc.entities.TitleATsEntity;

@Repository
public interface TitleATsRepository extends CrudRepository<TitleATsEntity,Long> 
{
	@Query("Select u from TitleATsEntity u Where u.titleNum=?1")
	List<TitleATsEntity>  getAssignated(String contract);
		
	@Query("Select count(u) from TitleATsEntity u where (u.trainingNum=?1 and u.titleNum=?2)")
	int findAssignationDuplicity(String trainingNum,String titleNum);

	@Query("Select u from TitleATsEntity u Where u.titleNum=?1")
	List<TitleATsEntity>  getATsByTitle (String titleNum);
	
}
