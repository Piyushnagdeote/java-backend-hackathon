package com.company.platform.hr;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hr")
public class HrController {

    @GetMapping("/dashboard")
    public String hrDashboard() {
        return "HR ACCESS GRANTED";
    }
}
