package com.example.chatbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private AiIntegrationService aiIntegrationService;

    // Define a threshold for local matching accuracy (e.g., 70% of meaningful keywords must match)
    private static final double MIN_KEYWORD_PERCENTAGE_FOR_LOCAL_MATCH = 0.7; // 70%

    // A set of common English stopwords to filter out from questions
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "a", "an", "the", "is", "are", "was", "were", "be", "been", "being",
            "and", "or", "but", "if", "then", "else", "when", "where", "why", "how",
            "what", "which", "who", "whom", "whose", "this", "that", "these", "those",
            "my", "your", "his", "her", "its", "our", "their", "me", "you", "him", "her", "it", "us", "them",
            "i", "we", "he", "she", "it", "they", "to", "of", "in", "on", "at", "for", "with", "by", "about",
            "against", "between", "into", "through", "during", "before", "after", "above", "below", "up", "down",
            "out", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where",
            "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no",
            "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just",
            "don", "should", "now", "from", "has", "had", "have", "do", "does", "did", "could", "would", "shall",
            "may", "might", "must", "get", "go", "say", "see", "make", "know", "take", "come", "think", "look",
            "want", "give", "use", "find", "tell", "ask", "work", "seem", "feel", "try", "leave", "call"
    ));

    public String getAnswer(String question) {
        String documentContent = documentService.getLatestDocumentContent();

        if (documentContent == null || documentContent.isBlank()) {
            return "No document uploaded yet. Please upload a document first.";
        }

        // --- Step 1: Try Local Sentence Matching with stricter criteria ---
        List<String> sentences = Arrays.stream(documentContent.split("(?<=[.!?])\\s+(?=[A-Z])|\\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        // Extract meaningful keywords from the question by removing stopwords
        String[] rawKeywords = question.toLowerCase().split("\\s+");
        List<String> meaningfulKeywords = Arrays.stream(rawKeywords)
                .filter(k -> !k.isBlank() && !STOP_WORDS.contains(k))
                .collect(Collectors.toList());

        int totalMeaningfulKeywords = meaningfulKeywords.size();


        if (totalMeaningfulKeywords == 0) {
            // Fallback to AI if the question is too generic or only contains stopwords
            System.out.println("No meaningful keywords found in the question, falling back to Gemini.");
            return getGeminiAnswer(documentContent, question);
        }

        var bestMatch = sentences.stream()
                .map(sentence -> {
                    long matchCount = meaningfulKeywords.stream()
                            .filter(k -> sentence.toLowerCase().contains(k))
                            .count();
                    double matchPercentage = (double) matchCount / totalMeaningfulKeywords;
                    return new Object() {
                        String matchedSentence = sentence;
                        long count = matchCount;
                        double percentage = matchPercentage;
                    };
                })
                // Filter for sentences that meet the minimum percentage threshold
                .filter(obj -> obj.percentage >= MIN_KEYWORD_PERCENTAGE_FOR_LOCAL_MATCH)
                // Prioritize the sentence with the highest percentage match
                .max(Comparator.comparingDouble(obj -> obj.percentage))
                .orElse(null);

        if (bestMatch != null) {
            System.out.println("Local match found with keyword count " + bestMatch.count + " (" + (bestMatch.percentage * 100) + "%)");
            return bestMatch.matchedSentence;
        }

        // --- Step 2: Fallback to Gemini AI if no local match is found ---
        System.out.println("No local match found, falling back to Gemini AI.");
        return getGeminiAnswer(documentContent, question);
    }

    // Helper method to encapsulate Gemini AI call logic
    private String getGeminiAnswer(String documentContent, String question) {
        String context = documentContent.length() > 4000
                ? documentContent.substring(0, 4000)
                : documentContent;

        String prompt = """
        You are an intelligent document assistant.

        Document Context:
        "%s"

        Question:
        "%s"

        Answer based only on the document context above. If the answer cannot be found, say: "I couldn't find the answer in the document."
        """.formatted(context, question);

        System.out.println("Prompt sent to Gemini:\n" + prompt);

        String aiResponse = aiIntegrationService.getGeminiResponse(prompt);
        System.out.println("Gemini Raw Response:\n" + aiResponse);

        if (aiResponse != null && !aiResponse.isBlank() && !aiResponse.toLowerCase().contains("error")) {
            return aiResponse.trim();
        }

        // --- Step 3: Final fallback ---
        return "I couldn't find the answer in the document or generate one using AI.";
    }
}
