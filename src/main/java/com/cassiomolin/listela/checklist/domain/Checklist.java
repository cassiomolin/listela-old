package com.cassiomolin.listela.checklist.domain;

import com.cassiomolin.listela.user.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Document
@NoArgsConstructor
@Accessors(chain = true)
public class Checklist {

    @Id
    private String id;

    private String name;

    @DBRef(lazy = true)
    private User owner;

    @DBRef(lazy = true)
    private List<ChecklistItem> items;

    @CreatedDate
    private OffsetDateTime createdDate;
}
