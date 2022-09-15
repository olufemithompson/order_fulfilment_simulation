package com.orderfulfillmentsimulation.event.model;

import com.orderfulfillmentsimulation.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DispatchCourierEvent {
    Order order;
}
