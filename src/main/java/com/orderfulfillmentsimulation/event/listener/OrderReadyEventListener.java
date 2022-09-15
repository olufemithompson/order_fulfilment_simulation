package com.orderfulfillmentsimulation.event.listener;

import com.orderfulfillmentsimulation.dispatchstrategy.OrderBoundDispatchStrategy;
import com.orderfulfillmentsimulation.dispatchstrategy.QueueCourierDispatchStrategy;
import com.orderfulfillmentsimulation.event.model.OrderReadyEvent;
import com.orderfulfillmentsimulation.model.Order;
import com.orderfulfillmentsimulation.utils.Constants;
import com.orderfulfillmentsimulation.utils.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Event listener that listed for when an order is ready event
 */
@Component
public class OrderReadyEventListener {

    @Autowired
    private ApplicationContext context;

    @Value("${app.dispatch-strategy}")
    String dispatchStrategy;

    @Async
    @EventListener
    void receiveEvent(OrderReadyEvent event) {
        Order order = event.getOrder();
        order.setCurrentState(OrderState.ORDER_WAITING);
        if(dispatchStrategy.equalsIgnoreCase(Constants.QUEUE_DISPATCH_STRATEGY)){
            QueueCourierDispatchStrategy dispatchStrategy = context.getBean(QueueCourierDispatchStrategy.class);
            dispatchStrategy.orderReady(order);
        }
        else if(dispatchStrategy.equalsIgnoreCase(Constants.ORDER_DISPATCH_STRATEGY)){
            OrderBoundDispatchStrategy dispatchStrategy = context.getBean(OrderBoundDispatchStrategy.class);
            dispatchStrategy.orderReady(order);
        }

    }

}
