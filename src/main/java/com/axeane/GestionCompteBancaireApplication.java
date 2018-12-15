package com.axeane;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@EnableConfigurationProperties
public class GestionCompteBancaireApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionCompteBancaireApplication.class, args);
    }
}
