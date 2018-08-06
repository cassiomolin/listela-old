package com.cassiomolin.listela.checklist.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class QueryChecklistItemDetails {

    private String id;

    private String title;

    private Boolean completed;

    private OffsetDateTime createdDate;
}