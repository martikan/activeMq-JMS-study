package org.martikan.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashMap;

public class RequestReplyDemo {

    public static void main(String[] args) throws NamingException, JMSException {

        final var ctx = new InitialContext();

        final var requestQueue = (Queue) ctx.lookup("queue/requestQueue");
//        final var replyQueue = (Queue) ctx.lookup("queue/replyQueue");

        try (final var cf = new ActiveMQConnectionFactory();
             final var jmsContext = cf.createContext()) {

            // Producer
            final var producer = jmsContext.createProducer();
            final var replyQueue = jmsContext.createTemporaryQueue();
            final var msg1 = jmsContext.createTextMessage("Hello there");
            msg1.setJMSReplyTo(replyQueue);
            producer.send(requestQueue, msg1);
            System.out.println(msg1.getJMSMessageID());

            final var requestMessages = new HashMap<String, TextMessage>();
            requestMessages.put(msg1.getJMSMessageID(), msg1);

            // Consumer
            final var consumer = jmsContext.createConsumer(requestQueue);
            final var receivedMsg = (TextMessage) consumer.receive();
            System.out.println(receivedMsg.getText());

            // Reply producer
            final var replyProducer = jmsContext.createProducer();
            final var replyMsg = jmsContext.createTextMessage("Awesome");
            replyMsg.setJMSCorrelationID(receivedMsg.getJMSCorrelationID());
            replyProducer.send(receivedMsg.getJMSReplyTo(), replyMsg);

            // Reply Consumer
            final var replyConsumer = jmsContext.createConsumer(replyQueue);
//            System.out.println(consumer.receiveBody(String.class));
            final var replyReceived = (TextMessage) replyConsumer.receive();
            System.out.println(replyReceived.getJMSCorrelationID());
            System.out.println(requestMessages.get(replyReceived.getJMSCorrelationID()).getText());
        }
    }

}
