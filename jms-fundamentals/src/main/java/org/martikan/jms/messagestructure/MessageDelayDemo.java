package org.martikan.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MessageDelayDemo {

    public static void main(String[] args) throws NamingException {

        final var ctx = new InitialContext();

        final var queue = (Queue) ctx.lookup("queue/myQueue");

        try (final var cf = new ActiveMQConnectionFactory();
             final var jmsContext = cf.createContext()) {

            JMSProducer producer = jmsContext.createProducer();
            producer.setDeliveryDelay(3000);
            producer.send(queue, "Hello there");

            final var receivedMsg = jmsContext.createConsumer(queue).receive(5000);
            System.out.println(receivedMsg);
        }
    }

}
