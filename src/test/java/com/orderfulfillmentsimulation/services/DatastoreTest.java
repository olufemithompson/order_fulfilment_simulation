package com.orderfulfillmentsimulation.services;

import com.orderfulfillmentsimulation.model.Order;
import com.orderfulfillmentsimulation.utils.OrderState;
import com.orderfulfillmentsimulation.utils.PlaceOrderCronJob;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@MockBean(PlaceOrderCronJob.class)
public class DatastoreTest {

    @Autowired
    private Datastore datastore;

    @Test
    public void testDataIsInitiated(){
        assertThat(datastore.getAllOrders().size()).isGreaterThan(0);
    }

    @Test
    public void testNextOrdersReturn2(){
        assertThat(datastore.getNextOrders().size()).isEqualTo(2);
    }

    @Test
    public void testSetPickupOrder(){
        List<Order> orders = datastore.getAllOrders();
        String id = orders.get(0).getId();
        datastore.setPickedUp(id);
        Order order = orders.stream().filter((Order i) -> i.getId().equals(id)).findFirst().get();
        assertThat(order.getCurrentState()).isEqualTo(OrderState.ORDER_PICKEDUP);
    }


    @Test
    public void testSetReadyOrder(){
        List<Order> orders = datastore.getAllOrders();
        String id = orders.get(0).getId();
        datastore.setReady(id);
        Order order = orders.stream().filter((Order i) -> i.getId().equals(id)).findFirst().get();
        assertThat(order.getCurrentState()).isEqualTo(OrderState.ORDER_READY);
    }


    @Test
    public void testSetRecievedOrder(){
        List<Order> orders = datastore.getAllOrders();
        String id = orders.get(0).getId();
        datastore.setReceived(id);
        Order order = orders.stream().filter((Order i) -> i.getId().equals(id)).findFirst().get();
        assertThat(order.getCurrentState()).isEqualTo(OrderState.ORDER_RECEIEVED
        );
    }


    @Test
    public void testAllOrderCompleted(){
        List<Order> orders = datastore.getAllOrders();
        for(Order o : orders){
            o.setCurrentState(OrderState.ORDER_PICKEDUP);
        }
        assert(datastore.isAllOrderCompleted());
    }

}
