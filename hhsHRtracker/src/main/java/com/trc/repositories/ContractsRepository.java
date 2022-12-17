package com.trc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trc.entities.ContractsEntity;

@Repository
public interface ContractsRepository extends CrudRepository<ContractsEntity,Long> 
{
	@Query("Select u from ContractsEntity u Order by u.contract")
	List<ContractsEntity>  getAll();
		
	@Query("Select count(u) from ContractsEntity u where u.contract=?1")
	int findContractDuplicity(String contract);
	
	@Query("Select u.typeProject from ContractsEntity u where u.contract=?1")
	String getTypeContract(String contract);
		
}
