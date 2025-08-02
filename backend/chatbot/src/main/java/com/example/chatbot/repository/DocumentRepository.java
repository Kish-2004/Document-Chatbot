package com.example.chatbot.repository;


import com.example.chatbot.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByFileName(String fileName);
    Optional<Document> findTopByOrderByUploadDateDesc();
}