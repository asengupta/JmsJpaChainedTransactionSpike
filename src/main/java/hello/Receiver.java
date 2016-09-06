package hello;

import org.hibernate.jpa.internal.EntityManagerFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    @Autowired
    private CustomerRepository repository;

    private static final Logger log = LoggerFactory.getLogger(RandomApplication.class);

    @JmsListener(destination = "mailbox", containerFactory = "myJmsListenerContainerFactory")
    public void receiveMessage(Email email) {
        System.out.println("Received <" + email + ">");
        repository.save(new Customer(email.to, email.message));
        for (Customer customer : repository.findAll()) {
            log.info(customer.toString());
        }
    }
}
