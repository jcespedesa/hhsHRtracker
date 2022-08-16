package com.trc.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trc.entities.AssigCertsEntity;


@Repository
public interface AssigCertsRepository  extends CrudRepository<AssigCertsEntity,Long>
{
	@Query("Select u from AssigCertsEntity u where (u.period=?1) Order by u.certNumber")
	List<AssigCertsEntity>  getAllCerts(String period);
	
	@Query("Select u from AssigCertsEntity u where (u.period=?1 and u.clientId=?2) Order by u.certNumber")
	List<AssigCertsEntity>  getAllCertsByClientId(String period,String clientId);
	
	@Query("Select COUNT(u) from AssigCertsEntity u where (u.certNumber=?1 and u.clientId=?2 and u.period=?3)")
	int  checkExistentCert(String certNumber,String clientId,String period);
	
	@Modifying
	@Transactional
	@Query("Update AssigCertsEntity u set u.expirationDate=?2,u.realExpirationDate=?2,u.notes=?3 where u.recordid=?1")
	void updateCert(Long id,String expirationDate,String notes);
		
	@Query("Select COUNT(u) from AssigCertsEntity u where (u.recordid=?1 and u.realExpirationDate <= ?2)")
	int  compareDateExp(Long certId,String todayDate);
	
	@Query("Select COUNT(u) from AssigCertsEntity u where (u.clientId=?1 and u.realExpirationDate <= ?2)")
	int  countExpiredRecordsById(String clientId,String todayDate);
		
}
