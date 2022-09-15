package com.orderfulfillmentsimulation.dispatchstrategy;
import com.orderfulfillmentsimulation.event.model.OrderPickupEvent;
import com.orderfulfillmentsimulation.event.publisher.EventPublisher;
import com.orderfulfillmentsimulation.model.Courier;
import com.orderfulfillmentsimulation.model.Order;
import com.orderfulfillmentsimulation.services.Datastore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Has Implementation dispatching courier based on a matched order
 */
@Component
@Slf4j
public class OrderBoundDispatchStrategy extends DispatchStrategy {

    List<Order> orders = new ArrayList<>();
    List<Courier> couriers = new ArrayList<>();

    @Autowired
    Datastore datastore;

    @Autowired
    EventPublisher eventPublisher;

    @Override
    public void addCourier(Courier courier) {
        log.info("Courier Arrived, {}"+courier.getId());
        couriers.add(courier);
        pickupOrder(courier);
    }

    @Override
    public void orderReady(Order order) {
        orders.add(order);
        log.info("Order Ready : {}-{}",order.getId(),order.getName());
        pickupOrder(order);
    }

    /**
     * When order is ready get courier for that order and pickup
     * @param order
     */
    public void pickupOrder(Order order) {
        Optional<Courier> courierResult = couriers.stream().filter((Courier c)->c.getOrderId().equals(order.getId())).findFirst();
        if(courierResult.isPresent()){
            Courier courier = courierResult.get();
            datastore.setPickedUp(order.getId());
            log.info("Order picked up : {}-{}, by courier : {}",order.getId(),order.getName(),courier.getId());

            couriers.remove(courier);
            orders.remove(order);
        }
    }

    /**
     * When courier is available look for associated order and pickup
     * @param courier
     */
    public void pickupOrder(Courier courier) {
        Optional<Order> orderResult = orders.stream().filter((Order order)->order.getId().equals(courier.getOrderId())).findFirst();
        if(orderResult.isPresent()){
            Order order = orderResult.get();

            couriers.remove(courier);
            orders.remove(order);
            eventPublisher.publishOrderPickedUpEvent(new OrderPickupEvent(order,courier));
        }
    }

}
