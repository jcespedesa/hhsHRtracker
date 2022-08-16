package com.trc.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trc.entities.LogsEntity;

@Repository
public interface LogsRepository extends CrudRepository<LogsEntity,Long>  
{

}
