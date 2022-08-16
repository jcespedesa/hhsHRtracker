package com.trc.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trc.entities.SettingsEntity;

@Repository
public interface SettingsRepository extends CrudRepository<SettingsEntity,Long> 
{
	@Query("Select u.path from SettingsEntity u where u.sname=?1")
	String getAppPath(@Param("sname") String sname);
	
	@Query("Select u from SettingsEntity u where u.sname=?1")
	SettingsEntity getBySname(String sname);
	
	@Query("Select u.path from SettingsEntity u where u.sname=?1")
	String getDefaultPass(String sname);
	
	@Query("Select count(u) from SettingsEntity u where u.sname=?1")
	int findSettingDuplicity(String setting);
	
	@Query("Select u.param1 from SettingsEntity u where u.sname=?1")
	String getEmailSender(String sname);
}

