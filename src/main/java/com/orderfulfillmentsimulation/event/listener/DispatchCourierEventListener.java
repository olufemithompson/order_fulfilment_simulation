package com.orderfulfillmentsimulation.event.listener;

import com.orderfulfillmentsimulation.dispatchstrategy.OrderBoundDispatchStrategy;
import com.orderfulfillmentsimulation.dispatchstrategy.QueueCourierDispatchStrategy;
import com.orderfulfillmentsimulation.event.model.DispatchCourierEvent;
import com.orderfulfillmentsimulation.model.Courier;
import com.orderfulfillmentsimulation.model.Order;
import com.orderfulfillmentsimulation.services.Datastore;
import com.orderfulfillmentsimulation.services.OrderPreparer;
import com.orderfulfillmentsimulation.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

/***
 *
 * Listens for when a courier is dispatched.
 * It then uses the actual courier dispatch strategy
 */
@Component
@Slf4j
public class DispatchCourierEventListener {

    @Value("${app.dispatch-strategy}")
    String dispatchStrategy;

    @Autowired
    private ApplicationContext context;

    @Autowired
    OrderPreparer orderPreparer;

    @Autowired
    Datastore datastore;


    @EventListener
    @Async
    void receiveEvent(DispatchCourierEvent orderReceivedEvent) throws InterruptedException {
        Order order = orderReceivedEvent.getOrder();
        dispatchCourier(order);
    }

    private void dispatchCourier(Order order){
        Courier courier = new Courier();
        courier.setId(UUID.randomUUID().toString());
        log.info("Courier Dispatched, {}",courier.getId());
        if(dispatchStrategy.equalsIgnoreCase(Constants.QUEUE_DISPATCH_STRATEGY)){
            QueueCourierDispatchStrategy dispatchStrategy = context.getBean(QueueCourierDispatchStrategy.class);
            dispatchStrategy.dispatch(courier);
        }else if(dispatchStrategy.equalsIgnoreCase(Constants.ORDER_DISPATCH_STRATEGY)){
            OrderBoundDispatchStrategy dispatchStrategy = context.getBean(OrderBoundDispatchStrategy.class);
            courier.setOrderId(order.getId());
            dispatchStrategy.dispatch(courier);
        }

    }
}
