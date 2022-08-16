package com.trc.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trc.entities.UsersEntity;

@Repository
public interface UsersRepository  extends CrudRepository<UsersEntity,Long>
{
	@Query("Select u from UsersEntity u where u.email=?1")
	UsersEntity getUserByEmail(String email);
	
	@Query("Select u from UsersEntity u Order by email")
	List<UsersEntity>  getAllByUsername();
	
	@Modifying
	@Transactional
	@Query("Update UsersEntity u set u.password=?2 where u.userid=?1")
	void changeUserPass(Long id,String pass);
	
	@Query("Select count(u) from UsersEntity u where u.email=?1")
	int findEmailDuplicity(String email);
	
	@Query("Select u.password From UsersEntity u Where u.email=?1")
    String findPassByEmail(String email);
}
