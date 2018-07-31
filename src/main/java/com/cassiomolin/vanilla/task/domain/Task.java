package com.cassiomolin.vanilla.task.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class Task {

    @Id
    private String id;

    private String description;

    private Boolean completed;

    private ZonedDateTime createdDate;
}
