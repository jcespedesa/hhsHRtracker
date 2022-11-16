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
	
	@Query("Select u from ClientsEntity u where u.active='Yes' Order by u.cname")
	List<ClientsEntity>  getAllActives();
	
	@Query("Select u.cname from ClientsEntity u where u.clientid=?1")
	String  getNameById(Long id);
	
	@Query("Select u from ClientsEntity u WHERE u.cname LIKE ('%' || ?1 || '%') Order by cname")
	List<ClientsEntity>  searchClientsByName(String stringSearch);
	
	@Query("Select u from ClientsEntity u WHERE u.clientid=?1")
	List<ClientsEntity>  searchClientsBySelection(Long stringSearch);
}
