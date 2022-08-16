package com.trc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trc.entities.ClientsEntity;


@Repository
public interface ClientsRepository extends CrudRepository<ClientsEntity,Long>  
{
	@Query("Select u from ClientsEntity u where u.project=?1 Order by u.cname")
	List<ClientsEntity>  getAllByProject(String project);
	
		
}
