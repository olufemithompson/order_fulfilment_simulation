package com.orderfulfillmentsimulation.event.listener;

import com.orderfulfillmentsimulation.event.model.OrderPickupEvent;
import com.orderfulfillmentsimulation.model.Courier;
import com.orderfulfillmentsimulation.model.Order;
import com.orderfulfillmentsimulation.services.Datastore;
import dnl.utils.text.table.TextTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

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
        log.info("Order picked up : {}-{}, by courier : {}",order.getId(),order.getName(),courier.getId());


        //once all orders have been picked up, print statistics result. and end program.
        if(datastore.isAllOrderCompleted()){
            List<Order> allOrders = datastore.getAllOrders();
            String[][] data = new String[allOrders.size()][4];
            for(int i = 0; i < allOrders.size(); i++){
                Order o = allOrders.get(i);
                String[] row  = new String[]{o.getId(),o.getName(), Long.toString((o.getPickupTime() - o.getReadyTime())), Long.toString((o.getPickupTime() - o.getCourierArriveTime())) };
                data[i] = row;
            }
            TextTable tt = new TextTable(new String[]{"Id", "Name","Food wait time","Courier Wait Time"}, data);
            tt.printTable();
            System.exit(0);
        }

    }

}
