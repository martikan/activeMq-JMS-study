package org.martikan.jms.hm.eligibilitycheck;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.martikan.jms.hm.eligibilitycheck.listener.EligibilityCheckListener;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EligibilityCheckerApp {

    public static void main(String[] args) throws NamingException, InterruptedException {

        final var initialContext = new InitialContext();
        final var reqQueue = (Queue) initialContext.lookup("queue/requestQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {
            final var consumer = jmsContext.createConsumer(reqQueue);
            consumer.setMessageListener(new EligibilityCheckListener());

            Thread.sleep(10000);
        }
    }

}
