package com.orderfulfillmentsimulation.event.model;

import com.orderfulfillmentsimulation.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderReceivedEvent {
    List<Order> orders;
}
