package net.PORC.journalApp.service;

import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
@Service
public class RagTestService {

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private EmbeddingStore<TextSegment> embeddingStore;

    public String testEmbedding() {
        try {
            TextSegment segment = TextSegment.from("I felt really anxious today, couldn't sleep at all.");

            System.out.println("🔵 Generating embedding...");
            Embedding embedding = embeddingModel.embed(segment).content();
            System.out.println("🟢 Embedding generated: " + embedding.vector().length);

            embeddingStore.add(embedding, segment);
            System.out.println("🟢 Stored in pgvector!");

            return "Stored! Vector size: " + embedding.vector().length;

        } catch (Exception e) {
            System.err.println("❌ RAG ERROR: " + e.getMessage());
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
    public String testSearch() {
        try {
            // embed the question
            Embedding queryEmbedding = embeddingModel
                    .embed("how was my mood?").content();

            // search for similar entries
            EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                    .queryEmbedding(queryEmbedding)
                    .maxResults(3)
                    .build();

            var results = embeddingStore.search(request).matches();

            StringBuilder sb = new StringBuilder();
            for (var match : results) {
                sb.append("Score: ").append(match.score()).append("\n");
                sb.append("Text: ").append(match.embedded().text()).append("\n\n");
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
