package com.LibManSys.LibManSys.controller;

import com.LibManSys.LibManSys.Repository.PendingPaymentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.sql.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PendingPaymentController {

    private final PendingPaymentRepository repo;

    public PendingPaymentController(PendingPaymentRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Map<String, Object>>> getPending() {
        return ResponseEntity.ok(repo.fetchPending());
    }

    @PostMapping("/pay/member/{memberId}")
    public ResponseEntity<?> markPaid(@PathVariable int memberId) {
        repo.markPaidForMember(memberId);
        return ResponseEntity.ok().build();
    }
}

