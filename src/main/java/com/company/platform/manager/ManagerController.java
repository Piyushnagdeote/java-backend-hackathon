package com.company.platform.manager;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/manager")
public class ManagerController {

    @GetMapping("/dashboard")
    public String managerDashboard() {
        return "MANAGER ACCESS GRANTED";
    }
}
