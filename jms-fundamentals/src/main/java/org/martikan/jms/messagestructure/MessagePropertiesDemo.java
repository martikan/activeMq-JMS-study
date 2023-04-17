package org.martikan.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MessagePropertiesDemo {

    public static void main(String[] args) throws NamingException, JMSException {

        final var ctx = new InitialContext();

        final var queue = (Queue) ctx.lookup("queue/myQueue");

        try (final var cf = new ActiveMQConnectionFactory();
             final var jmsContext = cf.createContext()) {

            JMSProducer producer = jmsContext.createProducer();
            final var msg = jmsContext.createTextMessage("Hello there");
            msg.setBooleanProperty("loggedIn", true);
            msg.setStringProperty("token", "1234567890");
            producer.send(queue, msg);

            final var receivedMsg = jmsContext.createConsumer(queue).receive(5000);
            System.out.println(receivedMsg);
            System.out.println(receivedMsg.getBooleanProperty("loggedIn"));
            System.out.println(receivedMsg.getStringProperty("token"));
        }
    }

}
