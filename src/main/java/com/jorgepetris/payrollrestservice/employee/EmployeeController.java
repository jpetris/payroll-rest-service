package com.jorgepetris.payrollrestservice.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    
    private final EmployeeRepository repository;

    @Autowired
    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    // tag::get-aggregate-root[]
    @GetMapping("/employees")
    public List<Employee> all() {
        return repository.findAll();
    }
    //end::get-aggregate-root[]

    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        return repository.save(newEmployee);
    }

    @GetMapping("/employees/{id}")
    Employee one(@PathVariable Long id) {
        return repository.findById(id)
        .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @PutMapping("/employees/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        return repository.findById(id)
        .map(employee -> {
            employee.setName(newEmployee.getName());
            employee.setRole(newEmployee.getRole());
            return repository.save(employee);
        })
        .orElseGet(() -> {
            newEmployee.setId(id);
            return repository.save(newEmployee);
        });
    }
    
    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }
    

    class EmployeeNotFoundException extends RuntimeException {
        
        EmployeeNotFoundException(Long id) {
            super("Could not find employee " + id);
        }

    }

    // When an EmployeeNotFoundException is thrown, this piece of Spring MVC configuration is used to render an HTTP 404.
    @ControllerAdvice
    class EmployeeNotFoundAdvice {
        
        @ResponseBody // Signals that this advice is rendered straight into the response body
        @ExceptionHandler(EmployeeNotFoundException.class) // configures the advice to only respond if an EmployeeNotFoundException is thrown
        @ResponseStatus(HttpStatus.NOT_FOUND) // syas to issue an HttpStatus.NOT_FOUND i.e. an HTTP 404
        String employeeNotFoundHandler(EmployeeNotFoundException e) {
            return e.getMessage();
        }

    }
}
