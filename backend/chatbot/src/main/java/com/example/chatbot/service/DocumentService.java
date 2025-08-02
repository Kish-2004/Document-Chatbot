package com.example.chatbot.service;


import com.example.chatbot.model.Document;
import com.example.chatbot.repository.DocumentRepository;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Service
public class DocumentService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private DocumentRepository documentRepository;

    public void storeFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath); // Create directory if it doesn't exist

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || (!originalFileName.endsWith(".pdf") && !originalFileName.endsWith(".txt"))) {
            throw new IllegalArgumentException("Unsupported file type. Only PDF and TXT files are allowed.");
        }

        Path filePath = uploadPath.resolve(originalFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String fileContent = parseDocumentContent(filePath);

        Document document = new Document();
        document.setFileName(originalFileName);
        document.setFileType(file.getContentType());
        document.setContent(fileContent);
        document.setUploadDate(LocalDateTime.now());
        documentRepository.save(document);
    }

    public String parseDocumentContent(Path filePath) throws IOException {
        try (InputStream stream = Files.newInputStream(filePath)) {
            ContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            AutoDetectParser parser = new AutoDetectParser();
            parser.parse(stream, handler, metadata);
            return handler.toString();
        } catch (Exception e) {
            throw new IOException("Error parsing document content: " + e.getMessage(), e);
        }
    }

    public String getDocumentContent(Long documentId) {
        return documentRepository.findById(documentId)
                .map(Document::getContent)
                .orElse(null);
    }

    public String getLatestDocumentContent() {
        return documentRepository.findTopByOrderByUploadDateDesc()
                .map(Document::getContent)
                .orElse(null);
    }
}