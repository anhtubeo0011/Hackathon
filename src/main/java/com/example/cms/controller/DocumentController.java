package com.example.cms.controller;

import com.example.cms.dto.DocumentDTO;
import com.example.cms.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "http://localhost:5173")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        List<DocumentDTO> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getDocumentById(@PathVariable Long id) {
        return documentService.getDocumentById(id)
                .map(document -> ResponseEntity.ok(document))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DocumentDTO> createDocument(@Valid @RequestBody DocumentDTO documentDTO) {
        DocumentDTO createdDocument = documentService.createDocument(documentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDocument);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentDTO> updateDocument(@PathVariable Long id, @Valid @RequestBody DocumentDTO documentDTO) {
        return documentService.updateDocument(id, documentDTO)
                .map(document -> ResponseEntity.ok(document))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        if (documentService.deleteDocument(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<DocumentDTO>> searchDocuments(@RequestParam String keyword) {
        List<DocumentDTO> documents = documentService.searchDocuments(keyword);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/error-type/{errorType}")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByErrorType(@PathVariable String errorType) {
        List<DocumentDTO> documents = documentService.getDocumentsByErrorType(errorType);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByPriority(@PathVariable String priority) {
        List<DocumentDTO> documents = documentService.getDocumentsByPriority(priority);
        return ResponseEntity.ok(documents);
    }
}