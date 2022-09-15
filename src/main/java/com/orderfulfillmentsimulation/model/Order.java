package com.orderfulfillmentsimulation.model;

import com.orderfulfillmentsimulation.utils.OrderState;
import lombok.Data;


@Data
public class Order {
    String id;
    String name;
    Long prepTime;
    Long pickupTime;
    Long readyTime;
    Long courierArriveTime;
    OrderState currentState;

}
