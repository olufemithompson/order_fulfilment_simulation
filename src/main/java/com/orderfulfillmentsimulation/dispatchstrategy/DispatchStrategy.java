package com.orderfulfillmentsimulation.dispatchstrategy;

import com.orderfulfillmentsimulation.model.Courier;
import com.orderfulfillmentsimulation.model.Order;

import java.util.Random;

/**
 * Base Dispatch Strategy class.
 * Child classes are implemented based on the actual strategy.
 *
 */
public abstract class DispatchStrategy {
    int low = 3;
    int high = 15;

    abstract void addCourier(Courier courier);
    abstract void orderReady(Order order);

    public void dispatch(Courier courier){

        //simulate time taken before courier arrives kitchen
        Random r = new Random();
        long timeTakenToArriveKitchen = (r.nextInt(high-low) + low) * 1000;
        try {
            Thread.sleep(timeTakenToArriveKitchen);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        addCourier(courier);
    }
}
