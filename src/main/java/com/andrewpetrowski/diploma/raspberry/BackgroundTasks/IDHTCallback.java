package com.andrewpetrowski.diploma.raspberry.BackgroundTasks;

import com.andrewpetrowski.diploma.bridgelib.Models.DHT11_Data;

/**
 *  Callback for DHT11 sensor
 */
public interface IDHTCallback {
    void acceptDHT(DHT11_Data t);
}
