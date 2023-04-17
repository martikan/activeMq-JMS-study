package org.martikan.jms.basics;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import java.text.MessageFormat;

public class FirstTopic {

    public static void main(String[] args) throws Exception {

        InitialContext initialContext = new InitialContext();

        Topic topic = (Topic) initialContext.lookup("topic/myTopic ");

        ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");

        Connection connection = cf.createConnection();

        Session session = connection.createSession();

        // Producer
        MessageProducer producer = session.createProducer(topic);
        TextMessage msg = session.createTextMessage("Hello there");
        producer.send(msg);
        System.out.println(MessageFormat.format("Message has been sent: {0}", msg.getText()));


        // Start
        connection.start();

        // Consumer 1
        MessageConsumer consumer1 = session.createConsumer(topic);
        TextMessage msgReceived1 = (TextMessage) consumer1.receive();
        System.out.println(MessageFormat.format("Consumer 1 -- Message received: {0}", msgReceived1.getText()));

        // Consumer 2
        MessageConsumer consumer2 = session.createConsumer(topic);
        TextMessage msgReceived2 = (TextMessage) consumer2.receive();
        System.out.println(MessageFormat.format("Consumer 2 -- Message received: {0}", msgReceived2.getText()));

        // Close connections
        initialContext.close();
        connection.close();
    }

}
