package com.neueda.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class EmpRepoTest {

    @InjectMocks
    EmpRepo repo;

    @Mock
    JdbcTemplate jdbcTemplate;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee("John", "IT", 75000);
        employee.setId(1);
    }

    @Test
    void shouldReturnAllEmployees() {
        List<Employee> employees = List.of(employee);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(employees);

        List<Employee> result = repo.getAllEmployees();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
    }

    @Test
    void shouldReturnEmployeeWhenEmployeeIdExists() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyInt())).thenReturn(employee);

        Employee result = repo.getEmployeeId(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void shouldThrowEmployeeNotFoundExceptionWhenEmployeeIdDoesNotExist() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyInt()))
                .thenThrow(new EmptyResultDataAccessException(1));

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> repo.getEmployeeId(999));

        assertTrue(exception.getMessage().contains("999"));
    }

    @Test
    void shouldReturnOneWhenEmployeeIsAddedSuccessfully() {
        when(jdbcTemplate.update(anyString(), any(), any(), any())).thenReturn(1);

        int result = repo.addEmployee(employee);

        assertEquals(1, result);
    }

    @Test
    void shouldReturnZeroWhenEmployeeAddAffectsNoRows() {
        when(jdbcTemplate.update(anyString(), any(), any(), any())).thenReturn(0);

        int result = repo.addEmployee(employee);

        assertEquals(0, result);
    }

    @Test
    void shouldReturnUpdatedEmployeeWhenUpdateSucceeds() {
        when(jdbcTemplate.update(anyString(), any(), any(), any(), anyInt())).thenReturn(1);

        Employee result = repo.updateEmployeeById(1, employee);

        assertNotNull(result);
        assertEquals("John", result.getName());
    }

    @Test
    void shouldReturnNullWhenUpdateAffectsNoRows() {
        when(jdbcTemplate.update(anyString(), any(), any(), any(), anyInt())).thenReturn(0);

        Employee result = repo.updateEmployeeById(999, employee);

        assertNull(result);
    }

    @Test
    void shouldReturnOneWhenDeleteSucceeds() {
        when(jdbcTemplate.update(anyString(), anyInt())).thenReturn(1);

        int result = repo.deleteEmployeeById(1);

        assertEquals(1, result);
    }

    @Test
    void shouldReturnZeroWhenDeleteAffectsNoRows() {
        when(jdbcTemplate.update(anyString(), anyInt())).thenReturn(0);

        int result = repo.deleteEmployeeById(999);

        assertEquals(0, result);
    }
}
