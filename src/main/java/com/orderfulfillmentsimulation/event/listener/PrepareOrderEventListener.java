package com.orderfulfillmentsimulation.event.listener;

import com.orderfulfillmentsimulation.model.Order;
import com.orderfulfillmentsimulation.services.Datastore;
import com.orderfulfillmentsimulation.services.OrderPreparer;
import com.orderfulfillmentsimulation.event.model.PrepareOrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * This components waits for when an event for preparing order is fired
 * it then starts the process of preparing an order.
 */
@Component
@Slf4j
public class PrepareOrderEventListener {

    @Autowired
    OrderPreparer orderPreparer;

    @Autowired
    Datastore datastore;

    @EventListener
    @Async
    void receiveEvent(PrepareOrderEvent orderReceivedEvent) throws InterruptedException {
        Order order = orderReceivedEvent.getOrder();
        log.info("Order received, {} - {}", order.getId(), order.getName());
        datastore.setReceived(order.getId());
        orderPreparer.prepareOrder(orderReceivedEvent.getOrder());
    }

}
