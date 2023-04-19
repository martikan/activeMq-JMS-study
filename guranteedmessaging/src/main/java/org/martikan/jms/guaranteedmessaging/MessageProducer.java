package org.martikan.jms.guaranteedmessaging;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MessageProducer {

    public static void main(String[] args) throws NamingException {

        final var initialContext = new InitialContext();

        final var reqQueue = (Queue) initialContext.lookup("queue/requestQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext(JMSContext.AUTO_ACKNOWLEDGE)) {
            final var producer = jmsContext.createProducer();

            producer.send(reqQueue, "Message123");
        }

    }

}
