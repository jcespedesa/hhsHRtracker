package com.trc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trc.entities.CertificatesEntity;


@Repository
public interface CertificatesRepository extends CrudRepository<CertificatesEntity,Long>  
{
		
	@Query("Select u from CertificatesEntity u Where u.active='Yes' Order by u.certName")
	List<CertificatesEntity>  getActives();
	
	@Query("Select u from CertificatesEntity u Order by u.certName")
	List<CertificatesEntity>  getCerts();
	
		
	@Query("Select count(u) from CertificatesEntity u where u.certNumber=?1")
	int findCertDuplicity(String certNumber);
	
	@Query("Select u.certName from CertificatesEntity u where u.certNumber=?1")
	String  getCertName(String certNumber);
}
