package hello;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.springframework.jms.listener.DefaultMessageListenerContainer.CACHE_CONSUMER;

@Configuration
@EnableJms
public class AppConfig {

    @Value("lol")
    public String val;
    
    @Bean
    public ActiveMQConnectionFactory connectionFactory() throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL("tcp://localhost:61616");
        connectionFactory.createQueueConnection();
        return connectionFactory;
    }

    @Bean
    JmsTransactionManager getJmsTransactionManager(ActiveMQConnectionFactory connectionFactory) {
        JmsTransactionManager jmsTransactionManager = new JmsTransactionManager();
        connectionFactory.getRedeliveryPolicy().setUseExponentialBackOff(true);
        connectionFactory.getRedeliveryPolicy().setMaximumRedeliveries(5);
        connectionFactory.getRedeliveryPolicy().setBackOffMultiplier(1);
        jmsTransactionManager.setConnectionFactory(connectionFactory);
        return jmsTransactionManager;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:mojodb");
        return builder
                .dataSource(dataSource)
                .packages(Customer.class)
                .persistenceUnit("customers")
                .build();
    }

    @Bean
    JpaTransactionManager getJpaTransactionManager(EntityManagerFactory emf) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(emf);
        return jpaTransactionManager;
    }

    @Bean
    PlatformTransactionManager transactionManager(JmsTransactionManager jmsTransactionManager, JpaTransactionManager jpaTransactionManager) {
        System.out.println("========================================================");
        System.out.println("========================================================");
        System.out.println("CREATING A CHAINED TRANSACTION");
        System.out.println("========================================================");
        System.out.println("========================================================");
        ChainedTransactionManager transactionManager = new ChainedTransactionManager(jmsTransactionManager, jpaTransactionManager);
        return transactionManager;
    }

    @Bean
    public JmsListenerContainerFactory<?> myJmsListenerContainerFactory(ActiveMQConnectionFactory connectionFactory, PlatformTransactionManager transactionManager) {
        System.out.println("========================================================");
        System.out.println("========================================================");
        System.out.println("CREATING A CONTAINER FACTORY");
        System.out.println("========================================================");
        System.out.println("========================================================");
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        factory.setSessionTransacted(true);
        factory.setConnectionFactory(connectionFactory);
//        factory.setCacheLevel(CACHE_CONSUMER);
        factory.setTransactionManager(transactionManager);
        return factory;
    }
}