package org.apache.camel.learn;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {

    public void configure() {

        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://localhost:61616");
        cf.setPassword("artemis");
        cf.setUser("artemis");
        cf.setMinLargeMessageSize(999999999);
        JmsComponent jmsComponent = new JmsComponent();
        jmsComponent.setConnectionFactory(cf);
        getContext().addComponent("jms", jmsComponent);
        from("file:src/data")
                .to("jms:TEST_QUEUE");

        from("jetty:http://localhost:8081/test")
                .pollEnrich("jms:TEST_QUEUE", 10);
    }

}
