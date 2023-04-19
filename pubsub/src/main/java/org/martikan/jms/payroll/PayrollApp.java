package org.martikan.jms.payroll;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.martikan.jms.hr.model.Employee;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class PayrollApp {

    public static void main(String[] args) throws NamingException, JMSException {

        final var ctx = new InitialContext();

        final var empTopic = (Topic) ctx.lookup("topic/empTopic");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {
            final var consumer = jmsContext.createConsumer(empTopic);

            final var message = consumer.receive();

            final var employee = message.getBody(Employee.class);

            System.out.println(employee.getEmail());
        }
    }

}
