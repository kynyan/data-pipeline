package project;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(value = "project.repository")
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class PersistenceConfig {
    private final Environment env;
    private final ApplicationContext appContext;

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory() throws PropertyVetoException{
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false);
        vendorAdapter.setDatabase(Database.H2);
        vendorAdapter.setShowSql(false);

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        props.put("hibernate.hbm2ddl.auto", "create-drop");

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("project.model");
        factory.setDataSource(dataSource());
        factory.setJpaPropertyMap(props);
        return factory;
    }

    @Bean
    PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(appContext.getBean(EntityManagerFactory.class));
        return txManager;
    }

    @Bean
    DataSource dataSource() {
        ComboPooledDataSource actualDataSource = new com.mchange.v2.c3p0.ComboPooledDataSource();
        actualDataSource.setJdbcUrl(env.getProperty("db.url"));
        actualDataSource.setUser(env.getProperty("db.username"));
        actualDataSource.setPassword(env.getProperty("db.password"));
        actualDataSource.setMinPoolSize(env.getProperty("db.pool.min_size", int.class));
        actualDataSource.setMaxPoolSize(env.getProperty("db.pool.max_size", int.class));
        actualDataSource.setIdleConnectionTestPeriod(env.getProperty("db.pool.idle_connections_test_period_sec", int.class));
        actualDataSource.setUnreturnedConnectionTimeout(env.getProperty("db.pool.unreturned_connection_timeout_sec", int.class));
        actualDataSource.setCheckoutTimeout(env.getProperty("db.pool.checkout_timeout_millisec", int.class));
        actualDataSource.setAcquireRetryAttempts(env.getProperty("db.pool.acquire_attempts", int.class));
        actualDataSource.setTestConnectionOnCheckin(true);
        return actualDataSource;
    }

}
