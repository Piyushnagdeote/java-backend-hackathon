package com.company.platform.contact;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;   

@RestController
@RequestMapping("/api/v1/contact")
public class ContactController {

    @PostMapping
    public ResponseEntity<?> submitContact(@RequestBody ContactRequest req) {

        System.out.println("CONTACT NAME = " + req.getName());
        System.out.println("CONTACT EMAIL = " + req.getEmail());
        System.out.println("CONTACT MESSAGE = " + req.getMessage());

        return ResponseEntity.ok(
                Map.of("message", "Message received successfully")
        );
    }
}
