package org.martikan.jms.basics;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.text.MessageFormat;
import java.util.Enumeration;

/**
 * Hello world!
 *
 */
public class QueueBrowserDemo {
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

            TextMessage msg1 = session.createTextMessage("Msg 1");
            TextMessage msg2 = session.createTextMessage("Msg 2");


            producer.send(msg1);
            producer.send(msg2);

            QueueBrowser browser = session.createBrowser(queue);
            Enumeration messagesEnum = browser.getEnumeration();
            while (messagesEnum.hasMoreElements()) {
                TextMessage msg = (TextMessage) messagesEnum.nextElement();
                System.out.println(MessageFormat.format("Browsing: {0}", msg.getText()));
            }

            MessageConsumer consumer = session.createConsumer(queue);
            connection.start();

            TextMessage msgReceiver1 = (TextMessage) consumer.receive(5000);
            System.out.println(MessageFormat.format("Message received: {0}", msgReceiver1.getText()));

            TextMessage msgReceiver2 = (TextMessage) consumer.receive(5000);
            System.out.println(MessageFormat.format("Message received: {0}", msgReceiver2.getText()));
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
