package com.example.cms.service;

import com.example.cms.dto.DocumentDTO;
import com.example.cms.entity.Document;
import com.example.cms.entity.Priority;
import com.example.cms.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public List<DocumentDTO> getAllDocuments() {
        return documentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<DocumentDTO> getDocumentById(Long id) {
        return documentRepository.findById(id)
                .map(this::convertToDTO);
    }

    public DocumentDTO createDocument(DocumentDTO documentDTO) {
        Document document = convertToEntity(documentDTO);
        
        // Generate document ID
        String documentId = generateDocumentId();
        document.setDocumentId(documentId);
        
        Document savedDocument = documentRepository.save(document);
        return convertToDTO(savedDocument);
    }

    public Optional<DocumentDTO> updateDocument(Long id, DocumentDTO documentDTO) {
        return documentRepository.findById(id)
                .map(document -> {
                    document.setTitle(documentDTO.getTitle());
                    document.setErrorType(documentDTO.getErrorType());
                    document.setContent(documentDTO.getContent());
                    document.setPriority(Priority.fromValue(documentDTO.getPriority()));
                    document.setViewPermissions(documentDTO.getViewPermissions());
                    document.setAttachments(documentDTO.getAttachments());
                    return convertToDTO(documentRepository.save(document));
                });
    }

    public boolean deleteDocument(Long id) {
        if (documentRepository.existsById(id)) {
            documentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<DocumentDTO> searchDocuments(String keyword) {
        return documentRepository.findByKeyword(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DocumentDTO> getDocumentsByErrorType(String errorType) {
        return documentRepository.findByErrorType(errorType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DocumentDTO> getDocumentsByPriority(String priority) {
        Priority documentPriority = Priority.fromValue(priority);
        return documentRepository.findByPriority(documentPriority).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private String generateDocumentId() {
        long count = documentRepository.count() + 1;
        return String.format("DOC%03d", count);
    }

    private DocumentDTO convertToDTO(Document document) {
        DocumentDTO dto = new DocumentDTO();
        dto.setId(document.getId());
        dto.setDocumentId(document.getDocumentId());
        dto.setTitle(document.getTitle());
        dto.setErrorType(document.getErrorType());
        dto.setContent(document.getContent());
        dto.setPriority(document.getPriority().getValue());
        dto.setCreatedBy(document.getCreatedBy());
        dto.setCreatedAt(document.getCreatedAt());
        dto.setUpdatedAt(document.getUpdatedAt());
        dto.setAttachments(document.getAttachments());
        dto.setViewPermissions(document.getViewPermissions());
        return dto;
    }

    private Document convertToEntity(DocumentDTO dto) {
        Document document = new Document();
        document.setTitle(dto.getTitle());
        document.setErrorType(dto.getErrorType());
        document.setContent(dto.getContent());
        document.setPriority(Priority.fromValue(dto.getPriority()));
        document.setCreatedBy(dto.getCreatedBy());
        document.setAttachments(dto.getAttachments());
        document.setViewPermissions(dto.getViewPermissions());
        return document;
    }
}