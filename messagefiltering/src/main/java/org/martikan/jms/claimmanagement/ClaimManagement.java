package org.martikan.jms.claimmanagement;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.martikan.jms.claimmanagement.model.Claim;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ClaimManagement {

    public static void main(String[] args) throws NamingException, JMSException {

        final var ctx = new InitialContext();

        final var claimQueue = (Queue) ctx.lookup("queue/claimQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {
            final var producer = jmsContext.createProducer();

            final var consumer = jmsContext.createConsumer(claimQueue, "claimAmount BETWEEN 300 AND 700 OR JMSPriority BETWEEN 3 AND 6");

            final var claim = new Claim();
            claim.setHospitalId(123);
            claim.setDoctorName("John Doe");
            claim.setDoctorType("gyna");
            claim.setInsuranceProvider("CareEu");
            claim.setClaimAmount(400d);

            final var message = jmsContext.createObjectMessage(claim);
//            message.setIntProperty("hospitalId", 1);
            message.setDoubleProperty("claimAmount", claim.getClaimAmount());

            producer.send(claimQueue, message);

            final var receivedMsg = consumer.receive().getBody(Claim.class);
            System.out.println(receivedMsg.getClaimAmount());
        }

    }

}
