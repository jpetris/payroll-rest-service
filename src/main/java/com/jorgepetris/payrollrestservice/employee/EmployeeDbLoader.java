package com.jorgepetris.payrollrestservice.employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmployeeDbLoader {

    private static final Logger log = LoggerFactory.getLogger(EmployeeDbLoader.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository) {
        return args -> {
            log.info("Preloading " + employeeRepository.save(new Employee("Bilbo Baggins", "burglar")));
            log.info("Preloading " + employeeRepository.save(new Employee("Frodo Baggins", "thief")));
        };
    }
    
}