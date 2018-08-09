package com.cassiomolin.listela;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public abstract class AbstractTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    protected void cleanDatabase() {
        mongoTemplate.getDb().drop();
    }
}
