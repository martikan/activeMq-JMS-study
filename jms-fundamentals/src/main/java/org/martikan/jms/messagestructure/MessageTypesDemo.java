package org.martikan.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MessageTypesDemo {

    public static void main(String[] args) throws NamingException, JMSException {

        final var ctx = new InitialContext();

        final var queue = (Queue) ctx.lookup("queue/myQueue");

        try (final var cf = new ActiveMQConnectionFactory();
             final var jmsContext = cf.createContext()) {

            JMSProducer producer = jmsContext.createProducer();

            final var bytesMsg = jmsContext.createBytesMessage();
            bytesMsg.writeUTF("John Doe");
            bytesMsg.writeLong(123);
//            producer.send(queue, bytesMsg);

            final var streamMsg = jmsContext.createStreamMessage();
            streamMsg.writeBoolean(true);
            streamMsg.writeFloat(1.5f);
//            producer.send(queue, bytesMsg);

            final var mapMsg = jmsContext.createMapMessage();
            mapMsg.setBoolean("isCreditAvailable", true);
//            producer.send(queue, mapMsg);

            final var patient = new Patient();
            patient.setId(1);
            patient.setName("John Doe");
//            final var objectMsg = jmsContext.createObjectMessage();
//            objectMsg.setObject(patient);
            producer.send(queue, patient);

//            final var receivedMsg = (BytesMessage) jmsContext.createConsumer(queue).receive(5000);
//            System.out.println(receivedMsg.readUTF());
//            System.out.println(receivedMsg.readLong());

//            final var receivedMsg = (StreamMessage) jmsContext.createConsumer(queue).receive(5000);
//            System.out.println(receivedMsg.readBoolean());
//            System.out.println(receivedMsg.readFloat());

//            final var receiveMsg = (MapMessage) jmsContext.createConsumer(queue).receive(5000);
//            System.out.println(receiveMsg.getBoolean("isCreditAvailable"));

            final var receiveMsg = jmsContext.createConsumer(queue).receiveBody(Patient.class);
            System.out.println(receiveMsg.getId());
            System.out.println(receiveMsg.getName());
        }
    }

}
