package com.neueda.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpService {

    @Autowired
    private EmpRepo empRepo;

    public List<Employee> getAllEmployees() {
        return empRepo.getAllEmployees();
    }
//
    public Employee getEmployeeId(int id){
        return empRepo.getEmployeeId(id);
    }
//
    public int addEmployee(Employee emp){
        return empRepo.addEmployee(emp);
    }
//
    public Employee updateEmployeeById(int id, Employee emp){
        return empRepo.updateEmployeeById(id,emp);
    }
//
    public int deleteEmployeeById(int id){
        return empRepo.deleteEmployeeById(id);
    }
}
