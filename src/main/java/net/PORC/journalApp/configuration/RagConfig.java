package net.PORC.journalApp.configuration;

import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class RagConfig {

    @Value("${huggingface.api.token}")
    private String hfToken;

    @Value("${spring.datasource.vector.url}")
    private String dbUrl;

    @Value("${spring.datasource.vector.username}")
    private String dbUser;

    @Value("${spring.datasource.vector.password}")
    private String dbPassword;

    @Bean
    public EmbeddingModel embeddingModel() {
        return OllamaEmbeddingModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("nomic-embed-text")
                .build();
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return PgVectorEmbeddingStore.builder()
                .host("ep-long-king-aocgr9ks.c-2.ap-southeast-1.aws.neon.tech")
                .port(5432)
                .database("neondb")
                .user(dbUser)
                .password(dbPassword)
                .table("document_chunks")
                .dimension(768)
                .createTable(true)
                .build();
    }
}