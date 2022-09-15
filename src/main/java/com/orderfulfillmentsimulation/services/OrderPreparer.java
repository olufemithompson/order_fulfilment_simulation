package com.orderfulfillmentsimulation.services;

import com.orderfulfillmentsimulation.event.model.OrderReadyEvent;
import com.orderfulfillmentsimulation.event.publisher.EventPublisher;
import com.orderfulfillmentsimulation.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class OrderPreparer {
    @Autowired
    EventPublisher eventPublisher;

    @Autowired
    Datastore datastore;


    public void prepareOrders(List<Order> orders) throws InterruptedException {
        for(Order order: orders){
            prepareOrder(order);
        }
    }

    @Async
    private void prepareOrder(Order order) throws InterruptedException {
        long timeToWait = order.getPrepTime()*1000;
        log.info("Order prepared, {} - {} for {} secs", order.getId(), order.getName(), order.getPrepTime());
        datastore.setReady(order.getId());
        Thread.sleep(timeToWait);
        log.info("Order Done prepared, {} - {}", order.getId(), order.getName());
        eventPublisher.publishOrderReadyEvent(new OrderReadyEvent(order));
    }

}
