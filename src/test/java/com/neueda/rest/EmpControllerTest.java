package com.neueda.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmpController.class)
public class EmpControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    EmpService empService;

    @Test
    void shouldReturnAllEmployee() throws Exception {
        List<Employee> list= List.of(new Employee("John", "Doe", 30));
        when(empService.getAllEmployees()).thenReturn(list);

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("John"));
    }

    @Test
    void shouldReturnEmptyListWhenNoEmployeesExist() throws Exception {
        when(empService.getAllEmployees()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void shouldReturnEmployeeWhenIdExists() throws Exception {
        Employee emp = new Employee("Jane", "HR", 50000);
        emp.setId(1);
        when(empService.getEmployeeId(1)).thenReturn(emp);

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Jane"))
                .andExpect(jsonPath("$.data.department").value("HR"));
    }

    @Test
    void shouldReturn201WhenEmployeeIsAddedSuccessfully() throws Exception {
        when(empService.addEmployee(any(Employee.class))).thenReturn(1);

        mockMvc.perform(post("/employees/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"department\":\"IT\",\"salary\":60000}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Employee added successfully"));
    }

    @Test
    void shouldReturn400WhenEmployeeIsNotAdded() throws Exception {
        when(empService.addEmployee(any(Employee.class))).thenReturn(0);

        mockMvc.perform(post("/employees/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"department\":\"IT\",\"salary\":60000}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Employee not added"));
    }

    @Test
    void shouldReturn200WhenEmployeeIsUpdatedSuccessfully() throws Exception {
        Employee updated = new Employee("John", "Finance", 80000);
        when(empService.updateEmployeeById(eq(1), any(Employee.class))).thenReturn(updated);

        mockMvc.perform(put("/employees/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"department\":\"Finance\",\"salary\":80000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Employee updated successfully"))
                .andExpect(jsonPath("$.data.department").value("Finance"));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistentEmployee() throws Exception {
        when(empService.updateEmployeeById(eq(999), any(Employee.class))).thenReturn(null);

        mockMvc.perform(put("/employees/update/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Ghost\",\"department\":\"None\",\"salary\":0}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found"));
    }

    @Test
    void shouldReturn200WhenEmployeeIsDeletedSuccessfully() throws Exception {
        when(empService.deleteEmployeeById(1)).thenReturn(1);

        mockMvc.perform(delete("/employees/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Employee deleted successfully"));
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentEmployee() throws Exception {
        when(empService.deleteEmployeeById(999)).thenReturn(0);

        mockMvc.perform(delete("/employees/delete/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found"));
    }
}
