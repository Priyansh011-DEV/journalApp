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

@Document(collection = "journal_entries")
@Data
@NoArgsConstructor
public class JournalEntry {
    @Id
    private ObjectId id;
    @NotBlank(message = "title is required")
    private String title;
    private String content;
    @JsonIgnore
    private LocalDateTime date;
}
