package com.orderfulfillmentsimulation.event.listener;

import com.orderfulfillmentsimulation.event.model.OrderPickupEvent;
import com.orderfulfillmentsimulation.model.Courier;
import com.orderfulfillmentsimulation.model.Order;
import com.orderfulfillmentsimulation.services.Datastore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Event listener that listed for when an order is picked up
 */
@Component
@Slf4j
public class OrderPickupEventListener {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Datastore datastore;

    @Async
    @EventListener
    void receiveEvent(OrderPickupEvent event) {
        Order order = event.getOrder();
        Courier courier = event.getCourier();

        datastore.setPickedUp(order.getId());
        log.info("Order picked up : {}-{}, by courier : {}"+order.getId(),order.getName(),courier.getId());

        if(datastore.isAllOrderCompleted()){
            //print statistics;
        }

    }

}
