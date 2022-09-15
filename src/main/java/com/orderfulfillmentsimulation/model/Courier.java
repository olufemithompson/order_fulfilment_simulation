package com.orderfulfillmentsimulation.model;

import lombok.Data;


@Data
public class Courier {
    String id;
    String orderId;
    Long arriveTime;
}
