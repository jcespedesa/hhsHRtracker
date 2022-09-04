package com.trc.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trc.entities.ClientsEntity;


@Repository
public interface ClientsRepository extends CrudRepository<ClientsEntity,Long>  
{
	@Query("Select u from ClientsEntity u where u.project=?1 Order by u.cname")
	List<ClientsEntity>  getAllByProject(String project);
	
	@Modifying
	@Transactional
	@Query("Update ClientsEntity u set u.project=?2,u.projectName=?3 where u.clientid=?1")
	void tranferEmployee(Long clientId,String projectNumber,String projectName);
		
}
