package org.martikan.jms.grouping;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageGroupingDemo {

    public static void main(String[] args) throws NamingException, JMSException, InterruptedException {

        final var ctx = new InitialContext();

        final var queue = (Queue) ctx.lookup("queue/requestQueue");

        final var receivedMessages = new ConcurrentHashMap<String, String>();

        try (final var cf = new ActiveMQConnectionFactory();
                final var jmsContext = cf.createContext();
                final var jmsContext2 = cf.createContext()) {
            final var producer = jmsContext.createProducer();

            final var consumer1 = jmsContext2.createConsumer(queue);
            consumer1.setMessageListener(new MyListener("Consumer-1", receivedMessages));

            final var consumer2 = jmsContext2.createConsumer(queue);
            consumer2.setMessageListener(new MyListener("Consumer-2", receivedMessages));

            final int count = 10;

            final var messages = new TextMessage[count];
            for (var i = 0; i < count; i++) {
                messages[i] = jmsContext.createTextMessage("Group-0 message " + i);
                messages[i].setStringProperty("JMSXGroupID", "Group-0");

                producer.send(queue, messages[i]);
            }

            Thread.sleep(2000);

            for (final var message : messages) {
                if (!receivedMessages.get(message.getText()).equals("Consumer-1")) {
                    throw new IllegalStateException(MessageFormat.format("Group message: {0} - has gone to the wrong receiver", message.getText()));
                }
            }
        }

    }

    static class MyListener implements MessageListener {

        private final String name;

        private final Map<String, String> receivedMessages;

        MyListener(String name, Map<String, String> receivedMessages) {
            this.name = name;
            this.receivedMessages = receivedMessages;
        }

        @Override
        public void onMessage(Message message) {
            final var textMessage = (TextMessage) message;

            try {
                System.out.println("Message received is: " + textMessage.getText());
                System.out.println("Listener name is: " + name);
                receivedMessages.put(textMessage.getText(), name);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

}
