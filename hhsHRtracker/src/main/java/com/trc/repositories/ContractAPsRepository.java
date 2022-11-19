package com.trc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trc.entities.ContractAPsEntity;

@Repository
public interface ContractAPsRepository extends CrudRepository<ContractAPsEntity,Long> 
{
	@Query("Select u from ContractAPsEntity u Where u.contract=?1 Order by u.contract")
	List<ContractAPsEntity>  getAssignated(String contract);
		
	@Query("Select count(u) from ContractAPsEntity u where (u.projectNum=?1 and u.contract=?2)")
	int findAssignationDuplicity(String projectNum,String contract);
}
