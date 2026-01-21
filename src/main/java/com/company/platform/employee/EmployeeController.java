package com.company.platform.employee;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    @GetMapping("/dashboard")
    public String employeeDashboard() {
        return "EMPLOYEE ACCESS GRANTED";
    }
}
