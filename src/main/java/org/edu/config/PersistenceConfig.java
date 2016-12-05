package org.edu.config;


import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@PropertySource({"classpath:persistence.properties"})
public class PersistenceConfig {

    @Autowired
    Environment env;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(restDataSource());
        sessionFactory.setPackagesToScan(new String[]{"org.edu.model"});
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource restDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
//        String dbUrl = System.getenv("LOCAL_DATABASE_URL"); //get db url from environment variables
        String dbUrl = System.getenv("LOCAL_DB");
        /*
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.user"));
        dataSource.setPassword(env.getProperty("jdbc.pass"));
        */
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        //crazy parsing of db url
        String host = dbUrl.split("/")[2].split("@")[1].split(":")[0];
        String port = dbUrl.split("/")[2].split("@")[1].split(":")[1];
        String path = dbUrl.split("/")[3];
        String username = dbUrl.split("/")[2].split(":")[0];
        String password = dbUrl.split("/")[2].split("@")[0].split(":")[1];

        dataSource.setUrl("jdbc:postgresql://" + host + ":" + port +
                "/" + path);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    private Properties hibernateProperties() {
        return new Properties() {
            {
                setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
                setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
                setProperty("hibernate.globally_quoted_identifiers", "true");
                setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
                setProperty("hibernate.dbcp.maxActive", "20");
            }
        };
    }
}
