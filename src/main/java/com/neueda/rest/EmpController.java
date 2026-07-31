package com.neueda.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/api/v1/employees")
public class EmpController {
    @Autowired
    private EmpService empService;


    //generate exception handling for path not found


    @GetMapping("/employees")
    public ResponseEntity<Map<String,Object>> getAllEmployees(){
        List<Employee> employees= empService.getAllEmployees();
        Map<String,Object> response= new HashMap<>();
        response.put("message","Employees fetched successfully");
        response.put("data",employees);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/employees/add")
    public ResponseEntity<Map<String,Object>> addEmployee(@RequestBody Employee emp){
        int result= empService.addEmployee(emp);
        Map<String,Object> response= new HashMap<>();
        if(result>0){
            response.put("message","Employee added successfully");
            response.put("data",emp);
            return ResponseEntity.status(201).body(response);
        }else{
            response.put("message","Employee not added");
            return ResponseEntity.status(400).body(response);
        }
    }
    @GetMapping("/employees/{id}")
    public ResponseEntity<Map<String, Object>> getEmployeeId(@PathVariable int id){
        Employee employees= empService.getEmployeeId(id);
        Map<String,Object> response= new HashMap<>();
        response.put("message","Employee with id : "+id +"fetched successfully");
        response.put("data",employees);
        return ResponseEntity.status(200).body(response);
    }


    @PutMapping("/employees/update/{id}")
    public ResponseEntity<Map<String,Object>> updateEmployeeById(@PathVariable int id, @RequestBody Employee emp){
        Map<String,Object> response= new HashMap<>();
        Employee employee= empService.updateEmployeeById(id,emp);
        if(employee!=null){
            response.put("message","Employee updated successfully");
            response.put("data",employee);
            return ResponseEntity.status(200).body(response);
        }else{
            response.put("message","Employee not found");
            return ResponseEntity.status(404).body(response);
        }
    }
    @DeleteMapping("/employees/delete/{id}")
    public ResponseEntity<Map<String,Object>> deleteEmployeeById(@PathVariable int id){
        Map<String,Object> response= new HashMap<>();
        int result= empService.deleteEmployeeById(id);
        if(result>0){
            response.put("message","Employee deleted successfully");
            return ResponseEntity.status(200).body(response);
        }else {
            response.put("message", "Employee not found");
            return ResponseEntity.status(404).body(response);
        }

    }


}
