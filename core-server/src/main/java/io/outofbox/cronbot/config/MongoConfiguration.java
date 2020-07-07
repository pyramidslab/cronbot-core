package io.outofbox.cronbot.config;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
@Profile("!testing")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    private String host;
    private String db;

    public MongoConfiguration(@Value("${mongo.host}") String host,
                              @Value("${mongo.db}") String db) {
        this.host = host;
        this.db = db;
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        MongoClient mongoClient = MongoClients.create(host);
        return mongoClient;
    }

    @Override
    protected String getDatabaseName() {
        return db;
    }
}
