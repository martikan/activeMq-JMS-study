package org.martikan.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MessagePriority {

    public static void main(String[] args) throws NamingException {

        final var ctx = new InitialContext();

        final var queue = (Queue) ctx.lookup("queue/myQueue");

        try (final var cf = new ActiveMQConnectionFactory();
             final var jmsContext = cf.createContext()){

            final var producer = jmsContext.createProducer();

            final var messages = new String[] {
                    "Message one", "Message two", "Message three"
            };

            producer.setPriority(3);
            producer.send(queue, messages[0]);

            producer.setPriority(1);
            producer.send(queue, messages[1]);

            producer.setPriority(9);
            producer.send(queue, messages[2]);

            // Consuming
            final var consumer = jmsContext.createConsumer(queue);

            for (var i = 0; i < 3; i ++) {
                System.out.println(consumer.receiveBody(String.class));
            }
        }

    }

}
