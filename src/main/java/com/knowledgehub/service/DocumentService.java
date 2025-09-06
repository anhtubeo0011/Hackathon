package com.knowledgehub.service;

import com.knowledgehub.dto.DocumentRequest;
import com.knowledgehub.entity.Document;
import com.knowledgehub.entity.User;
import com.knowledgehub.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Page<Document> getVisibleDocuments(User user, String search, String privacy, Pageable pageable) {
        if (StringUtils.hasText(search)) {
            return documentRepository.findVisibleDocumentsWithSearch(
                user.getId(), user.getGroupName(), search, pageable);
        } else {
            return documentRepository.findVisibleDocuments(
                user.getId(), user.getGroupName(), pageable);
        }
    }

    public boolean canUserAccessDocument(User user, Document document) {
        switch (document.getPrivacy()) {
            case PUBLIC:
                return true;
            case GROUP:
                return document.getAuthor().getGroupName().equals(user.getGroupName());
            case PRIVATE:
                return document.getAuthor().getId().equals(user.getId());
            default:
                return false;
        }
    }

    public boolean isDocumentOwner(Long documentId, Long userId) {
        Optional<Document> document = documentRepository.findById(documentId);
        return document.isPresent() && document.get().getAuthor().getId().equals(userId);
    }

    public Document createDocument(DocumentRequest request, User author) {
        Document document = new Document();
        document.setTitle(request.getTitle());
        document.setContent(request.getContent());
        document.setSummary(request.getSummary());
        document.setTags(request.getTags());
        document.setPrivacy(request.getPrivacy());
        document.setFileUrl(request.getFileUrl());
        document.setFileType(request.getFileType());
        document.setFileSize(request.getFileSize());
        document.setAuthor(author);

        return documentRepository.save(document);
    }

    public Document updateDocument(Long id, DocumentRequest request, User currentUser) {
        Optional<Document> documentOpt = documentRepository.findById(id);
        if (documentOpt.isEmpty()) {
            return null;
        }

        Document document = documentOpt.get();
        
        // Check if user is the owner
        if (!document.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Access denied");
        }

        document.setTitle(request.getTitle());
        document.setContent(request.getContent());
        document.setSummary(request.getSummary());
        document.setTags(request.getTags());
        document.setPrivacy(request.getPrivacy());
        
        if (request.getFileUrl() != null) {
            document.setFileUrl(request.getFileUrl());
            document.setFileType(request.getFileType());
            document.setFileSize(request.getFileSize());
        }

        return documentRepository.save(document);
    }

    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }
}