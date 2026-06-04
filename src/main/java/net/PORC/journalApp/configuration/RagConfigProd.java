package net.PORC.journalApp.configuration;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class RagConfigProd {


    @Value("${huggingface.api.token}")
    private String hfToken;



    @Value("${spring.datasource.vector.username}")
    private String dbUser;

    @Value("${spring.datasource.vector.password}")
    private String dbPassword;

    @Bean
    public EmbeddingModel embeddingModel() {
        return HuggingFaceEmbeddingModel.builder()
                .accessToken(hfToken)
                .modelId("sentence-transformers/all-MiniLM-L6-v2")
                .waitForModel(true)
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
                .table("document_chunks_prod")
                .dimension(384)
                .createTable(true)
                .build();
    }
}
