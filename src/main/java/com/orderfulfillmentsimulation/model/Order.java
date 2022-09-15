package com.orderfulfillmentsimulation.model;

import com.orderfulfillmentsimulation.utils.OrderState;
import lombok.Data;

import java.util.Date;

@Data
public class Order {
    String id;
    String name;
    Long prepTime;
    Long pickupTime;
    Long readyTime;


    OrderState currentState;

    Long deliveryTime;

}
