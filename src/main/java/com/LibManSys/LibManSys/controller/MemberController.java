package com.LibManSys.LibManSys.controller;

import com.LibManSys.LibManSys.Repository.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "*")
public class MemberController {

    private final MemberRepository repo;

    public MemberController(MemberRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> fetchAll() {
        return ResponseEntity.ok(repo.fetchAllMembers());
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Map<String, Object> payload) {
        repo.addMember(payload);
        return ResponseEntity.ok().build();
    }
}

