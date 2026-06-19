package net.javaguides.ems.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Lazy;

@Configuration
public class HibernateConfig {
        @Bean
        @Lazy
        public org.hibernate.SessionFactory sessionFactory(@Lazy EntityManagerFactory entityManagerFactory) {
            return entityManagerFactory.unwrap(org.hibernate.SessionFactory.class);
        }
}

