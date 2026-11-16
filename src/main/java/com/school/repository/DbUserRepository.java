package com.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.model.DbUser;

@Repository
public interface DbUserRepository extends JpaRepository<DbUser, Long>{
	
	public DbUser findByUsername(String userName);

}
