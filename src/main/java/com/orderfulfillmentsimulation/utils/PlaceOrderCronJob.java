package com.orderfulfillmentsimulation.utils;

import com.orderfulfillmentsimulation.event.model.DispatchCourierEvent;
import com.orderfulfillmentsimulation.event.model.PrepareOrderEvent;
import com.orderfulfillmentsimulation.event.publisher.EventPublisher;
import com.orderfulfillmentsimulation.model.Order;
import com.orderfulfillmentsimulation.services.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Cronjob that places order every 1 second
 *
 */
@Component
public class PlaceOrderCronJob {

    @Autowired
    Datastore datastore;

    @Autowired
    EventPublisher eventPublisher;

    /**
     * Simulates placing orders every second.
     */
    @Scheduled(fixedDelay=1000)
    public void placeOrder(){
        List<Order> orders = datastore.getNextOrders();
        if(orders != null){
            for(Order order : orders){
                eventPublisher.publishDispatchCourierEvent(new DispatchCourierEvent(order));
                eventPublisher.publishOrderReceivedEvent(new PrepareOrderEvent(order));
            }
        }


    }
}
