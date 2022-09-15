package com.orderfulfillmentsimulation.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderfulfillmentsimulation.model.Order;
import com.orderfulfillmentsimulation.utils.OrderState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Datastore component that simulates database
 */
@Component
public class Datastore {
    @Value("classpath:data.json")
    Resource resourceFile;

    private int loadIndex=0;
    private static List<Order> orders;

    @PostConstruct
    public void init() throws IOException {
        initOrderTable();
    }

    private void initOrderTable() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        orders = mapper.readValue(resourceFile.getFile(), new TypeReference<List<Order>>(){});
    }



    public List<Order> getNextOrders(){
        List<Order> nextOrders = orders.subList(loadIndex, loadIndex+2);
        loadIndex+=2;
        return nextOrders;
    }

    public Order get(String id){
        return orders.stream().filter((Order i)->i.getId().equals(id)).findFirst().orElse(null);
    }

    public void setReady(String id){
        Optional<Order> order = orders.stream().filter((Order i)->i.getId().equals(id)).findFirst();
        if(order.isPresent()){
            order.get().setCurrentState(OrderState.ORDER_READY);
            order.get().setReadyTime(System.currentTimeMillis());
        }
    }

    public void setWaiting(String id){
        Optional<Order> order = orders.stream().filter((Order i)->i.getId().equals(id)).findFirst();
        if(order.isPresent()){
            order.get().setCurrentState(OrderState.ORDER_WAITING);
        }
    }

    public void setPickedUp(String id){
        Optional<Order> order = orders.stream().filter((Order i)->i.getId().equals(id)).findFirst();
        if(order.isPresent()){
            order.get().setCurrentState(OrderState.ORDER_PICKEDUP);
            order.get().setPickupTime(System.currentTimeMillis());
        }
    }

    public void setReceived(Order order){
        order.setCurrentState(OrderState.ORDER_RECEIEVED);
    }

    public boolean isAllOrderCompleted(){
        return orders.stream().filter((Order i)-> i.getCurrentState().equals(OrderState.ORDER_PICKEDUP)).count() == orders.size();
    }



}
