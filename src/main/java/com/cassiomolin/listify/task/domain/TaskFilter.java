package com.cassiomolin.listify.task.domain;

import lombok.Data;

@Data
public class TaskFilter {

    private String description;
    private Boolean completed;

}