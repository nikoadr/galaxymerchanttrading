package com.galaxymerchantrading.handlingtransaction.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan("com.galaxymerchanttrading.handlingtransaction")
public class DbConfig {
    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        DataSource dataSource = new DataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://" + env.getProperty("db-host") + ":" + env.getProperty("db-port") + "/" + env.getProperty("db-name"));
        dataSource.setUsername(env.getProperty("db-user"));
        dataSource.setPassword(env.getProperty("db-pass"));
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setValidationInterval(1 * 60 * 1000);
        dataSource.setInitialSize(1);
        dataSource.setTestOnBorrow(true);
        return dataSource;
    }
}
