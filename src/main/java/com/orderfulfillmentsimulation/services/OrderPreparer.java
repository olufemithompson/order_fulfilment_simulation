package com.orderfulfillmentsimulation.services;

import com.orderfulfillmentsimulation.event.model.OrderReadyEvent;
import com.orderfulfillmentsimulation.event.publisher.EventPublisher;
import com.orderfulfillmentsimulation.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Component that handles preparing of the order
 */
@Component
@Slf4j
public class OrderPreparer {
    @Autowired
    EventPublisher eventPublisher;

    @Autowired
    Datastore datastore;

    @Async
    public void prepareOrder(Order order) throws InterruptedException {
        long timeToWait = order.getPrepTime()*1000;
        log.info("Order prepared, {} - {} for {} secs", order.getId(), order.getName(), order.getPrepTime());
        datastore.setReady(order.getId());
        Thread.sleep(timeToWait);
        eventPublisher.publishOrderReadyEvent(new OrderReadyEvent(order));
    }

}
