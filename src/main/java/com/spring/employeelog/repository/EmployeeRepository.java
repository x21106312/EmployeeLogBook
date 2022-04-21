package com.spring.employeelog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.employeelog.model.EmployeeEntity;





public interface EmployeeRepository 
			extends JpaRepository<EmployeeEntity, Long> {

}
