package net.javaguides.ems.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.persistence.EntityManagerFactory;
@Configuration
public class HibernateConfig {
        @Bean
        public org.hibernate.SessionFactory sessionFactory(EntityManagerFactory entityManagerFactory) {
            return entityManagerFactory.unwrap(org.hibernate.SessionFactory.class);
        }
}

