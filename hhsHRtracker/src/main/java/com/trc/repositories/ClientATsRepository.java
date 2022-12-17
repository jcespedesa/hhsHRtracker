package com.trc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trc.entities.ClientATsEntity;

@Repository
public interface ClientATsRepository extends CrudRepository<ClientATsEntity,Long>  
{
	@Query("Select u from ClientATsEntity u Where u.clientId=?1 Order by u.trainingNum")
	List<ClientATsEntity>  getAssignated(String clientId);
		
	@Query("Select count(u) from ClientATsEntity u where (u.trainingNum=?1 and u.clientId=?2)")
	int findAssignationDuplicity(String projectNum,String clientId);
}
