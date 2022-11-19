package com.trc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trc.entities.ContractATsEntity;

@Repository
public interface ContractATsRepository extends CrudRepository<ContractATsEntity,Long> 
{
	@Query("Select u from ContractATsEntity u Where u.contract=?1 Order by u.contract")
	List<ContractATsEntity>  getAssignated(String contract);
		
	@Query("Select count(u) from ContractATsEntity u where (u.trainingNum=?1 and u.contract=?2)")
	int findAssignationDuplicity(String projectNum,String contract);
}
