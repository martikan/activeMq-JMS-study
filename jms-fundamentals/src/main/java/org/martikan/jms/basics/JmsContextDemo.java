package org.martikan.jms.basics;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JmsContextDemo {

    public static void main(String[] args) throws NamingException {

        final var ctx = new InitialContext();

        final var queue = (Queue) ctx.lookup("queue/myQueue");

        try (final var cf = new ActiveMQConnectionFactory();
             final var jmsContext = cf.createContext()) {

            jmsContext.createProducer().send(queue, "Hello there");

            final var receivedMsg = jmsContext.createConsumer(queue).receiveBody(String.class);

            System.out.println(receivedMsg);
        }
    }

}
