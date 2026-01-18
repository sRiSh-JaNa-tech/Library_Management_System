package com.LibManSys.LibManSys.controller;

import com.LibManSys.LibManSys.Repository.BookRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/library")
public class BookController {

    private final BookRepository repo;

    public BookController(BookRepository repo) {
        this.repo = repo;
    }

    @PostMapping(value = "/books_new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addBook(
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam(required = false) String isbn,
            @RequestParam String category,
            @RequestParam(required = false) String year,
            @RequestParam Integer quantity,
            @RequestParam(required = false) MultipartFile cover
    ) {
        try {
            Integer pubYear = (year == null || year.isBlank()) ? null : Integer.parseInt(year);

            String fileName = null;
            if (cover != null && !cover.isEmpty()) {
                fileName = System.currentTimeMillis() + "_" + cover.getOriginalFilename();
                Path uploadPath = Paths.get("photos");
                Files.createDirectories(uploadPath);
                Files.copy(cover.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            }

            repo.insertBook(title, author, isbn, category, pubYear, quantity, fileName);

            return ResponseEntity.ok(Map.of("message", "Book added successfully"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
