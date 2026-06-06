package net.PORC.journalApp.configuration;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import dev.langchain4j.model.nomic.NomicEmbeddingModel;

@Configuration
@Profile("prod")
public class RagConfigProd {


    @Value("${nomic.api.key}")
    private String NomicApiKey;



    @Value("${spring.datasource.vector.username}")
    private String dbUser;

    @Value("${spring.datasource.vector.password}")
    private String dbPassword;

    @Bean
    public EmbeddingModel embeddingModel() {
        return NomicEmbeddingModel.builder()
                .apiKey(NomicApiKey)
                .modelName("nomic-embed-text-v1.5")
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
                .dimension(768)
                .createTable(true)
                .build();
    }
}
