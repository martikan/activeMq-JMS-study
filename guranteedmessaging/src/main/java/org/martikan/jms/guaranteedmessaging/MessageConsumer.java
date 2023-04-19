package org.martikan.jms.guaranteedmessaging;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MessageConsumer {

    public static void main(String[] args) throws NamingException {

        final var initialContext = new InitialContext();

        final var reqQueue = (Queue) initialContext.lookup("queue/requestQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {
            final var consumer = jmsContext.createConsumer(reqQueue);

            final var message = (TextMessage) consumer.receive();
            System.out.println(message);
        }

    }

}
