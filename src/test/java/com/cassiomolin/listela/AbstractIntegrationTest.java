package com.cassiomolin.listela;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class AbstractIntegrationTest {

    @LocalServerPort
    protected int port;

    @Value("${auth.jwt.secret}")
    protected String jwtKey;

    @Value("${auth.jwt.issuer}")
    protected String jwtIssuer;

    @Value("${auth.jwt.audience}")
    protected String jwtAudience;

    @Autowired
    protected MongoTemplate mongoTemplate;

    protected void cleanDatabase() {
        mongoTemplate.getDb().drop();
    }

    @Before
    public void beforeEach() {
        cleanDatabase();
    }

    @Before
    public void afterEach() {

    }

    protected String issueAuthenticationToken(String subject) {

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer(jwtIssuer)
                .setAudience(jwtAudience)
                .setSubject(subject)
                .setIssuedAt(Date.from(OffsetDateTime.now().toInstant()))
                .setExpiration(Date.from(OffsetDateTime.now().plusMinutes(1).toInstant()))
                .signWith(SignatureAlgorithm.HS256, jwtKey)
                .compact();
    }

    protected void createTestUser() {
        Document document = new Document();
        document.put("_id", ObjectId.get());
        document.put("firstName", "Jane");
        document.put("lastName", "Doe");
        document.put("email", "jane.doe@mail.com");
        document.put("password", "$2a$10$LhvW555n98L8qNIQ04208.8sNEV.IptTxkeub0pln04b6wo2JFTYa"); // 'password' hashed with bcrypt
        document.put("active", true);
        document.put("createdDate", new Date());
        document.put("_class", "com.cassiomolin.listela.user.domain.User");
        mongoTemplate.getCollection("user").insertOne(document);
    }
}
