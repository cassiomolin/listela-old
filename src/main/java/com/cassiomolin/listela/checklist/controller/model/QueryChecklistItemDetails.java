package com.cassiomolin.listela.checklist.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class QueryChecklistItemDetails {

    private String id;

    private String description;

    private Boolean completed;

    private ZonedDateTime createdDate;
}