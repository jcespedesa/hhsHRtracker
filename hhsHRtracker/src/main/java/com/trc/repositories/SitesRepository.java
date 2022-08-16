package com.trc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trc.entities.SitesEntity;

@Repository
public interface SitesRepository extends CrudRepository<SitesEntity,Long> 
{
	@Query("Select u.sname from SitesEntity u where u.siteNumber=?1")
	String getLocationName(@Param("siteNumber") String siteNumber);
	
	@Query("Select u from SitesEntity u Order by sname")
	List<SitesEntity>  getAllByName();
	
	@Query("Select u from SitesEntity u Where division='300' Order by sname")
	List<SitesEntity>  findAllHHSsites();
	
	@Query("Select u from SitesEntity u WHERE (u.division=?1) Order by u.sname")
	List<SitesEntity>  searchByDivision(String stringSearch);
	
	
}
