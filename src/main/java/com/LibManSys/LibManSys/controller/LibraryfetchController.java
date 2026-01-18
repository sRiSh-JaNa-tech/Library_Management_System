package com.LibManSys.LibManSys.controller;

import com.LibManSys.LibManSys.Repository.CurrentIssuedBooks;
import com.LibManSys.LibManSys.Repository.OverdueBooksRepository;
import com.LibManSys.LibManSys.Repository.fetchBasicData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.Map;

@RestController
@RequestMapping("/api/library")
@CrossOrigin(origins = "*") // Allows frontend access if hosted separately (optional)
public class LibraryfetchController {

    private final fetchBasicData fetchBasicData;
    private final CurrentIssuedBooks currentIssuedBooks;
    private final OverdueBooksRepository overdueRepo;

    public LibraryfetchController(fetchBasicData fetchBasicData,
                                  CurrentIssuedBooks currentIssuedBooks,
                                  OverdueBooksRepository overdueRepo) {
        this.fetchBasicData = fetchBasicData;
        this.currentIssuedBooks = currentIssuedBooks;
        this.overdueRepo = overdueRepo;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getLibraryStats() {
        Map<String, Object> stats = fetchBasicData.fetchSummary();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/issued/today")
    public ResponseEntity<Map<String, Object>> getIssuedToday() {
        Map<String, Object> result = currentIssuedBooks.fetchIssuedToday();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/overdue")
    public ResponseEntity<Map<String, Object>> getOverdueBooks() {

        List<Map<String, Object>> books = overdueRepo.fetchOverdueBooks();

        return ResponseEntity.ok(Map.of(
                "count", books.size(),
                "books", books
        ));
    }
}
