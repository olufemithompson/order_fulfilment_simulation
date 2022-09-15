package com.orderfulfillmentsimulation.event.publisher;

import com.orderfulfillmentsimulation.event.model.OrderPickupEvent;
import com.orderfulfillmentsimulation.event.model.OrderReadyEvent;
import com.orderfulfillmentsimulation.event.model.OrderReceivedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Event Publisher class that publish events
 * for when order is received or prepared
 */
@Component
public class EventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    EventPublisher(ApplicationEventPublisher publisher) {
        this.eventPublisher = publisher;
    }

    public void publishOrderReceivedEvent(OrderReceivedEvent event) {
        eventPublisher.publishEvent(event);
    }

    public void publishOrderReadyEvent(OrderReadyEvent event) {
        eventPublisher.publishEvent(event);
    }

    public void publishOrderPickedUpEvent(OrderPickupEvent orderPickupEvent){
        eventPublisher.publishEvent(orderPickupEvent);
    }

}
