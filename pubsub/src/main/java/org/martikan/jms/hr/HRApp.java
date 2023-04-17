package org.martikan.jms.hr;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.martikan.jms.hr.model.Employee;

import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class HRApp {

    public static void main(String[] args) throws NamingException {

        final var ctx = new InitialContext();

        final var empTopic = (Topic) ctx.lookup("topic/empTopic");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {
            final var producer = jmsContext.createProducer();

            final var employee = new Employee();
            employee.setId(123);
            employee.setFirstName("John");
            employee.setLastName("Doe");
            employee.setEmail("j.doe@gmail.com");
            employee.setDesignation("Software engineer");
            employee.setPhone("+363012345522");

            producer.send(empTopic, employee);
            System.out.println("Message sent");
        }
    }

}
