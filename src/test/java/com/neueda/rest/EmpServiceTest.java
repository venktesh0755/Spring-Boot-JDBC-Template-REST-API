package com.neueda.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmpServiceTest {
    //for service layer testing we can use mockito to mock the repository layer and test the service layer independently.
    //using mockito we can create mock objects of the repository layer and define their behavior for testing the service layer methods.

    @InjectMocks
    EmpService service;

    @Mock
    EmpRepo repo;

    @Test
    void shouldReturnAllEmployees(){
        //mock data

        List<Employee>list=List.of(
                new Employee("John","Doe",50000),
                new Employee("Jane","Smith",60000),
                new Employee("Patrick","Jane",70000)

        );

        when(repo.getAllEmployees()).thenReturn(list);
        List<Employee> result=service.getAllEmployees();
        assertEquals(3,result.size());
        verify(repo).getAllEmployees();;

    }

    @Test
    void shouldReturnEmptyListWhenNoEmployeesExist(){
        when(repo.getAllEmployees()).thenReturn(Collections.emptyList());

        List<Employee> result=service.getAllEmployees();

        assertEquals(0,result.size());
        verify(repo).getAllEmployees();
    }

    @Test
    void shouldReturnEmployeeWhenIdExists(){
        Employee emp=new Employee("John","IT",50000);
        emp.setId(1);
        when(repo.getEmployeeId(1)).thenReturn(emp);

        Employee result=service.getEmployeeId(1);

        assertEquals("John",result.getName());
        assertEquals(1,result.getId());
        verify(repo).getEmployeeId(1);
    }

    @Test
    void shouldThrowExceptionWhenEmployeeIdDoesNotExist(){
        when(repo.getEmployeeId(999)).thenThrow(new EmployeeNotFoundException("Employee with id : 999 not found"));

        assertThrows(EmployeeNotFoundException.class,()->service.getEmployeeId(999));
        verify(repo).getEmployeeId(999);
    }

    @Test
    void shouldReturnOneWhenEmployeeIsAddedSuccessfully(){
        Employee emp=new Employee("Jane","HR",60000);
        when(repo.addEmployee(emp)).thenReturn(1);

        int result=service.addEmployee(emp);

        assertEquals(1,result);
        verify(repo).addEmployee(emp);
    }

    @Test
    void shouldReturnZeroWhenEmployeeAddFails(){
        Employee emp=new Employee("Jane","HR",60000);
        when(repo.addEmployee(emp)).thenReturn(0);

        int result=service.addEmployee(emp);

        assertEquals(0,result);
        verify(repo).addEmployee(emp);
    }

    @Test
    void shouldReturnUpdatedEmployeeWhenUpdateSucceeds(){
        Employee emp=new Employee("John","Finance",80000);
        when(repo.updateEmployeeById(1,emp)).thenReturn(emp);

        Employee result=service.updateEmployeeById(1,emp);

        assertNotNull(result);
        assertEquals("Finance",result.getDepartment());
        verify(repo).updateEmployeeById(1,emp);
    }

    @Test
    void shouldReturnNullWhenUpdatingNonExistentEmployee(){
        Employee emp=new Employee("Ghost","None",0);
        when(repo.updateEmployeeById(999,emp)).thenReturn(null);

        Employee result=service.updateEmployeeById(999,emp);

        assertNull(result);
        verify(repo).updateEmployeeById(999,emp);
    }

    @Test
    void shouldReturnOneWhenDeleteSucceeds(){
        when(repo.deleteEmployeeById(1)).thenReturn(1);

        int result=service.deleteEmployeeById(1);

        assertEquals(1,result);
        verify(repo).deleteEmployeeById(1);
    }

    @Test
    void shouldReturnZeroWhenDeletingNonExistentEmployee(){
        when(repo.deleteEmployeeById(999)).thenReturn(0);

        int result=service.deleteEmployeeById(999);

        assertEquals(0,result);
        verify(repo).deleteEmployeeById(999);
    }
}
