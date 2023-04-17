package org.martikan.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MessageExpirationDemo {

    public static void main(String[] args) throws NamingException, InterruptedException {

        final var ctx = new InitialContext();

        final var queue = (Queue) ctx.lookup("queue/myQueue");
        final var expiryQueue = (Queue) ctx.lookup("queue/expiryQueue");

        try (final var cf = new ActiveMQConnectionFactory();
             final var jmsContext = cf.createContext()) {

            JMSProducer producer = jmsContext.createProducer();
            producer.setTimeToLive(2000);
            producer.send(queue, "Hello there");
            Thread.sleep(5000);

            final var receivedMsg = jmsContext.createConsumer(queue).receive(5000);
            System.out.println(receivedMsg);

            System.out.println(jmsContext.createConsumer(expiryQueue).receiveBody(String.class));
        }
    }

}
