package com.cassiomolin.listela.checklist.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Data
@Document
@NoArgsConstructor
@Accessors(chain = true)
public class ChecklistItem {

    @Id
    private String id;

    private String title;

    private Boolean completed;

    @CreatedDate
    private OffsetDateTime createdDate;
}
