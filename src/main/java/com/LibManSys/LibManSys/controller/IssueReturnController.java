package com.LibManSys.LibManSys.controller;

import com.LibManSys.LibManSys.Repository.IssueReturnRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/library/issuance")
public class IssueReturnController {

    private final IssueReturnRepository repo;

    public IssueReturnController(IssueReturnRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Map<String, Object>> getAll() {
        return repo.fetchAll();
    }

    @PutMapping("/{id}/return")
    public void returnBook(@PathVariable int id) {
        repo.markReturned(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        repo.deleteIssuance(id);
    }
}

