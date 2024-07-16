package com.justdeax.WSpringDocs.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
interface EmployeeRepository extends JpaRepository<Employee, Long> { }