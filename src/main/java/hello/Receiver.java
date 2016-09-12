package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    @Autowired
    private CustomerRepository customerRepository;

    private static final Logger log = LoggerFactory.getLogger(RandomApplication.class);

    @JmsListener(destination = "mojo01", containerFactory = "myJmsListenerContainerFactory")
    public void receiveMessage(String s) {
//        if (1 == 1) throw new RuntimeException("BAHAHAHAHA");
        log.info("Received <" + s + ">");

        customerRepository.save(new Customer(s + "-to", s + "-msg"));
        for (Customer customer : customerRepository.findAll()) {
            log.info(customer.toString());
        }
    }
}
