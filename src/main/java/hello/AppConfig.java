package hello;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.springframework.jms.listener.DefaultMessageListenerContainer.CACHE_CONSUMER;

@Configuration
@EnableJms
public class AppConfig {

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
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
    JpaTransactionManager getJpaTransactionManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU");
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(emf);
        return jpaTransactionManager;
    }

    @Bean
    public JmsListenerContainerFactory<?> myJmsListenerContainerFactory(ActiveMQConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setSessionTransacted(true);
        factory.setConnectionFactory(connectionFactory);
        factory.setCacheLevel(CACHE_CONSUMER);

        ChainedTransactionManager transactionManager = new ChainedTransactionManager(getJmsTransactionManager(connectionFactory), getJpaTransactionManager());
        factory.setTransactionManager(transactionManager);
        return factory;
    }
}