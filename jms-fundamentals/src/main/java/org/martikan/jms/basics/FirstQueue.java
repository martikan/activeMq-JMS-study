package org.martikan.jms.basics;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.text.MessageFormat;

/**
 * Hello world!
 *
 */
public class FirstQueue {
    public static void main( String[] args ) {

        InitialContext initialContext = null;

        Connection connection = null;

        try {
            initialContext = new InitialContext();

            ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");

            connection = cf.createConnection();

            Session session = connection.createSession();

            Queue queue = (Queue) initialContext.lookup("queue/myQueue");

            MessageProducer producer = session.createProducer(queue);
            TextMessage msg = session.createTextMessage("Hello there");
            producer.send(msg);
            System.out.println(MessageFormat.format("Message has been sent: {0}", msg.getText()));

            MessageConsumer consumer = session.createConsumer(queue);
            connection.start();
            TextMessage msgReceiver = (TextMessage) consumer.receive(5000);
            System.out.println(MessageFormat.format("Message received: {0}", msgReceiver.getText()));
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        } finally {
            if (initialContext != null) {
                try {
                    initialContext.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
