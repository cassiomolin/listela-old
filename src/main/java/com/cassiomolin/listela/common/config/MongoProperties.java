package com.cassiomolin.listela.common.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class MongoProperties {

    private String host;

    private Integer port;

    private String database;
}
