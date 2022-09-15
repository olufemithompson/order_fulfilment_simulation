package com.orderfulfillmentsimulation.dispatchstrategy;

import com.orderfulfillmentsimulation.event.model.OrderPickupEvent;
import com.orderfulfillmentsimulation.event.publisher.EventPublisher;
import com.orderfulfillmentsimulation.model.Courier;
import com.orderfulfillmentsimulation.model.Order;
import com.orderfulfillmentsimulation.services.Datastore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *  Has Implementation dispatching courier based  courier that arrived first.
 */
@Component
@Slf4j
public class QueueCourierDispatchStrategy extends DispatchStrategy{
    @Autowired
    Datastore datastore;

    @Autowired
    EventPublisher eventPublisher;


    //use a queue to ensure FIFO for choosing which courier to deliver the order
    Queue<Courier> couriers = new ArrayDeque<>();

    //use a queue to hold orders that have been prepared. the first order in the queue will be picked up first by the courier
    Queue<Order> orders = new ArrayDeque<>();

    @Override
    public void addCourier(Courier courier) {
        log.info("Courier Arrived, {}", courier.getId());
        courier.setArriveTime(System.currentTimeMillis());
        couriers.add(courier);
        deliverOrder();
    }

    @Override
    public void orderReady(Order order){
        orders.add(order);
        log.info("Order Ready : {}-{}",order.getId(),order.getName());
        deliverOrder();
    }

    public void deliverOrder(){
        //only deliver order if courier is available and an order is available for pickup.
        if(!orders.isEmpty() && !couriers.isEmpty()){

            // get first to arrive carrier
            Courier courier = couriers.poll();

            //get first order
            Order order = orders.poll();

            //set the courier arrive time here as this is the only time we get it from courier
            order.setCourierArriveTime(courier.getArriveTime());
            eventPublisher.publishOrderPickedUpEvent(new OrderPickupEvent(order,courier));
        }
    }

}
