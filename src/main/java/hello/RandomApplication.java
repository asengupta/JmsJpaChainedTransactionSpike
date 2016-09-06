package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

@SpringBootApplication
@EnableJms
public class RandomApplication {
    private static final Logger log = LoggerFactory.getLogger(RandomApplication.class);
    private static final Logger txLogger = LoggerFactory.getLogger("org.springframework.transaction");

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(RandomApplication.class, args);
        System.out.println("LOLOLOLOLOLOLOLOL");
//        CustomerRepository repository = ctx.getBean(CustomerRepository.class);
//        repository.save(new Customer("mojo@thoughtworks.com", "What's going on?"));
//        for (Customer customer : repository.findAll()) {
//            log.info(customer.toString());
//        }

//        JmsTemplate jmsTemplate = ctx.getBean(JmsTemplate.class);
//        System.out.println("Sending an email message.");
//        jmsTemplate.convertAndSend("mailbox", new Email("info@example.com", "Hello"));
    }
}
