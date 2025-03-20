package com.example.taxiservice.config;

import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.taxiservice.repository")
public class HibernateSpatialConfig {

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource, JpaProperties jpaProperties) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.example.taxiservice.entity");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>(jpaProperties.getProperties());

        // Используем современный диалект PostgreSQL с поддержкой PostGIS
        properties.put("hibernate.dialect", "org.hibernate.spatial.dialect.postgis.PostgisPG10Dialect");

        // Стратегия именования для преобразования camelCase в snake_case
        properties.put("hibernate.physical_naming_strategy",
                "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");

        // Добавляем показ SQL запросов для отладки
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");

        // Автоматическое создание схемы базы данных
        properties.put("hibernate.hbm2ddl.auto", "update");

        em.setJpaPropertyMap(properties);

        return em;
    }
}