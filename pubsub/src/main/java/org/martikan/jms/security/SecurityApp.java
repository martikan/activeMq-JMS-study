package org.martikan.jms.security;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.martikan.jms.hr.model.Employee;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SecurityApp {

    public static void main(String[] args) throws NamingException, JMSException, InterruptedException {

        final var ctx = new InitialContext();

        final var empTopic = (Topic) ctx.lookup("topic/empTopic");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {
            jmsContext.setClientID("security-app");

            final var consumerDead = jmsContext.createDurableConsumer(empTopic, "subscription1");
            consumerDead.close(); Thread.sleep(10000); // Simulate the server down.

            final var consumerRestarted = jmsContext.createDurableConsumer(empTopic, "subscription1");

            final var message = consumerRestarted.receive();

            final var employee = message.getBody(Employee.class);

            System.out.println(employee.getEmail());

            consumerRestarted.close();
            jmsContext.unsubscribe("subscription1");
        }
    }

}
