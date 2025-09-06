package com.knowledgehub.controller;

import com.knowledgehub.dto.DocumentRequest;
import com.knowledgehub.dto.DocumentResponse;
import com.knowledgehub.entity.Comment;
import com.knowledgehub.entity.Document;
import com.knowledgehub.entity.Rating;
import com.knowledgehub.entity.User;
import com.knowledgehub.repository.CommentRepository;
import com.knowledgehub.repository.DocumentRepository;
import com.knowledgehub.repository.RatingRepository;
import com.knowledgehub.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping
    public ResponseEntity<Page<DocumentResponse>> getAllDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String privacy,
            Authentication authentication) {

        if ("newest".equals(sortBy) || "oldest".equals(sortBy)) {
            sortBy = "createdAt";
        }

        if ("newest".equals(sortBy)) {
            sortDir = "desc";
        } else if ("oldest".equals(sortBy)) {
            sortDir = "asc";
        }

        User currentUser = (User) authentication.getPrincipal();
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Document> documents = documentService.getVisibleDocuments(
            currentUser, search, privacy, pageable);
        
        Page<DocumentResponse> response = documents.map(DocumentResponse::new);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getDocument(@PathVariable Long id, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        
        Optional<Document> documentOpt = documentRepository.findById(id);
        if (documentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Document document = documentOpt.get();
        
        // Check access permission
        if (!documentService.canUserAccessDocument(currentUser, document)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // Increment view count
        document.incrementViews();
        documentRepository.save(document);
        
        return ResponseEntity.ok(new DocumentResponse(document));
    }

    @PostMapping
    public ResponseEntity<DocumentResponse> createDocument(
            @Valid @RequestBody DocumentRequest request, 
            Authentication authentication) {
        
        User currentUser = (User) authentication.getPrincipal();
        Document document = documentService.createDocument(request, currentUser);
        
        return ResponseEntity.ok(new DocumentResponse(document));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@documentService.isDocumentOwner(#id, authentication.principal.id)")
    public ResponseEntity<DocumentResponse> updateDocument(
            @PathVariable Long id,
            @Valid @RequestBody DocumentRequest request,
            Authentication authentication) {
        
        User currentUser = (User) authentication.getPrincipal();
        Document document = documentService.updateDocument(id, request, currentUser);
        
        if (document == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(new DocumentResponse(document));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@documentService.isDocumentOwner(#id, authentication.principal.id)")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<?> rateDocument(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request,
            Authentication authentication) {
        
        User currentUser = (User) authentication.getPrincipal();
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        
        if (!documentService.canUserAccessDocument(currentUser, document)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Integer ratingValue = request.get("rating");
        if (ratingValue == null || ratingValue < 1 || ratingValue > 5) {
            return ResponseEntity.badRequest().body("Rating must be between 1 and 5");
        }
        
        // Check if user already rated this document
        Optional<Rating> existingRating = ratingRepository.findByUserAndDocument(currentUser, document);
        
        if (existingRating.isPresent()) {
            // Update existing rating
            Rating rating = existingRating.get();
            rating.setRating(ratingValue);
            ratingRepository.save(rating);
        } else {
            // Create new rating
            Rating rating = new Rating(currentUser, document, ratingValue);
            ratingRepository.save(rating);
        }
        
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            Authentication authentication) {
        
        User currentUser = (User) authentication.getPrincipal();
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        
        if (!documentService.canUserAccessDocument(currentUser, document)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        String content = request.get("content");
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Comment content cannot be empty");
        }
        
        Comment comment = new Comment(currentUser, document, content);
        commentRepository.save(comment);
        
        return ResponseEntity.ok().build();
    }
}