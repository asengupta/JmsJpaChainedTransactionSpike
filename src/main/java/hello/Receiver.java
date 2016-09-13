package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class Receiver {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    TransactionTemplate transactionTemplate;

    private static final Logger log = LoggerFactory.getLogger(RandomApplication.class);

    @JmsListener(destination = "mojo01", containerFactory = "myJmsListenerContainerFactory")
    public void receiveMessage(String s) {
        log.info("Received <" + s + ">");

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                customerRepository.save(new Customer(s + "-to", s + "-msg"));
                for (Customer customer : customerRepository.findAll()) {
                    log.info(customer.toString());
                }
            }
        });
    }
}
