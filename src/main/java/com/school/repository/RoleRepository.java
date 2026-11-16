package com.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	

}
