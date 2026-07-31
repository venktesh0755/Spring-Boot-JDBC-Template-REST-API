package com.neueda.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Repository
public class EmpRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Employee> getAllEmployees() {
        String sql="SELECT * FROM employee";
        return jdbcTemplate.query(
          sql,new BeanPropertyRowMapper<>((Employee.class))
        );
    }

//    public List<Employee> getAllEmployees(){
//        return employees;
//    }

    public Employee getEmployeeId(int id){
        String sql="Select * from employee where id=?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Employee.class), id);
        }catch (EmptyResultDataAccessException e){
            throw new EmployeeNotFoundException("Employee with id : "+id+" not found");
        }
    }
//
    public int addEmployee(Employee emp){
        String sql="INSERT INTO employee (name,department,salary) VALUES (?,?,?)";
        int result=jdbcTemplate.update(sql,emp.getName(),emp.getDepartment(),emp.getSalary());
        if(result>0){

            return result;
        }
        return 0;
    }
//
    public Employee updateEmployeeById(int id, Employee emp){
        String sql="Update employee set name=?,department=?,salary=? where id=?";
        int result=jdbcTemplate.update(sql,emp.getName(),emp.getDepartment(),emp.getSalary(),id);
        if(result>0) {
            return emp;
        }
        return null;
    }

    public int deleteEmployeeById( int id){

        String sql="Delete from employee where id=?";
        int result=jdbcTemplate.update(sql,id);
        if(result>0){
            return result;
        }
        return 0;
    }

}
