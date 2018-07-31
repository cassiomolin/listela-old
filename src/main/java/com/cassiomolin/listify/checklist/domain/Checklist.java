package com.cassiomolin.listify.checklist.domain;

import com.cassiomolin.listify.task.domain.Task;
import com.cassiomolin.listify.user.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class Checklist {

    @Id
    private String id;

    private String name;

    @DBRef
    private User owner;

    private List<Task> items;

    private ZonedDateTime createdDate;
}
