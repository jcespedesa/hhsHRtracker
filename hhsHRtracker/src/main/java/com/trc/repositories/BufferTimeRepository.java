package com.trc.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trc.entities.BufferTimeEntity;

@Repository
public interface BufferTimeRepository extends CrudRepository<BufferTimeEntity,Long>
{
	
	@Query("Select u from BufferTimeEntity u where u.recordid=?1")
	BufferTimeEntity getByRecordId(Long recordId);

	@Modifying
	@Transactional
	@Query("Delete from BufferTimeEntity u where u.username=?1")
	void deleteByUsername(String username);
	
	@Modifying
	@Transactional
	@Query("update BufferTimeEntity u set u.strobe=?1 where u.username=?1")
	void updateByUsername(String strobe, String username);
	
	@Query("Select u.recordid from BufferTimeEntity u where u.username=?1")
	Long getRecordId(String username);
	
	@Query("Select u from BufferTimeEntity u where u.username=?1")
	BufferTimeEntity getByUsername(String username);
	
	@Query("Select u from BufferTimeEntity u where u.username=?1")
	List<BufferTimeEntity>  getAllByUsername(String username);
	
}
