package com.orderfulfillmentsimulation.event.listener;

import com.orderfulfillmentsimulation.dispatchstrategy.OrderBoundDispatchStrategy;
import com.orderfulfillmentsimulation.dispatchstrategy.QueueCourierDispatchStrategy;
import com.orderfulfillmentsimulation.model.Courier;
import com.orderfulfillmentsimulation.model.Order;
import com.orderfulfillmentsimulation.services.Datastore;
import com.orderfulfillmentsimulation.services.OrderPreparer;
import com.orderfulfillmentsimulation.event.model.OrderReceivedEvent;
import com.orderfulfillmentsimulation.utils.Constants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Listens to order received events and immediately
 * dispatches a courier to pickup the order based on the dispatch strategy.
 */
@Component
@Slf4j
public class OrderReceivedEventListener {

    @Value("${app.dispatch-strategy}")
    String dispatchStrategy;

    @Autowired
    private ApplicationContext context;

    @Autowired
    OrderPreparer orderPreparer;

    @Autowired
    Datastore datastore;

    @Async
    @EventListener
    void receiveEvent(OrderReceivedEvent orderReceivedEvent) throws InterruptedException {
        List<Order> orders = orderReceivedEvent.getOrders();
        for(Order order : orders){
            log.info("Order received, {} - {}", order.getId(), order.getName());
            datastore.setReceived(order);
            dispatchCourier(order);
        }
        orderPreparer.prepareOrders(orders);
    }

    @Async
    private void dispatchCourier(Order order){
        if(dispatchStrategy.equalsIgnoreCase(Constants.QUEUE_DISPATCH_STRATEGY)){
            QueueCourierDispatchStrategy dispatchStrategy = context.getBean(QueueCourierDispatchStrategy.class);
            Courier courier = new Courier();
            courier.setId(UUID.randomUUID().toString());

            log.info("Courier Dispatched, {}"+courier.getId());
            dispatchStrategy.dispatch(courier);
        }

        if(dispatchStrategy.equalsIgnoreCase(Constants.ORDER_DISPATCH_STRATEGY)){
            OrderBoundDispatchStrategy dispatchStrategy = context.getBean(OrderBoundDispatchStrategy.class);
            Courier courier = new Courier();
            courier.setOrderId(order.getId());
            courier.setId(UUID.randomUUID().toString());

            log.info("Courier Dispatched, {}"+courier.getId());
            dispatchStrategy.dispatch(courier);
        }

    }
}
