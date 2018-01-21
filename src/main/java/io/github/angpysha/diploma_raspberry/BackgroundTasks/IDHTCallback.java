package io.github.angpysha.diploma_raspberry.BackgroundTasks;

import io.github.angpysha.diploma_bridge.Models.DHT11_Data;

/**
 *  Callback for DHT11 sensor
 */
public interface IDHTCallback {
    void acceptDHT(DHT11_Data t);
}
