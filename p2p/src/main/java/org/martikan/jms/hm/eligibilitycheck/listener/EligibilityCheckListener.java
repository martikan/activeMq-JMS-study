package org.martikan.jms.hm.eligibilitycheck.listener;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.martikan.jms.hm.model.Patient;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.text.MessageFormat;

public class EligibilityCheckListener implements MessageListener {

    final InitialContext initialContext;

    final Queue replyQueue;

    public EligibilityCheckListener() throws NamingException {
        initialContext = new InitialContext();
        replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
    }

    @Override
    public void onMessage(Message message) {
        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {
            final var patient = (Patient) ((ObjectMessage) message).getObject();

            final var replyMsg = jmsContext.createMapMessage();

            final var producer = jmsContext.createProducer();

            System.out.println(MessageFormat.format("insurance provider: {0}", patient.getInsuranceProvider()));
            if (patient.getInsuranceProvider() != null && (patient.getInsuranceProvider().equals("Red shield") || patient.getInsuranceProvider().equals("United Health"))) {
                if ((patient.getCopay() != null && patient.getCopay() < 40) && (patient.getAmountToBePayed() != null && patient.getAmountToBePayed() < 1000)) {
                    replyMsg.setBoolean("eligible", true);
                }
            } else {
                replyMsg.setBoolean("eligible", true);
            }

            producer.send(replyQueue, replyMsg);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
