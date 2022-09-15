package com.orderfulfillmentsimulation.utils;

import com.orderfulfillmentsimulation.event.model.OrderReceivedEvent;
import com.orderfulfillmentsimulation.event.publisher.EventPublisher;
import com.orderfulfillmentsimulation.model.Order;
import com.orderfulfillmentsimulation.services.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CronJob {

    @Autowired
    Datastore datastore;

    @Autowired
    EventPublisher eventPublisher;

    /**
     * Simulates receiving orders every second.
     */
    @Scheduled(fixedDelay=1000*4)
    public void sendOutOrders(){
        List<Order> orders = datastore.getNextOrders();
        eventPublisher.publishOrderReceivedEvent(new OrderReceivedEvent(orders));
    }
}
