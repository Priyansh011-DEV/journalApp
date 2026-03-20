package net.PORC.journalApp.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jdk.jfr.SettingDefinition;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Document(collection = "journal_entries")
@Data
@NoArgsConstructor
public class JournalEntry {
    @Id
    private String id;
    private String username;
    @NotBlank(message = "title is required")
    private String title;
    private String content;
    @JsonIgnore
    private LocalDateTime date;
    private String mood;
    private String aiSummary;
    private String aiAdvice;
    private Map<String, Integer> moodScore;
    private String AiSource;
}
