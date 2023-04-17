package org.martikan.jms.hm.clinicals;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.martikan.jms.hm.model.Patient;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.text.MessageFormat;

public class ClinicalApp {

    public static void main(String[] args) throws NamingException, JMSException {

        final var initialContext = new InitialContext();

        final var reqQueue = (Queue) initialContext.lookup("queue/requestQueue");

        final var replyQueue = (Queue) initialContext.lookup("queue/replyQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {
            final var producer = jmsContext.createProducer();

            final var newPatient = new Patient();
            newPatient.setId(123);
            newPatient.setName("John Doe");
            newPatient.setInsuranceProvider("Red shield");
            newPatient.setCopay(30d);
            newPatient.setAmountToBePayed(350d);

            final var message = jmsContext.createObjectMessage(newPatient);

            producer.send(reqQueue, message);

            final var consumer = jmsContext.createConsumer(replyQueue);

            final var replyMessage = (MapMessage) consumer.receive(3000);
            System.out.println(MessageFormat.format("Patient eligibility is: {0}", replyMessage.getBoolean("eligible")));
        }

    }

}
